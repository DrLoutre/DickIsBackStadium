package webServices;

import beans.Lap;
import beans.Match;
import beans.Race;
import beans.Refreshment;
import beans.custom.GoalCustom;
import beans.custom.LapCustom;
import beans.custom.RefreshmentsCustom;
import dao.impl.*;
import exceptions.NotFoundException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("communication")
public class CommunicationWebService {

    private final float BASE_BUVETTE = 1023;
    private RefreshmentDaoImpl refreshmentDao = new RefreshmentDaoImpl();
    private RaceDaoImpl raceDao = new RaceDaoImpl();
    private LapDaoImpl lapDao = new LapDaoImpl();
    private MatchDaoImpl matchDao = new MatchDaoImpl();

    @POST
    @Path("/refreshments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCustomRefresh(RefreshmentsCustom refreshmentsCustom) {
        try {
            float value1 = toAttendance(refreshmentsCustom.getAttendance1());
            float value2 = toAttendance(refreshmentsCustom.getAttendance2());

            ArrayList<Refreshment> refreshments = refreshmentDao.getAllRefreshment();
            refreshmentDao.setAttendance(refreshments.get(0).getId(), value1);
            refreshmentDao.setAttendance(refreshments.get(1).getId(), value2);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    private float toAttendance(int base) {
        float value = base/BASE_BUVETTE;

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.floatValue();
    }

    @POST
    @Path("/laps")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCustomLap(LapCustom lapCustom) {
        Race race;
        try {
            if (lapCustom.getNbrLaps() == 1) {
                race = new Race();
                race.setNFC(lapCustom.getRfid());
                int id = raceDao.addRace(lapCustom.getRfid());
                race.setId(id);
            } else {
                List<Race> races = raceDao.getRacesList(lapCustom.getRfid());
                race = races.get(races.size() - 1);
            }

            Lap lap = new Lap();
            lap.setIdRace(race.getId());
            DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime dateTime = df.parseDateTime(lapCustom.getTemps());
            lap.setTempHour(dateTime.getHourOfDay());
            lap.setTempMin(dateTime.getMinuteOfHour());
            lap.setTempSec(dateTime.getSecondOfMinute());

            lapDao.addLap(lap.getTempHour(), lap.getTempMin(), lap.getTempSec(), lap.getTempMs(),
                    lap.getIdRace());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/goals")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCustomGoal(GoalCustom goalCustom) {
        try {
            ArrayList<Match> matchs = matchDao.getNotEndedMatch();
            Match match = matchs.get(0);

            DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime beginning = df.parseDateTime(match.getDate()); //Beginning
            DateTime end = beginning.plusMinutes(100); //End
            DateTime now = new DateTime(); //Now

            if (now.getMinuteOfDay() >= beginning.getMinuteOfDay() &&
                    now.getMinuteOfDay() <= end.getMinuteOfDay()) {
                if (now.getMinuteOfDay() <= beginning.plusMinutes(50).getMinuteOfDay()) {
                    if (goalCustom.isDroit()) {
                        matchDao.setGoals(match.getID(), match.getGoals1() + 1, match.getGoals2());
                    } else {
                        matchDao.setGoals(match.getID(), match.getGoals1(), match.getGoals2() + 1);
                    }
                } else {
                    if (goalCustom.isDroit()) {
                        matchDao.setGoals(match.getID(), match.getGoals1(), match.getGoals2() + 1);
                    } else {
                        matchDao.setGoals(match.getID(), match.getGoals1() + 1, match.getGoals2());

                    }

                }
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }
}
