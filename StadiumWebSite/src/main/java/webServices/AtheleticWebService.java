package webServices;

import beans.Athletic;
import beans.Credentials;
import dao.impl.AthleticDaoImpl;
import exceptions.NotFoundException;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("athletics")
public class AtheleticWebService {

    private AthleticDaoImpl athleticDao;

    @Context
    public void setContext(ServletContext servletContext) {
        athleticDao = new AthleticDaoImpl();
    }

    @GET
    @Path("/{id}")
    public Response getAthletic(@PathParam("id") String id) {
        Athletic athletic;
        try {
            athletic = athleticDao.getAthletic(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        return Response.ok(athletic).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAthletic(Athletic athletic) {
        athleticDao.addAthletic(athletic.getNFC(), athletic.getPrenom(), athletic.getNom(), athletic.getAge(),
                athletic.getSex(), athletic.getMDP());
        return Response.status(Response.Status.CREATED).entity(athletic).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLogin(Credentials credentials) {
        String username = credentials.getUsername();

        //Get athlectics from DB
        Athletic athletic;
        try {
            athletic = athleticDao.getAthletic(username);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (athletic.getMDP().equals(credentials.getPassword())) {
            return Response.status(Response.Status.CREATED).entity(athletic).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAthletic(@PathParam("id") String id) {
        //TODO : Delete athl
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
