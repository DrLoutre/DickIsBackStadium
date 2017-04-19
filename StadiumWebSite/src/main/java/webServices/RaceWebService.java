package webServices;

import beans.Lap;
import beans.Race;
import dao.LapDao;
import dao.impl.LapDaoImpl;
import dao.impl.RaceDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("races")
public class RaceWebService {

    private RaceDaoImpl raceDao = new RaceDaoImpl();
    private LapDaoImpl lapDao = new LapDaoImpl();

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

    @POST
    @Path("/{id}/laps")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLap(@PathParam("id") int id, Lap lap) {
        try {
            lapDao.addLap(lap.getID(), lap.getTemp().getKey().getMinutes(), lap.getTemp().getKey().getMinutes(),
                    lap.getTemp().getValue(), id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).entity(lap).build();
    }
}
