package fun.is.quarkus.italktomyself;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.logging.Logger;

import fun.is.quarkus.italktomyself.api.TalkToMyselfApi;
import fun.is.quarkus.italktomyself.dto.HeartBeatDto;
import fun.is.quarkus.italktomyself.dto.InstanceOfMeDto;
import fun.is.quarkus.italktomyself.dto.ReplyDto;
import fun.is.quarkus.italktomyself.dto.StatusDto;
import fun.is.quarkus.italktomyself.mapper.DtoMapper;
import fun.is.quarkus.italktomyself.model.HeartBeat;
import fun.is.quarkus.italktomyself.model.InstanceOfMe;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.vertx.ConsumeEvent;

@Singleton
public class TalkToMyselfApp {

    final Logger LOG = Logger.getLogger(TalkToMyselfApp.class);

    Map<UUID, InstanceOfMe> instances;

    Map<UUID, HeartBeat> pendingHeartbeats;

    @ConfigProperty(name = "instance-of-me.servers")
    List<String> serviceUrls;

    boolean pause;

    UUID myInstanceId;

    @Inject
    DtoMapper mapper;

    void startUp(@Observes StartupEvent startupEvent) {
        myInstanceId = UUID.randomUUID();
        instances = Collections.synchronizedMap(new HashMap<UUID, InstanceOfMe>());
        pendingHeartbeats = Collections.synchronizedMap(new HashMap<UUID, HeartBeat>());
    }

    @ConsumeEvent("receive-heartbeat")
    public ReplyDto receiveHeartbeat(HeartBeatDto heartbeat) {
        LOG.info("Received Heartbeat From Event Bus: " + heartbeat);
        if (this.pause) {
            LOG.info("Simulating Slow Response with 2500ms pause.");
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
                LOG.error(e.getMessage() + e.getCause().getMessage());
            }
        }
        instances.put(heartbeat.sender(), new InstanceOfMe(heartbeat.url(), true));
        ReplyDto reply = new ReplyDto(myInstanceId, UUID.randomUUID(), heartbeat.messageId());
        LOG.info("Sending Reply: " + reply + " To: " + heartbeat.sender());
        return reply;
    }

    @ConsumeEvent("status")
    public StatusDto status(Object noValue) {

        List<InstanceOfMeDto> instanceDtos = new ArrayList<InstanceOfMeDto>();
        for (UUID key : instances.keySet()) {
            instanceDtos.add(mapper.instanceOfMeToDto(key, instances.get(key)));
        }
        List<HeartBeatDto> hBeatDtos = new ArrayList<HeartBeatDto>();
        for (HeartBeat hb : pendingHeartbeats.values()) {
            hBeatDtos.add(mapper.heartBeatToDto(hb));
        }
        StatusDto status = new StatusDto(myInstanceId, instanceDtos, hBeatDtos);
        return status;
    }

    @ConsumeEvent("sleep")
    public void sleep(boolean sleep) {
        this.pause = sleep;
    }

    @Scheduled(every = "{instance-of-me.schedule}")
    public void heartbeat() {
        LOG.info("Scheduler Fired");
        for (String url : serviceUrls) {
            HeartBeat hb = new HeartBeat(myInstanceId, UUID.randomUUID(), url);
            pendingHeartbeats.put(hb.getMessageId(), hb);
            TalkToMyselfApi api = RestClientBuilder.newBuilder().baseUri(URI.create(url)).build(TalkToMyselfApi.class);
            LOG.info("Sending Heartbeat: " + hb + " To: " + url);
            api.heartbeat(mapper.heartBeatToDto(hb)).ifNoItem().after(Duration.ofMillis(1000)).failWith(new Exception("Request Timeout")).subscribe().with(reply -> processHbReply(reply), fail -> handleFailure(hb, url, fail));
        }
    }

    private void processHbReply(Response response) {
        ReplyDto reply = response.readEntity(ReplyDto.class);
        LOG.info("Received HB Reply: " + response.getStatus() + " From: " + reply.sender());
        pendingHeartbeats.remove(reply.messageId());
    }

    private void handleFailure(HeartBeat hb, String url, Throwable error) {
        LOG.error("Failed sending heartbeat: " + hb + " To: " + url + " With Error: " + error.getMessage());
    }
}
