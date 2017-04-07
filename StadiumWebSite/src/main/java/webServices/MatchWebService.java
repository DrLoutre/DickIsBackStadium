package webServices;

import beans.Match;
import dao.impl.MatchDaoImpl;
import exceptions.NotFoundException;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("matchs")
public class MatchWebService {

    private MatchDaoImpl matchDao;

    @Context
    public void setContext(ServletContext servletContext) {
        matchDao = new MatchDaoImpl();
    }

    @GET
    @Path("/{id}")
    public Response getMatch(@PathParam("id") int id) {
        Match match;
        try {
            match = matchDao.getMatch(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(match).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postMatch(Match match) {
        try {
            matchDao.addMatch(match.getID(), match.getTeamID1(), match.getTeamID2());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).entity(match).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMatch(@PathParam("id") int id) {
        try {
            matchDao.deleteMatch(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }
}
