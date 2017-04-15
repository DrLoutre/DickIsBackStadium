package dao.impl;

import beans.Race;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.AthleticDao;
import dao.Dao;
import dao.RaceDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import stade.data.RaceData;
import stade.data.QRace;

public class RaceDaoImpl extends Dao implements RaceDao{
    
    private final AthleticDao athleticDao;
    
    private static final QRace RACE = QRace.race;
    
    public RaceDaoImpl(){
        super();
        athleticDao = new AthleticDaoImpl();
    }
    
    @Override
    public void addRace(int ID, String athleticNFC) 
            throws IntegrityException, NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.notNull(athleticNFC);
        Assert.isTrue(athleticNFC.length() > 0);
        
        if(raceExists(ID)) throw new IntegrityException("A race already exists "
                + "in the database with the ID : " + ID);
        
        if(!athleticDao.athleticExists(athleticNFC)) throw 
                new NotFoundException("Athletic "+ athleticNFC 
                + " has not been found in the database");
        
        RaceData data = toData(ID,athleticNFC);
        long rows = queryFactory.insert(RACE).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public Race getRace(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        RaceData data = queryFactory.selectFrom(RACE)
                .where(RACE.idScore.eq(ID)).fetchFirst();
        closeConnection();
        
        if (data == null) throw new NotFoundException("Race "+ ID 
                + " has not been found in the database");
        
        return toRace(data);
    }

    @Override
    public boolean raceExists(int ID) {
        Assert.isTrue(ID >= 0);
        
        RaceData data = queryFactory.selectFrom(RACE)
                .where(RACE.idScore.eq(ID)).fetchFirst();
        closeConnection();
        
        return (data != null);
    }

    @Override
    public void setAthleticNFC(int ID, String athleticNFC) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.notNull(athleticNFC);
        Assert.isTrue(athleticNFC.length() > 0);
        
        if (!raceExists(ID)) throw new NotFoundException("Race "+ ID 
                + " has not been found in the database");
        
        if(!athleticDao.athleticExists(athleticNFC)) throw 
                new NotFoundException("Athletic "+ athleticNFC 
                + " has not been found in the database");
        
        SQLUpdateClause update = queryFactory.update(RACE);
        
        long rows = update.set(RACE.nfc, athleticNFC)
                .where(RACE.idScore.eq(ID)).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    private RaceData toData(int ID, String NFC){
        Assert.isTrue(ID >= 0);
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        RaceData data = new RaceData();
        data.setIdScore(ID);
        data.setNfc(NFC);
        return data;
    }
    
    private Race toRace(RaceData data){
        Race returnValue = new Race();
        returnValue.setId(data.getIdScore());
        returnValue.setNFC(data.getNfc());
        
        return returnValue;
    }
}
