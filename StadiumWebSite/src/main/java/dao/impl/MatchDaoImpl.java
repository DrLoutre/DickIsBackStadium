package dao.impl;

import beans.Match;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.Dao;
import dao.MatchDao;
import dao.TeamDao;
import exceptions.FailureException;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import stade.data.MatchsData;
import stade.data.QMatchs;

public class MatchDaoImpl extends Dao implements MatchDao{

    private final TeamDao teamDao;
    
    private static final QMatchs MATCH = QMatchs.matchs;
    
    public MatchDaoImpl(){
        super();
        teamDao = new TeamDaoImpl();
    }
    
    @Override
    public void addMatch(int ID, int idTeam1, int idTeam2, int goals1, int goals2, 
            Date date, boolean ended) 
            throws IntegrityException, NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(idTeam1 >= 0);
        Assert.isTrue(idTeam2 >= 0);
        Assert.isTrue(goals1 >= 0);
        Assert.isTrue(goals2 >= 0);
        Assert.notNull(date);
        
        if(matchExists(ID)) throw new IntegrityException("A match already "
                + "exists in the database with the ID : " + ID);
        
        if(!teamDao.teamExists(idTeam1)) throw new NotFoundException("The team "
                + "with the id " + idTeam1 + "does not exists in the database");
        if(!teamDao.teamExists(idTeam2)) throw new NotFoundException("The team "
                + "with the id " + idTeam2 + "does not exists in the database");
        
        MatchsData data = toData(ID, idTeam1, idTeam2, goals1, goals2, date, 
                ended);
        long rows = queryFactory.insert(MATCH).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
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
        
        return new Pair(data.getGoal1(),data.getGoal2());
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
        match.setTeamGoals(match.getTeamID1(), goals1);
        match.setTeamGoals(match.getTeamID2(), goals2);
        MatchsData data = toData(match);
        SQLUpdateClause update = queryFactory.update(MATCH);
        
        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setTeamGoals(int ID, int idTeam, int goals) 
            throws IntegrityException, NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(idTeam >= 0);
        Assert.isTrue(goals >= 0);
        
        Match match = getMatch(ID);
        try {
            match.setTeamGoals(idTeam, goals);
        } catch (NotFoundException ex) {
            throw new IntegrityException(ex.getMessage());
        }
        MatchsData data = toData(match);
        SQLUpdateClause update = queryFactory.update(MATCH);
        
        long rows = update.set(MATCH, data).where(MATCH.idMatch.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    private MatchsData toData(Match match){
        try {
            return toData(match.getID(), match.getTeamID1(), match.getTeamID2(),
                    match.getTeamGoals(match.getTeamID1()), 
                    match.getTeamGoals(match.getTeamID2()), match.getDate(), 
                    match.getEnded());
        } catch (Exception ex){
            throw new FailureException(ex.getMessage());
        }
    }
    
    private MatchsData toData(int ID, int idTeam1, int idTeam2, int goals1, 
            int goals2, Date date, boolean ended) {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(idTeam1 >= 0);
        Assert.isTrue(idTeam2 >= 0);
        Assert.isTrue(goals1 >= 0);
        Assert.isTrue(goals2 >= 0);
        Assert.notNull(date);
        
        MatchsData data = new MatchsData();
        data.setIdMatch(ID);
        data.setIdTeam1(idTeam1);
        data.setIdTeam2(idTeam2);
        data.setGoal1(goals1);
        data.setGoal2(goals2);
        data.setMatchDate(new Timestamp(date.getTime()));
        data.setEnded(ended);
        return data;
    }
    
    private Match toMatch(MatchsData data){
        Match returnValue = new Match();
        returnValue.setTeamID(data.getIdTeam1(), data.getIdTeam2());
        try {
            returnValue.setTeamGoals(data.getIdTeam1(), data.getGoal1());
            returnValue.setTeamGoals(data.getIdTeam2(), data.getGoal2());
        } catch (Exception ex){
            throw new FailureException(ex.getMessage());
        }
        returnValue.setID(data.getIdMatch());
        returnValue.setDate(data.getMatchDate());
        returnValue.setEnded(data.getEnded());
        
        return returnValue;
    }
}
