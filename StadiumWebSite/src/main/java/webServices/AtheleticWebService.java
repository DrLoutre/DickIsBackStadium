package webServices;

import beans.Athletic;
import beans.Credentials;
import dao.impl.AthleticDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("athletics")
public class AtheleticWebService {

    private AthleticDaoImpl athleticDao = new AthleticDaoImpl();

    @GET
    public Response getAthletics() {
        //TODO : Get all athlectics
        return  Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}")
    public Response getAthletic(@PathParam("id") String id) {
        Athletic athletic;
        try {
            athletic = athleticDao.getAthletic(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
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

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAthletic(@PathParam("id") String id) {
        //TODO : Put athletic
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLogin(Credentials credentials) {
        String username = credentials.getId();

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
        try {
            athleticDao.deleteAthletic(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
