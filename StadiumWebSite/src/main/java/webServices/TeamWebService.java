package webServices;

import beans.Team;
import dao.impl.TeamDaoImpl;
import exceptions.NotFoundException;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("teams")
public class TeamWebService {

    private TeamDaoImpl teamDao;

    @Context
    public void setContext(ServletContext servletContext) {
        teamDao = new TeamDaoImpl();
    }

    @GET
    public Response getTeams() {
        //TODO : Get all teams
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}")
    public Response getTeam(@PathParam("id") int id) {
        Team team;
        try {
            team = teamDao.getTeam(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(team).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTeam(Team team) {
        teamDao.addTeam(team.getId(), team.getNom());
        return Response.status(Response.Status.CREATED).entity(team).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTeam(@PathParam("id") int id) {
        try {
            teamDao.deleteTeam(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
