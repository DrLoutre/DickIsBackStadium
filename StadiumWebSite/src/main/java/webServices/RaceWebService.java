package webServices;

import beans.Race;
import dao.impl.RaceDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("races")
public class RaceWebService {

    private RaceDaoImpl raceDao = new RaceDaoImpl();

    @GET
    @Path("/{id}")
    public Response getRace(@PathParam("id") int id) {
        Race race;
        try {
            race = raceDao.getRace(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(race).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRace(Race race) {
        try {
            raceDao.addRace(race.getId(), race.getNFC());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).entity(race).build();
    }
}
