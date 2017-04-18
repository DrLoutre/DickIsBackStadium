package webServices;

import beans.Refreshment;
import dao.impl.RefreshmentDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("refreshments")
public class RefreshmentWebService {

    private RefreshmentDaoImpl refreshmentDao = new RefreshmentDaoImpl();

    @GET
    @Path("/{id}")
    public Response getRefreshment(@PathParam("id") int id) {
        Refreshment refreshment;
        try {
            refreshment = refreshmentDao.getRefreshment(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(refreshment).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRefreshment(Refreshment refreshment) {
        refreshmentDao.addRefreshment(refreshment.getId(), refreshment.getAttendance(), refreshment.getLocalisation());
        return Response.status(Response.Status.CREATED).entity(refreshment).build();
    }
}
