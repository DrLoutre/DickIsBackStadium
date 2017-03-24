/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.Dao;
import dao.RaceDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import stade.data.RaceData;
import stade.data.QRace;

/**
 *
 * @author Dwade
 */
public class RaceDaoImpl extends Dao implements RaceDao{

    private static final QRace RACE = QRace.race;
    
    @Override
    public void addRace(int ID, String NFC) throws IntegrityException {
        Assert.isTrue(ID >= 0);
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if(raceExists(ID)) throw new IntegrityException("A race already exists "
                + "in the database with the ID : " + ID);
        
        RaceData data = toData(ID,NFC);
        long rows = queryFactory.insert(RACE).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
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
    public void setNFC(int ID, String NFC) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if (!raceExists(ID)) throw new NotFoundException("Race "+ ID 
                + " has not been found in the database");
        
        SQLUpdateClause update = queryFactory.update(RACE);
        
        long rows = update.set(RACE.nfc, NFC)
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
}
