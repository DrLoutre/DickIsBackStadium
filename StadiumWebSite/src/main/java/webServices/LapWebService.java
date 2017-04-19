package webServices;

import beans.Lap;
import dao.impl.LapDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("laps")
public class LapWebService {

    private LapDaoImpl lapDao = new LapDaoImpl();

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
}
