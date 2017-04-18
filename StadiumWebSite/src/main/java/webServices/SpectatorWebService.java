package webServices;

import beans.Spectator;
import dao.impl.SpectatorDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

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

    @GET
    public Response getSpectators() {
        ArrayList<Spectator> spectators;
        try {
            spectators = spectatorDao.getAllSpectator();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(spectators).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSpectator(@PathParam("id") int id) {
        try {
            spectatorDao.deleteSpectator(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
