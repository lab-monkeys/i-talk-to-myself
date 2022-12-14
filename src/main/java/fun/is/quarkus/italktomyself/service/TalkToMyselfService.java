package fun.is.quarkus.italktomyself.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fun.is.quarkus.italktomyself.api.TalkToMyselfApi;
import fun.is.quarkus.italktomyself.dto.HeartBeatDto;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;

import org.jboss.logging.Logger;

@ApplicationScoped
public class TalkToMyselfService implements TalkToMyselfApi {

    final Logger LOG = Logger.getLogger(TalkToMyselfService.class);

    @Inject
    EventBus eventBus;

    @Override
    public Uni<Response> heartbeat(HeartBeatDto heartbeat) {
        return eventBus.<HeartBeatDto>request("receive-heartbeat", heartbeat).onItem().transform(item -> Response.ok(item.body()).build());
    }

    @Override
    public Uni<Response> getStatus() {
        return eventBus.request("status", null).onItem().transform(item -> Response.ok(item.body()).build());
    }

    @Override
    public Uni<Response> getPendingHeartbeats() {
        return eventBus.request("no-response", null).onItem().transform(item -> Response.ok(item.body()).build());
    }

    @Override
    public Response sleep() {
        eventBus.send("sleep", true);
        return Response.ok().build();
    }

    @Override
    public Response wake() {
        eventBus.send("sleep", false);
        return Response.ok().build();
    }
}
