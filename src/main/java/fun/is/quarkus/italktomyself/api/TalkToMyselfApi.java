package fun.is.quarkus.italktomyself.api;

import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fun.is.quarkus.italktomyself.dto.HeartBeatDto;
import io.smallrye.mutiny.Uni;

@Path("/i-talk-to-myself")
public interface TalkToMyselfApi {
    
    @Path("/heartbeat")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> heartbeat(HeartBeatDto heartbeat);

    @Path("/status")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getStatus();

    @Path("/sleep")
    @POST
    public Uni<Response> sleep();

    @Path("wake")
    @POST
    public Uni<Response> wake();
}
