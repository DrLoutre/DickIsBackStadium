package dao.impl;

import beans.Tribune;
import core.Assert;
import dao.Dao;
import dao.TribuneDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import stade.data.QTribune;
import stade.data.TribuneData;

import java.util.ArrayList;
import java.util.List;

public class TribuneDaoImpl extends Dao implements TribuneDao{

    private static final QTribune TRIBUNE = QTribune.tribune;
    
    @Override
    public boolean tribuneExists(int NFC) {
        Assert.isTrue(NFC >= 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        return (data != null);
    }

    @Override
    public void addTribune(int NFC, int places, String localisation, 
            String texteExplanation) throws IntegrityException {
        Assert.isTrue(NFC >= 0);
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
    public Tribune getTribune(int NFC) throws NotFoundException {
        Assert.isTrue(NFC >= 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        return toTribune(data);
    }

    @Override
    public ArrayList<Tribune> getAllTribune() throws NotFoundException {
        List<TribuneData> datas = queryFactory.select(TRIBUNE).from(TRIBUNE)
                .fetch();
        closeConnection();

        if (datas.isEmpty()) throw new NotFoundException("Tribunes has not "
                + "been found in the database");

        ArrayList<Tribune> tribunes = new ArrayList<>();
        for (TribuneData tribune : datas) {
            tribunes.add(toTribune(tribune));
        }

        return tribunes;
    }

    @Override
    public int getPlaces(int NFC) throws NotFoundException {
        Assert.isTrue(NFC >= 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        return data.getPlaces();
    }

    @Override
    public void setPlaces(int NFC, int places) throws NotFoundException {
        Assert.isTrue(NFC >= 0);
        Assert.isTrue(places >= 0);
        
        if(!tribuneExists(NFC)) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        Tribune tribune = getTribune(NFC);
        TribuneData data = toData(NFC, places, tribune.getLocalisation(), 
                tribune.getDescription());
        
        long rows = queryFactory.update(TRIBUNE).where(TRIBUNE.nfc.eq(NFC))
                .populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public String getLocalisation(int NFC) throws NotFoundException {
        Assert.isTrue(NFC >= 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        return data.getLocalisation();
    }

    @Override
    public String getExplanation(int NFC) throws NotFoundException {
        Assert.isTrue(NFC >= 0);
        
        TribuneData data = queryFactory.selectFrom(TRIBUNE)
                .where(TRIBUNE.nfc.eq(NFC))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The tribune " 
                + NFC + " does not exists in the database");
        
        return data.getTexteExplanation();
    }
    
    TribuneData toData(int NFC, int places, String localisation, 
            String texteExplanation){
        Assert.isTrue(NFC >= 0);
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
