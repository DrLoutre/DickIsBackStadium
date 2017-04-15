package dao.impl;

import beans.Lap;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.Dao;
import dao.LapDao;
import dao.RaceDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.sql.Time;
import javafx.util.Pair;
import stade.data.LapData;
import stade.data.QLap;

public class LapDaoImpl extends Dao implements LapDao{
    
    private final RaceDao raceDao;
    
    private static final QLap LAP = QLap.lap;
    
    public LapDaoImpl() {
        super();
        raceDao = new RaceDaoImpl();
    }

    @Override
    public void addLap(int ID, int temp_min, int temp_sec, int temp_ms, 
            int id_race) throws IntegrityException, NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(temp_min >= 0);
        Assert.isTrue(temp_min < 60);
        Assert.isTrue(temp_sec >= 0);
        Assert.isTrue(temp_sec < 60);
        Assert.isTrue(temp_ms >= 0);
        Assert.isTrue(temp_ms < 1000);
        Assert.isTrue(id_race >= 0);
        
        if(lapExists(ID)) throw new IntegrityException("A lap already exists "
                + "in the database with the ID : " + ID);
        
        if(!raceDao.raceExists(id_race)) throw new NotFoundException("The race "
                + "with the id " + id_race + "does not exists in the database");
        
        LapData data = toData(ID, temp_min, temp_sec, temp_ms, id_race);
        long rows = queryFactory.insert(LAP).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public Lap getLap(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        LapData data = queryFactory.selectFrom(LAP)
                .where(LAP.id.eq(ID)).fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("Lap "+ ID 
                + " has not been found in the database");
        
        return toLap(data);
    }

    @Override
    public boolean lapExists(int ID) {
        Assert.isTrue(ID >= 0);
        
        LapData data = queryFactory.selectFrom(LAP)
                .where(LAP.id.eq(ID)).fetchFirst();
        closeConnection();
        
        return (data != null);
    }

    @Override
    public void setTime(int ID, int temp_min, int temp_sec, int temp_ms) 
            throws NotFoundException{
        Assert.isTrue(ID >= 0);
        Assert.isTrue(temp_min >= 0);
        Assert.isTrue(temp_min < 60);
        Assert.isTrue(temp_sec >= 0);
        Assert.isTrue(temp_sec < 60);
        Assert.isTrue(temp_ms >= 0);
        Assert.isTrue(temp_ms < 1000);
        
        if(!lapExists(ID)) throw new NotFoundException("Lap "+ ID 
                + " has not been found in the database");
        
        LapData data = toData(ID, temp_min, temp_sec, temp_ms,getIdRace(ID));
        SQLUpdateClause update = queryFactory.update(LAP);
        
        long rows = update.set(LAP, data)
                .where(LAP.id.eq(ID)).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public Pair<Time,Integer> getTime(int ID) throws NotFoundException{
        Assert.isTrue(ID >= 0);
        
        if(!lapExists(ID)) throw new NotFoundException("Lap "+ ID 
                + " has not been found in the database");
        
        LapData data = queryFactory.selectFrom(LAP)
                .where(LAP.id.eq(ID)).fetchFirst();
        closeConnection();
        
        return new Pair(data.getTemp(),data.getTempMs());
    }
    
    private LapData toData (int ID, int temp_min, int temp_sec, int temp_ms, 
            int id_race){
        Assert.isTrue(ID >= 0);
        Assert.isTrue(temp_min >= 0);
        Assert.isTrue(temp_min < 60);
        Assert.isTrue(temp_sec >= 0);
        Assert.isTrue(temp_sec < 60);
        Assert.isTrue(temp_ms >= 0);
        Assert.isTrue(temp_ms < 1000);
        Assert.isTrue(id_race >= 0);
        
        LapData data = new LapData();
        data.setId(ID);
        Time temp = new Time(0,temp_min,temp_sec);
        data.setTemp(temp);
        data.setTempMs(temp_ms);
        data.setIdScore(id_race);
        return data;
    }
    
    private int getIdRace(int ID){
        Assert.isTrue(lapExists(ID));
        
        LapData data = queryFactory.selectFrom(LAP)
                .where(LAP.id.eq(ID)).fetchFirst();
        closeConnection();
        
        return data.getIdScore();
    }
    
    private Lap toLap(LapData data){
        Lap returnValue = new Lap();
        returnValue.setID(data.getId());
        returnValue.setIdRace(data.getIdScore());
        returnValue.setTemp(data.getTemp(), data.getTempMs());
        
        return returnValue;
    }
}
