package dao.impl;

import beans.Team;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.Dao;
import dao.TeamDao;
import exceptions.NotFoundException;
import stade.data.QTeam;
import stade.data.TeamData;

import java.util.ArrayList;
import java.util.List;

public class TeamDaoImpl extends Dao implements TeamDao{

    private static final QTeam TEAM = QTeam.team;
    
    @Override
    public int addTeam(String Name){
        Assert.notNull(Name);
        Assert.isTrue(Name.length() > 0);
        
        TeamData data = toData(Name);
        int ID = queryFactory.insert(TEAM).populate(data)
                .executeWithKey(TEAM.idTeam);
        closeConnection();
        
        return ID;
    }

    @Override
    public boolean teamExists(int ID) {
        Assert.isTrue(ID >= 0);
        
        TeamData data = queryFactory.selectFrom(TEAM)
                .where(TEAM.idTeam.eq(ID)).fetchFirst();
        closeConnection();
        
        return (data != null);
    }
    
    @Override
    public ArrayList<Team> getAllTeam() throws NotFoundException {
        List<TeamData> data = queryFactory.select(TEAM).from(TEAM).fetch();
        closeConnection();

        if (data.isEmpty()) throw new NotFoundException("Matchs"
                + " has not been found in the database");
        
        ArrayList<Team> teams = new ArrayList<>();
        for (TeamData data1 : data) {
            teams.add(toTeam(data1));
        }
                
        return teams;
    }
    
    @Override
    public Team getTeam(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        TeamData data = queryFactory.selectFrom(TEAM)
                .where(TEAM.idTeam.eq(ID)).fetchFirst();
        closeConnection();
        
        if (data==null) throw new NotFoundException("Team "+ ID 
                + " has not been found in the database");
        
        return toTeam(data);
    }

    @Override
    public void deleteTeam(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);

        if (!teamExists(ID)) throw new NotFoundException("Team " + ID
                + " has not been found in the database");

        long rows = queryFactory.delete(TEAM).where(TEAM.idTeam.eq(ID))
                .execute();

        Assert.isTrue(rows == 1);
    }

    @Override
    public String getName(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        TeamData data = queryFactory.selectFrom(TEAM)
                .where(TEAM.idTeam.eq(ID)).fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("Team "+ ID 
                + " has not been found in the database");
        else return data.getTeamName();
    }

    @Override
    public void setName(int ID, String name) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(name.length() > 0);
        Assert.notNull(name);

        Team team = getTeam(ID);
        team.setNom(name);
        TeamData data = toData(name);
        SQLUpdateClause update = queryFactory.update(TEAM);

        long rows = update.populate(data).where(TEAM.idTeam.eq(ID))
                .execute();
        closeConnection();

        Assert.isTrue(rows == 1);
    }

    private TeamData toData(String Name){
        Assert.notNull(Name);
        Assert.isTrue(Name.length() > 0);
        
        TeamData data = new TeamData();
        data.setTeamName(Name);
        return data;
    }

    private Team toTeam(TeamData data){
        Team returnValue = new Team();
        returnValue.setId(data.getIdTeam());
        returnValue.setNom(data.getTeamName());
        
        return returnValue;
    }
}
