package webServices;

import beans.Athletic;
import beans.Lap;
import beans.Match;
import beans.custom.Credentials;
import beans.custom.MatchNotEnded;
import dao.impl.AthleticDaoImpl;
import dao.impl.LapDaoImpl;
import dao.impl.MatchDaoImpl;
import dao.impl.TeamDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("athletics")
public class AtheleticWebService {

    private AthleticDaoImpl athleticDao = new AthleticDaoImpl();
    private LapDaoImpl lapDao = new LapDaoImpl();
    private MatchDaoImpl matchDao = new MatchDaoImpl();
    private TeamDaoImpl teamDao = new TeamDaoImpl();

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

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLogin(Credentials credentials) {
        String username = credentials.getId();
        String password = credentials.getPassword();

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
