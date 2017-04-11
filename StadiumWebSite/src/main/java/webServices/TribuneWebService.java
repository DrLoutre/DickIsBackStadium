package webServices;

import beans.Tribune;
import dao.impl.SeatDaoImpl;
import dao.impl.TribuneDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("tribunes")
public class TribuneWebService {

    private TribuneDaoImpl tribuneDao =  new TribuneDaoImpl();
    private SeatDaoImpl seatDao = new SeatDaoImpl();

    @GET
    @Path("/{id}")
    public Response getTribune(@PathParam("id") String id) {
        Tribune tribune;
        try {
            tribune = tribuneDao.getTribune(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tribune).build();
    }

    @GET
    @Path("/{id}/seats")
    public Response getTribuneSeats(@PathParam("id") String id) {
        List<Integer> seats;
        try {
            seats = seatDao.getTribuneSeats(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(seats).build();
    }
}
