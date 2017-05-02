package webServices;

import beans.OccupancyRate;
import beans.Seat;
import beans.Tribune;
import beans.custom.SeatsByTribune;
import dao.impl.SeatDaoImpl;
import dao.impl.TribuneDaoImpl;
import exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.util.*;

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
    public Response getTribunes() {
        ArrayList<Tribune> tribunes;
        try {
            tribunes = tribuneDao.getAllTribune();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tribunes).build();
    }

    @GET
    @Path("/seats/available")
    public Response getSeatsTribunes() {
        ArrayList<Tribune> tribunes;
        ArrayList<SeatsByTribune> seatsByTribunes = new ArrayList<>();
        try {
            tribunes = tribuneDao.getAllTribune();
            for (Tribune tribune : tribunes) {
                SeatsByTribune seatsByTribune = new SeatsByTribune();
                seatsByTribune.setTribune(tribune);
                ArrayList<Seat> available = getAvailable(seatDao.getTribuneSeats(tribune.getNFC()));
                seatsByTribune.setSeats(available);
                seatsByTribunes.add(seatsByTribune);
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(seatsByTribunes).build();
    }

    @GET
    @Path("/{id}/seats")
    public Response getTribuneSeats(@PathParam("id") String id) {
        List<Integer> seats;
        try {
            seats = seatDao.getTribuneSeatsID(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(seats).build();
    }

    @GET
    @Path("/{id}/rate")
    public Response getTribuneOccupancyRate(@PathParam("id") String id) {
        List<Seat> seats;
        try {
            seats = seatDao.getTribuneSeats(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        int total = seats.size();
        int free = 0;
        for (Seat seat : seats) {
            if (!seat.getOccupied()) {
                free += 1;
            }
        }

        double freeRate = ((int)(((free / total) * 100) * 100)) / 100.;

        OccupancyRate occupancyRate = new OccupancyRate(freeRate, 100 - freeRate);

        return Response.ok(occupancyRate).build();
    }

    @GET
    @Path("/{id}/available")
    public Response getTribuneSeatsAvailable(@PathParam("id") String id) {
        List<Seat> seats;
        try {
            seats = seatDao.getTribuneSeats(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ArrayList<Seat> available = getAvailable(seats);

        return Response.ok(available).build();
    }

    private ArrayList<Seat> getAvailable(List<Seat> seats) {
        ArrayList<Seat> available = new ArrayList<>();

        for (Seat seat: seats) {
            if (!seat.getOccupied()) {
                available.add(seat);
            }
        }
        return available;
    }
}
