package webServices;

import beans.Lap;
import dao.impl.LapDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class regroups all WebService associated to Laps
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("laps")
public class LapWebService {

    private LapDaoImpl lapDao = new LapDaoImpl();

    /**
     * Get a lap with a specific id
     * @param id The lap's id
     * @return Lap
     */
    @GET
    @Path("/{id}")
    public Response getLap(@PathParam("id") int id) {
        Lap lap;
        try {
            lap = lapDao.getLap(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(lap).build();
    }

    /**
     * Update a lap and his attributes with a specific id
     * @param id The lap's id
     * @param lap The lap object
     * @return Lap
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putLap(@PathParam("id") int id, Lap lap) {
        try {
            Lap lap1 = lapDao.getLap(id);
            if (lap.getTempHour() != lap1.getTempHour() || lap.getTempMin() != lap1.getTempMin() ||
                    lap.getTempSec() != lap1.getTempSec() || lap.getTempMs() != lap.getTempMs()) {
                lapDao.setTime(id, lap.getTempHour(), lap.getTempMin(), lap.getTempSec(), lap.getTempMs());
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(lap).build();
    }
}