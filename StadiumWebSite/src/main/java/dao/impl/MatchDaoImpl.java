package dao.impl;

import beans.Match;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.Dao;
import dao.MatchDao;
import dao.PlaysInDao;
import dao.TeamDao;
import exceptions.FailureException;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import stade.data.MatchsData;
import stade.data.QMatchs;

public class MatchDaoImpl extends Dao implements MatchDao{

    private final TeamDao teamDao;
    private final PlaysInDao playsInDao;
    
    private static final QMatchs MATCH = QMatchs.matchs;
    
    public MatchDaoImpl(){
        super();
        teamDao = new TeamDaoImpl();
        playsInDao = new PlaysInDaoImpl();
    }
    
    @Override
    public int addMatch(int idTeam1, int idTeam2, int goals1, int goals2, 
            String date, boolean ended) throws NotFoundException {
        Assert.isTrue(idTeam1 >= 0);
        Assert.isTrue(idTeam2 >= 0);
        Assert.isTrue(goals1 >= 0);
        Assert.isTrue(goals2 >= 0);
        Assert.notNull(date);
        Assert.isTrue(date.length() > 0);
        
        if(!teamDao.teamExists(idTeam1)) throw new NotFoundException("The team "
                + "with the id " + idTeam1 + "does not exists in the database");
        if(!teamDao.teamExists(idTeam2)) throw new NotFoundException("The team "
                + "with the id " + idTeam2 + "does not exists in the database");
        
        MatchsData data = toData(idTeam1, idTeam2, goals1, goals2, date, 
                ended);
        int ID = queryFactory.insert(MATCH).populate(data)
                .executeWithKey(MATCH.idMatch);
        closeConnection();
        
        return ID;
    }
    
    @Override
    public Match getMatch(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        MatchsData data = queryFactory.selectFrom(MATCH)
                .where(MATCH.idMatch.eq(ID)).fetchFirst();
        closeConnection();
        
        if (data == null) throw new NotFoundException("Match "+ ID 
                + " has not been found in the database");
        
        return toMatch(data);
    }

    @Override
    public void deleteMatch(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);

        if(matchExists(ID)) throw new IntegrityException("A match already "
                + "exists in the database with the ID : " + ID);

        long rows = queryFactory.delete(MATCH).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();

        Assert.isTrue(rows == 1);
    }

    @Override
    public boolean matchExists(int ID) {
        Assert.isTrue(ID >= 0);
        
        MatchsData data = queryFactory.selectFrom(MATCH)
                .where(MATCH.idMatch.eq(ID)).fetchFirst();
        closeConnection();
        
        return (data != null);
    }
    
    @Override
    public Pair<Integer,Integer> getGoals(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        if(!matchExists(ID)) throw new NotFoundException("Match "+ ID 
                + " has not been found in the database");
        
        MatchsData data = queryFactory.selectFrom(MATCH)
                .where(MATCH.idMatch.eq(ID)).fetchFirst();
        closeConnection();
        Assert.notNull(data);
        
        return new Pair<>(data.getGoal1(),data.getGoal2());
    }
    
    @Override
    public ArrayList<Match> getAllMatch() throws NotFoundException {
        List<MatchsData> data = queryFactory.select(MATCH).from(MATCH).fetch();
        closeConnection();

        if (data.isEmpty()) throw new NotFoundException("Matchs"
                + " has not been found in the database");
        
        ArrayList<Match> matchs = new ArrayList<>();
        for (MatchsData data1 : data) {
            matchs.add(toMatch(data1));
        }
                
        return matchs;
    }
    
    @Override
    public void setGoals(int ID, int goals1, int goals2) 
            throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(goals1 >= 0);
        Assert.isTrue(goals2 >= 0);
        
        Match match = getMatch(ID);
//        match.setTeamGoals(match.getTeamID1(), goals1);
//        match.setTeamGoals(match.getTeamID2(), goals2);
        match.setGoals1(goals1);
        match.setGoals1(goals2);
        MatchsData data = toData(match);
        SQLUpdateClause update = queryFactory.update(MATCH);
        
        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

