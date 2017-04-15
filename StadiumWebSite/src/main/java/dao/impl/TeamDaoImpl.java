package dao.impl;

import beans.Team;
import core.Assert;
import dao.Dao;
import dao.TeamDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import stade.data.QTeam;
import stade.data.TeamData;

public class TeamDaoImpl extends Dao implements TeamDao{

    private static final QTeam TEAM = QTeam.team;
    
    @Override
    public void addTeam(int ID, String Name) throws IntegrityException {
        Assert.isTrue(ID >= 0);
        Assert.notNull(Name);
        Assert.isTrue(Name.length() > 0);
        
        if(teamExists(ID)) throw new IntegrityException("A team already exists "
                + "in the database with the ID : " + ID);
        
        TeamData data = toData(ID,Name);
        long rows = queryFactory.insert(TEAM).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
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
    
    private TeamData toData(int ID, String Name){
        Assert.isTrue(ID >= 0);
        Assert.notNull(Name);
        Assert.isTrue(Name.length() > 0);
        
        TeamData data = new TeamData();
        data.setIdTeam(ID);
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