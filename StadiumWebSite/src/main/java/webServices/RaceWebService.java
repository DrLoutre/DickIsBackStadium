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

/**
 * This class regroups all WebService associated to Races
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("races")
public class RaceWebService {

    private RaceDaoImpl raceDao = new RaceDaoImpl();
    private LapDaoImpl lapDao = new LapDaoImpl();

    /**
     * Get a race with a specific id
     * @param id The race's id
     * @return Race
     */
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

    /**
     * Create a new race associated to an athletic
     * @param race Race object
     * @return Race
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRace(Race race) {
        try {
            int id = raceDao.addRace(race.getNFC());
            race.setId(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).entity(race).build();
    }

    /**
     * Create a new lap associated to a race
     * @param id The race's id
     * @param lap The lap object
     * @return Lap
     */
    @POST
    @Path("/{id}/laps")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLap(@PathParam("id") int id, Lap lap) {
        try {
            int idLap = lapDao.addLap(lap.getYear(), lap.getMonth(), lap.getDay(), lap.getTempHour(), lap.getTempMin(),
                    lap.getTempSec(), lap.getTempMs(), lap.getIsBeginning(), id);
            lap.setID(idLap);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).entity(lap).build();
    }
}
