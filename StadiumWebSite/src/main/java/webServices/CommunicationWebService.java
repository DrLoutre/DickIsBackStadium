package webServices;

import beans.*;
import beans.custom.GoalCustom;
import beans.custom.LapCustom;
import beans.custom.RefreshmentsCustom;
import beans.custom.SeatsCustom;
import dao.impl.*;
import exceptions.NotFoundException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class regroups all WebService associated to Communication
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("communication")
public class CommunicationWebService {

    private final float BASE_BUVETTE = 1023;
    private RefreshmentDaoImpl refreshmentDao = new RefreshmentDaoImpl();
    private RaceDaoImpl raceDao = new RaceDaoImpl();
    private LapDaoImpl lapDao = new LapDaoImpl();
    private MatchDaoImpl matchDao = new MatchDaoImpl();
    private TribuneDaoImpl tribuneDao = new TribuneDaoImpl();
    private SeatDaoImpl seatDao = new SeatDaoImpl();

    /**
     * Use to update the refreshments in the database.
     * @param refreshmentsCustom The refreshment
     * @return CREATED
     */
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
        float newValue = Math.abs(base - BASE_BUVETTE);
        float value = newValue/BASE_BUVETTE;

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.floatValue();
    }

    /**
     * Use to create a new lap in the database
     * @param lapCustom The lap
     * @return CREATED
     */
    @POST
    @Path("/laps")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCustomLap(LapCustom lapCustom) {
        Race race;
        DateTime dateTime = new DateTime(Long.valueOf(lapCustom.getTemps()));

        try {
            if (lapCustom.getNbrLaps() == 0) {
                race = new Race();
                race.setNFC(lapCustom.getRfid());
                int id = raceDao.addRace(lapCustom.getRfid());
                race.setId(id);

                lapDao.addLap(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                        dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute(),
                        dateTime.getMillisOfSecond(), true, race.getId());
            } else {
                List<Race> races = raceDao.getRacesList(lapCustom.getRfid());
                race = races.get(races.size() - 1);
                List<Lap> laps = lapDao.getAllLapWithBeginning(race.getId());
                if (laps.size() != 0) {
                    DateTime baseDate = new DateTime(laps.get(0).getYear(), laps.get(0).getMonth(), laps.get(0).getDay(),
                            laps.get(0).getTempHour(), laps.get(0).getTempMin(), laps.get(0).getTempSec(), laps.get(0).getTempMs());
                    for (int i = 1; i < laps.size(); i++) {
                        baseDate = baseDate.plusHours(laps.get(i).getTempHour());
                        baseDate = baseDate.plusMinutes(laps.get(i).getTempMin());
                        baseDate = baseDate.plusSeconds(laps.get(i).getTempSec());
                        baseDate = baseDate.plusMillis(laps.get(i).getTempMs());
                    }
                    dateTime = dateTime.minusHours(baseDate.getHourOfDay());
                    dateTime = dateTime.minusMinutes(baseDate.getMinuteOfHour());
                    dateTime = dateTime.minusSeconds(baseDate.getSecondOfMinute());
                    dateTime = dateTime.minusMillis(baseDate.getMillisOfSecond());
                }

                lapDao.addLap(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                        dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute(),
                        dateTime.getMillisOfSecond(), false, race.getId());
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Use to update a match in the database
     * @param goalCustom A goal
     * @return CREATED
     */
    @POST
    @Path("/goals")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCustomGoal(GoalCustom goalCustom) {
        try {
            ArrayList<Match> matchs = matchDao.getNotEndedMatch();
            Collections.sort(matchs);

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

    @POST
    @Path("/tribunes/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTribunes(@PathParam("id") int id, SeatsCustom seatsCustom) {
        try {
            Tribune tribune = tribuneDao.getTribune(id);
            List<Seat> seats = seatDao.getTribuneSeats(tribune.getNFC());
            int i = 0;
            for (Seat seat : seats) {
                seatDao.setOccupiedState(seat.getID(), seatsCustom.getOccupArray()[i]);
                i++;
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/newMatch")
    public Response getNewMatchs() {
        ArrayList<Match> matches;
        try {
            matches = matchDao.getNotEndedMatch();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(matches).build();
    }
}
