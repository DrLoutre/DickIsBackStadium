package dao.impl;

import beans.Lap;
import beans.Race;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.Dao;
import dao.LapDao;
import dao.RaceDao;
import exceptions.NotFoundException;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
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
    public int addLap(int temp_hour, int temp_min, int temp_sec, int temp_ms, 
            int id_race) throws NotFoundException {
        Assert.isTrue(temp_hour >= 0);
        Assert.isTrue(temp_hour < 24);
        Assert.isTrue(temp_min >= 0);
        Assert.isTrue(temp_min < 60);
        Assert.isTrue(temp_sec >= 0);
        Assert.isTrue(temp_sec < 60);
        Assert.isTrue(temp_ms >= 0);
        Assert.isTrue(temp_ms < 1000);
        Assert.isTrue(id_race >= 0);
        
        if(!raceDao.raceExists(id_race)) throw new NotFoundException("The race "
                + "with the id " + id_race + "does not exists in the database");
        
        LapData data = toData(temp_hour, temp_min, temp_sec, temp_ms, id_race);
        int ID = queryFactory.insert(LAP).populate(data).executeWithKey(LAP.id);
        closeConnection();
        
        return ID;
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
    public void setTime(int ID, int temp_hour, int temp_min, int temp_sec, 
            int temp_ms) throws NotFoundException{
        Assert.isTrue(ID >= 0);
        Assert.isTrue(temp_hour >= 0);
        Assert.isTrue(temp_hour < 24);
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
    public List<Lap> getLastRace(String athleticNFC) throws NotFoundException {
        Assert.notNull(athleticNFC);
        Assert.isTrue(athleticNFC.length()>0);
        
        List<Race> raceList = raceDao.getRacesList(athleticNFC);
        
        List<LapData> datas = queryFactory.selectFrom(LAP)
                .where(LAP.idScore.eq(raceList.get(raceList.size()-1).getId()))
                .fetch();
        closeConnection();
        
        if(datas == null || datas.isEmpty()) 
            throw new NotFoundException("There's not lap found "
                    + "for the last race of the Athletics " + athleticNFC);
        
        List<Lap> returnValue = new LinkedList();
        for(LapData data : datas){
            returnValue.add(toLap(data));
        }
        
        return returnValue;
    }
    
//    private LapData toData (int ID, int temp_hour, int temp_min, int temp_sec, 
//            int temp_ms, int id_race){
//        Assert.isTrue(ID >= 0);
//        Assert.isTrue(temp_hour >= 0);
//        Assert.isTrue(temp_hour < 24);
//        Assert.isTrue(temp_min >= 0);
//        Assert.isTrue(temp_min < 60);
//        Assert.isTrue(temp_sec >= 0);
//        Assert.isTrue(temp_sec < 60);
//        Assert.isTrue(temp_ms >= 0);
//        Assert.isTrue(temp_ms < 1000);
//        Assert.isTrue(id_race >= 0);
//        
//        LapData data = new LapData();
//        data.setId(ID);
//        Time temp = new Time(0,temp_min,temp_sec);
//        data.setTemp(temp);
//        data.setTempMs(temp_ms);
//        data.setIdScore(id_race);
//        return data;
//    }

    private LapData toData (int temp_hour, int temp_min, int temp_sec, 
            int temp_ms, int id_race){
        Assert.isTrue(temp_hour >= 0);
        Assert.isTrue(temp_hour < 24);
        Assert.isTrue(temp_min >= 0);
        Assert.isTrue(temp_min < 60);
        Assert.isTrue(temp_sec >= 0);
        Assert.isTrue(temp_sec < 60);
        Assert.isTrue(temp_ms >= 0);
        Assert.isTrue(temp_ms < 1000);
        Assert.isTrue(id_race >= 0);
        
        LapData data = new LapData();
        Time temp = new Time(temp_hour,temp_min,temp_sec);
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
        returnValue.setTempHour(data.getTemp().getHours());
        returnValue.setTempMin(data.getTemp().getMinutes());
        returnValue.setTempSec(data.getTemp().getSeconds());
        returnValue.setTempMs(data.getTempMs());
        
        return returnValue;
    }
}
