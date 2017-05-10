package webServices;

import beans.Match;
import dao.impl.MatchDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class regroups all WebService associated to Matchs
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("matchs")
public class MatchWebService {

    private MatchDaoImpl matchDao = new MatchDaoImpl();

    /**
     * Get a match with a specific attribute
     * @param id The match's id
     * @return Match
     */
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

    /**
     * Create a new match
     * @param match Match object
     * @return Match
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postMatch(Match match) {
        try {
           int id =  matchDao.addMatch(match.getTeamID1(), match.getTeamID2(), match.getGoals1(), match.getGoals2(),
                    match.getDate(), match.getEnded());
            match.setID(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).entity(match).build();
    }

    /**
     * Update the match and all his attributes
     * @param id The match's id
     * @param match1 The match object
     * @return Match
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putMatch(@PathParam("id") int id, Match match1) {
        Match match2;
        try {
            match2 = matchDao.getMatch(id);

            if (match2.getTeamID1() != match1.getTeamID1()) {
                matchDao.setIDTeam1(id, match1.getTeamID1());
            }
            if (match2.getTeamID2() != match1.getTeamID2()) {
                matchDao.setIDTeam1(id, match1.getTeamID2());
            }
            if (!match2.getDate().equals(match1.getDate())) {
                matchDao.setDate(id, match1.getDate());
            }
            if (match2.getGoals1() != match1.getGoals1() || match2.getGoals2() != match1.getGoals2()) {
                matchDao.setGoals(id, match1.getGoals1(), match1.getGoals2());
            }
            if (match2.getEnded() != match1.getEnded()) {
                matchDao.setState(id, match1.getEnded());
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(match1).build();
    }

    /**
     * Delete the match with the specific id
     * @param id The match's id
     * @return 204 NO_CONTENT
     */
    @DELETE
    @Path("/{id}")
    public Response deleteMatch(@PathParam("id") int id) {
        try {
            matchDao.deleteMatch(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
