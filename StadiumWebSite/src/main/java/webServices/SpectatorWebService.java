package webServices;

import beans.Spectator;
import dao.impl.SpectatorDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("/spectators")
public class SpectatorWebService {

    private SpectatorDaoImpl spectatorDao = new SpectatorDaoImpl();

    @GET
    @Path("/{id}")
    public Response getSpectator(@PathParam("id") int id) {
        Spectator spectator;
        try {
            spectator = spectatorDao.getSpectator(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(spectator).build();
    }

    public Response getSpectators() {
        // Todo : Get all spectators
        return Response.ok().build();
    }
}