//    @Override
//    public void setTeamGoals(int ID, int idTeam, int goals) 
//            throws IntegrityException, NotFoundException {
//        Assert.isTrue(ID >= 0);
//        Assert.isTrue(idTeam >= 0);
//        Assert.isTrue(goals >= 0);
//        
//        Match match = getMatch(ID);
//        try {
//            match.setTeamGoals(idTeam, goals);
//        } catch (NotFoundException ex) {
//            throw new IntegrityException(ex.getMessage());
//        }
//        MatchsData data = toData(match);
//        SQLUpdateClause update = queryFactory.update(MATCH);
//        
//        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
//                .execute();
//        closeConnection();
//        
//        Assert.isTrue(rows == 1);
//    }
    
    @Override
    public void SetIDTeam(int ID, int idTeam1, int idTeam2) 
            throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(idTeam1 >= 0);
        Assert.isTrue(idTeam2 >= 0);
        
        Match match = getMatch(ID);
        if(!teamDao.teamExists(idTeam1)) throw new NotFoundException("The team "
                + "with the id " + idTeam1 + "does not exists in the database");
        if(!teamDao.teamExists(idTeam2)) throw new NotFoundException("The team "
                + "with the id " + idTeam2 + "does not exists in the database");
        match.setTeamID1(idTeam1);
        match.setTeamID2(idTeam2);
        MatchsData data = toData(match);
        SQLUpdateClause update = queryFactory.update(MATCH);
        
        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setIDTeam1(int ID, int idTeam) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(idTeam >= 0);
        
        Match match = getMatch(ID);
        if(!teamDao.teamExists(idTeam)) throw new NotFoundException("The team "
                + "with the id " + idTeam + "does not exists in the database");
        match.setTeamID1(idTeam);
        MatchsData data = toData(match);
        SQLUpdateClause update = queryFactory.update(MATCH);
        
        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setIDTeam2(int ID, int idTeam) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(idTeam >= 0);
        
        Match match = getMatch(ID);
        if(!teamDao.teamExists(idTeam)) throw new NotFoundException("The team "
                + "with the id " + idTeam + "does not exists in the database");
        match.setTeamID2(idTeam);
        MatchsData data = toData(match);
        SQLUpdateClause update = queryFactory.update(MATCH);
        
        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public ArrayList<Match> getEndedMatch() throws NotFoundException {
        ArrayList<Match> matchs = getAllMatch();
        
        ArrayList<Match> returnList = new ArrayList<>();
        for(Match data : matchs){
            if(data.getEnded()){
                returnList.add(data);
            }
        }
        
        return returnList;
    }

    @Override
    public ArrayList<Match> getNotEndedMatch() throws NotFoundException {
        ArrayList<Match> matchs = getAllMatch();
        
        ArrayList<Match> returnList = new ArrayList<>();
        for(Match data : matchs){
            if(!data.getEnded()){
                returnList.add(data);
            }
        }
        
        return returnList;
    }

    @Override
    public void setDate(int ID, String date) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.notNull(date);
        
        Match match = getMatch(ID);
        match.setDate(date);
        
        MatchsData data = toData(match);
        SQLUpdateClause update = queryFactory.update(MATCH);
        
        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setState(int ID, boolean ended) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        Match match = getMatch(ID);
        match.setEnded(ended);
        MatchsData data = toData(match);
        SQLUpdateClause update = queryFactory.update(MATCH);
        
        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public List<Match> getNotEndedMatch(String athleticNFC) 
            throws NotFoundException {
        Assert.notNull(athleticNFC);
        Assert.isTrue(athleticNFC.length() > 0);
        
        List<Integer> teamIDList = playsInDao.getAllTeamID(athleticNFC);
        ArrayList<Match> notEndedMatchList = getNotEndedMatch();
        List<Match> returnValue = new LinkedList<>();
        for(Match match : notEndedMatchList){
            if(teamIDList.contains(match.getTeamID1()) 
                    || teamIDList.contains(match.getTeamID2()))
                returnValue.add(match);
        }
        
        return returnValue;
    }
    
    private MatchsData toData(Match match){
        try {
            return toData(match.getID(), match.getTeamID1(), match.getTeamID2(),
                    match.getGoals1(), match.getGoals2(), match.getDate(), 
                    match.getEnded());
        } catch (Exception ex){
            throw new FailureException(ex.getMessage());
        }
    }
    
    private MatchsData toData(int ID, int idTeam1, int idTeam2, int goals1, 
            int goals2, String date, boolean ended) {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(idTeam1 >= 0);
        Assert.isTrue(idTeam2 >= 0);
        Assert.isTrue(goals1 >= 0);
        Assert.isTrue(goals2 >= 0);
        Assert.notNull(date);
        Assert.isTrue(date.length() > 0);
        
        MatchsData data = new MatchsData();
        data.setIdMatch(ID);
        data.setIdTeam1(idTeam1);
        data.setIdTeam2(idTeam2);
        data.setGoal1(goals1);
        data.setGoal2(goals2);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            data.setMatchDate(new Timestamp(format.parse(date).getTime()));
        } catch (ParseException ex){
            throw new FailureException(ex.getMessage());
        }
        data.setEnded(ended);
        return data;
    }
    
    private MatchsData toData(int idTeam1, int idTeam2, int goals1, 
            int goals2, String date, boolean ended) {
        Assert.isTrue(idTeam1 >= 0);
        Assert.isTrue(idTeam2 >= 0);
        Assert.isTrue(goals1 >= 0);
        Assert.isTrue(goals2 >= 0);
        Assert.notNull(date);
        
        MatchsData data = new MatchsData();
        data.setIdTeam1(idTeam1);
        data.setIdTeam2(idTeam2);
        data.setGoal1(goals1);
        data.setGoal2(goals2);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println(date);
        try {
            data.setMatchDate(new Timestamp(format.parse(date).getTime()));
        } catch (ParseException ex){
            throw new FailureException(ex.getMessage());
        }
        data.setEnded(ended);
        return data;
    }
    
    private Match toMatch(MatchsData data){
        Match returnValue = new Match();
        returnValue.setTeamID1(data.getIdTeam1());
        returnValue.setTeamID2(data.getIdTeam2());
//        try {
//            returnValue.setTeamGoals(data.getIdTeam1(), data.getGoal1());
//            returnValue.setTeamGoals(data.getIdTeam2(), data.getGoal2());
//        } catch (Exception ex){
//            throw new FailureException(ex.getMessage());
//        }
        returnValue.setGoals1(data.getGoal1());
        returnValue.setGoals1(data.getGoal2());
        returnValue.setID(data.getIdMatch());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        returnValue.setDate(format.format(data.getMatchDate()));
        returnValue.setEnded(data.getEnded());
        return returnValue;
    }
}
