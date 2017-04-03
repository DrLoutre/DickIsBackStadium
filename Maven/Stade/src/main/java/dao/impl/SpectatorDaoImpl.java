/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import beans.Spectator;
import core.Assert;
import dao.Dao;
import dao.MatchDao;
import dao.SpectatorDao;
import dao.TribuneDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import javafx.util.Pair;
import stade.data.QSpectator;
import stade.data.SpectatorData;

/**
 *
 * @author Thibaut
 */
public class SpectatorDaoImpl extends Dao implements SpectatorDao{

    private final TribuneDao tribuneDao;
    private final MatchDao matchDao;
    
    private static final QSpectator SPECTATOR = QSpectator.spectator;
    
    public SpectatorDaoImpl(){
        super();
        tribuneDao = new TribuneDaoImpl();
        matchDao = new MatchDaoImpl();
    }
    
    @Override
    public boolean spectatorExists(int ID) {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        return(data != null);
    }

    @Override
    public void addSpetator(int ID, String lastName, String firstName, 
            String tribuneNFC, int IDMatch) 
            throws IntegrityException, NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(tribuneNFC);
        Assert.isTrue(tribuneNFC.length() > 0);
        Assert.isTrue(IDMatch >= 0);
        
        if(spectatorExists(ID)) throw new IntegrityException("A spectator " 
                + ID + " already exists in the database");
        if(!tribuneDao.tribuneExists(tribuneNFC)) throw 
                new IntegrityException("The tribune " + tribuneNFC 
                        + " does not exists in the database");
        if(!matchDao.matchExists(IDMatch)) throw new IntegrityException("The "
                + "match " + IDMatch + " does not exists in the database");
        
        SpectatorData data = toData(ID, lastName, firstName, tribuneNFC, 
                IDMatch);
        long rows = queryFactory.insert(SPECTATOR).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public Spectator getSpectator(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        if(data==null) throw new NotFoundException("The spectator " 
                + ID + " does not exists in the database");
        
        return toSpectator(data);
    }

    @Override
    public String getTribune(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The spectator " 
                + ID + " does not exists in the database");
        
        return data.getTribuneNFC();
    }

    @Override
    public Pair<String, String> getName(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The spectator " 
                + ID + " does not exists in the database");
        
        return new Pair(data.getFirstName(),data.getLastName());
    }

    @Override
    public int getMatch(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The spectator " 
                + ID + " does not exists in the database");
        
        return data.getIdMatch();
    }
    
    SpectatorData toData(int ID, String lastName, String firstName, 
            String tribuneNFC, int IDMatch){
        Assert.isTrue(ID >= 0);
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(tribuneNFC);
        Assert.isTrue(tribuneNFC.length() > 0);
        Assert.isTrue(IDMatch >= 0);
        
        SpectatorData data = new SpectatorData();
        data.setFirstName(firstName);
        data.setIdMatch(IDMatch);
        data.setIdSpec(ID);
        data.setLastName(lastName);
        data.setTribuneNFC(tribuneNFC);
        return data;
    }
    
    private Spectator toSpectator(SpectatorData data){
        Spectator returnValue = new Spectator();
        returnValue.setFirstName(data.getFirstName());
        returnValue.setID(data.getIdSpec());
        returnValue.setIDMatch(data.getIdMatch());
        returnValue.setLastName(data.getLastName());
        returnValue.setTribuneNFC(data.getTribuneNFC());
        
        return returnValue;
    }
}
