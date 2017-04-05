package webServices;

import beans.Athletic;
import beans.Credentials;
import dao.impl.AthleticDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("atheletics")
public class AtheleticWebService {

    private AthleticDaoImpl athleticDao;

    @Context
    public void setContext() {
        athleticDao = new AthleticDaoImpl();
    }

    @GET
    @Path("/{id}")
    public Response getAthletic(@PathParam("id") String id) {
        try {
            Athletic athletic = athleticDao.getAthletic(id);
            return Response.ok(athletic).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLogin(Credentials credentials) {
        String username = credentials.getUsername();

        //Get athlectics from DB
        Athletic athletic = new Athletic();

        return Response.status(Response.Status.CREATED).entity(athletic).build();
    }
}
