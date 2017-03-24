/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import core.Assert;
import dao.AthleticDao;
import dao.Dao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import stade.data.AthleticData;
import stade.data.QAthletic;

/**
 *
 * @author Dwade
 */
public class AthleticDaoImpl extends Dao implements AthleticDao {
    
    private static final QAthletic ATHLETIC = QAthletic.athletic;

    @Override
    public void addAthletic(String NFC, String firstName, String lastName, 
            int age, String sex, String password) throws IntegrityException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.isTrue(age >= 0);
        Assert.notNull(password);
        Assert.isTrue(password.length() > 0);
        Assert.notNull(sex);
        Assert.isTrue(sex.length() == 1);
        
        if (athleticExists(NFC)) throw new IntegrityException("An athletic "
                + "already exists in the database with the NFC id : " + NFC);
        
        AthleticData data = toData(NFC, firstName, lastName, age, sex, password);
        long rows = queryFactory.insert(ATHLETIC).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public boolean athleticExists(String NFC) {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        return (data != null);
    }

    @Override
    public String getFirstName(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if(!athleticExists(NFC)) throw new NotFoundException("Athletic " + NFC 
                + " has not been found in the database");
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        return data.getFirstName();
    }

    @Override
    public String getLastName(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if(!athleticExists(NFC)) throw new NotFoundException("Athletic " + NFC 
                + " has not been found in the database");
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        return data.getLastName();
    }

    @Override
    public String getPassword(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if(!athleticExists(NFC)) throw new NotFoundException("Athletic " + NFC 
                + " has not been found in the database");
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        return data.getPassword();
    }
    
    private AthleticData toData(String NFC, String firstName, String lastName, 
            int age, String sex, String password){
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.isTrue(age >= 0);
        Assert.notNull(password);
        Assert.isTrue(password.length() > 0);
        Assert.notNull(sex);
        Assert.isTrue(sex.length() == 1);
        
        AthleticData data = new AthleticData();
        data.setNfc(NFC);
        data.setFirstName(firstName);
        data.setLastName(lastName);
        data.setAge(age);
        data.setPassword(password);
        data.setSex(sex);
        
        return data;
    }
}
