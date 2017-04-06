/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import beans.Tribune;
import core.Assert;
import dao.Dao;
import dao.TribuneDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import stade.data.QTribune;
import stade.data.TribuneData;

/**
 *
 * @author Thibaut
 */
public class TribuneDaoImpl extends Dao implements TribuneDao{

    private static final QTribune TRIBUNE = QTribune.tribune;
    
    @Override
    public boolean tribuneExists(String NFC) {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        return (data != null);
    }

    @Override
    public void addTribune(String NFC, int places, String localisation, 
            String texteExplanation) throws IntegrityException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.isTrue(places >= 0);
        Assert.notNull(localisation);
        Assert.isTrue(localisation.length() > 0);
        Assert.notNull(texteExplanation);
        Assert.isTrue(texteExplanation.length() > 0);
        
        if(tribuneExists(NFC)) throw new IntegrityException("A tribune " + NFC 
                + " already exists in the database");
        
        TribuneData data = toData(NFC, places, localisation, texteExplanation);
        long rows = queryFactory.insert(TRIBUNE).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public Tribune getTribune(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        return toTribune(data);
    }

    @Override
    public int getPlaces(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        return data.getPlaces();
    }

    @Override
    public void setPlaces(String NFC, int places) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.isTrue(places >= 0);
        
        if(!tribuneExists(NFC)) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        long rows = queryFactory.update(TRIBUNE).where(TRIBUNE.nfc.eq(NFC))
                .set(TRIBUNE.places, places).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public String getLocalisation(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        return data.getLocalisation();
    }

    @Override
    public String getExplanation(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        return data.getTexteExplanation();
    }
    
    TribuneData toData(String NFC, int places, String localisation, 
            String texteExplanation){
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.isTrue(places >= 0);
        Assert.notNull(localisation);
        Assert.isTrue(localisation.length() > 0);
        Assert.notNull(texteExplanation);
        Assert.isTrue(texteExplanation.length() > 0);
        
        TribuneData data = new TribuneData();
        data.setLocalisation(localisation);
        data.setNfc(NFC);
        data.setPlaces(places);
        data.setTexteExplanation(texteExplanation);
        return data;
    }

    private Tribune toTribune(TribuneData data){
        Tribune returnValue = new Tribune();
        returnValue.setDescription(data.getTexteExplanation());
        returnValue.setLocalisation(data.getLocalisation());
        returnValue.setNFC(data.getNfc());
        returnValue.setPlaces(data.getPlaces());
        
        return returnValue;
    }
}
