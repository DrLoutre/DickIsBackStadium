package webServices;

import beans.Athletic;
import beans.Lap;
import beans.Match;
import beans.Race;
import beans.custom.Credentials;
import beans.custom.MatchNotEnded;
import dao.impl.*;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * This class regroups all WebService associated to Athletics
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("athletics")
public class AtheleticWebService {

    private AthleticDaoImpl athleticDao = new AthleticDaoImpl();
    private LapDaoImpl lapDao = new LapDaoImpl();
    private MatchDaoImpl matchDao = new MatchDaoImpl();
    private TeamDaoImpl teamDao = new TeamDaoImpl();
    private RaceDaoImpl raceDao = new RaceDaoImpl();

    /**
     * Get all athletics
     * @return ArrayList<Team>
     */
    @GET
    public Response getAthletics() {
        ArrayList<Athletic> athletics;
        try {
            athletics = athleticDao.getAllAthletic();
        } catch (NotFoundException e) {
            return  Response.status(Response.Status.NOT_FOUND).build();
        }
        return  Response.ok(athletics).build();
    }

    /**
     * Get athletic associated to the id
     * @param id The athletic's id
     * @return Athletic
     */
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

    /**
     * Get laps from the last race from the athletics with the id
     * @param id The athletic's id
     * @return ArrayList<Lap>
     */
    @GET
    @Path("/{id}/races/last/laps")
    public Response getLastRaceAthletic(@PathParam("id") String id) {
        List<Lap> laps;
        try {
            laps = lapDao.getLastRace(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(laps).build();
    }

    /**
     * Get all laps from a race with its id
     * @param id The athletic's id
     * @param id1 The race's id
     * @return ArrayList<Lap>
     */
    @GET
    @Path("/{id}/races/{id1}/laps")
    public Response getlapFromRace(@PathParam("id") String id, @PathParam("id1") int id1) {
        List<Lap> laps;
        try {
            laps = lapDao.getAllLap(id1);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(laps).build();
    }

    /**
     * Get all matchs not ended for an athletic with his id
     * @param id The athletic's id
     * @return ArrayList<Match>
     */
    @GET
    @Path("/{id}/matchs/notended")
    public Response getMatchNotEnded(@PathParam("id") String id) {
        ArrayList<MatchNotEnded> matchNotEndeds = new ArrayList<>();
        try {
            List<Match> matchs = matchDao.getNotEndedMatch(id);
            for (Match match : matchs) {
                MatchNotEnded matchNotEnded = new MatchNotEnded();
                matchNotEnded.setTeam1(teamDao.getName(match.getTeamID1()));
                matchNotEnded.setTeam2(teamDao.getName(match.getTeamID2()));
                matchNotEnded.setDate(match.getDate());
                matchNotEndeds.add(matchNotEnded);
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(matchNotEndeds).build();
    }

    /**
     * Create a new athletic
     * @param athletic Athletic object
     * @return Athletic
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAthletic(Athletic athletic) {
        athleticDao.addAthletic(athletic.getNFC(), athletic.getPrenom(), athletic.getNom(), athletic.getAge(),
                athletic.getSex(), athletic.getMDP());
        return Response.status(Response.Status.CREATED).entity(athletic).build();
    }

    /**
     * Update an athletic with his id and his attributes
     * @param id The athletic's id
     * @param athletic1 Athletic object
     * @return Athletic
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAthletic(@PathParam("id") String id, Athletic athletic1) {
        Athletic athletic;
        try {
            athletic = athleticDao.getAthletic(id);
            if (!athletic.getPrenom().equals(athletic1.getPrenom())) {
                athleticDao.setFirstName(id, athletic1.getPrenom());
            }
            if (!athletic.getNom().equals(athletic1.getNom())) {
                athleticDao.setLastName(id, athletic1.getNom());
            }
            if (athletic.getMDP().equals(athletic1.getMDP())) {
                athleticDao.setPassword(id, athletic1.getMDP());
            }
            if (athletic.getAge() != athletic1.getAge()) {
                athleticDao.setAge(id, athletic1.getAge());
            }
            if (athletic.getSex().equals(athletic1.getSex())) {
                athleticDao.setSex(id, athletic1.getSex());
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(athletic1).build();
    }

    /**
     * Method use to login in the Android App
     * @param credentials The credentials of the user
     * @return Created if success, Unauthorized otherwise
     */
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

        if (athleticDao.connect(athletic.getMDP(),credentials.getPassword())) {
            return Response.status(Response.Status.CREATED).entity(athletic).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Delete the athletic with the associated id
     * @param id The athletic's id
     * @return 204 NO_CONTENT
     */
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
