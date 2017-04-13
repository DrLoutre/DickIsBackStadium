package webServices;

import beans.OccupancyRate;
import beans.Seat;
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
import java.text.DecimalFormat;
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

    @GET
    @Path("/{id}/rate")
    public Response getTribuneOccupancyRate(@PathParam("id") String id) {
        List<Integer> seats;
        try {
            seats = seatDao.getTribuneSeats(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        int total = seats.size();
        int free = 0;
        for (Integer integer : seats) {
            try {
                Seat seat = seatDao.getSeat(integer);
                if (!seat.getOccupied()) {
                    free += 1;
                }
            } catch (NotFoundException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

        double freeRate = ((int)(((free / total) * 100)*100))/100.;

        OccupancyRate occupancyRate = new OccupancyRate(freeRate, 100-freeRate);

        return Response.ok(occupancyRate).build();
    }
}
