package webServices;

import beans.Team;
import dao.impl.TeamDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * This class regroups all WebService associated to Teams
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("teams")
public class TeamWebService {

    private TeamDaoImpl teamDao =  new TeamDaoImpl();

    /**
     * It returns all teams that signed up
     * @return ArrayList<Team>
     */
    @GET
    public Response getTeams() {
        ArrayList<Team> teamArrayList;
        try {
            teamArrayList = teamDao.getAllTeam();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(teamArrayList).build();
    }

    /**
     * Return the team associated to the id. Return a 404 not found if it does not exist
     * @param id The team object
     * @return Team
     */
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

    /**
     * Create a new team
     * @param team The team object
     * @return Team
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTeam(Team team) {
        int id = teamDao.addTeam(team.getNom());
        team.setId(id);
        return Response.status(Response.Status.CREATED).entity(team).build();
    }

    /**
     * Update all attributes of a team
     * @param id The team's is
     * @param team The team object
     * @return Team
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putTeam(@PathParam("id") int id, Team team) {
        try {
            Team team1 = teamDao.getTeam(id);
            if (!team.getNom().equals(team1.getNom())) {
                teamDao.setName(id, team.getNom());
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(team).build();
    }

    /**
     * Delete the team with the associated id
     * @param id The team's id
     * @return 204 NO_CONTENT
     */
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
