/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import beans.Refreshment;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.Dao;
import dao.RefreshmentDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import stade.data.RefreshmentData;
import stade.data.QRefreshment;

/**
 *
 * @author Dwade
 */
public class RefreshmentDaoImpl extends Dao implements RefreshmentDao{
    
    private static final QRefreshment REFRESHMENT = QRefreshment.refreshment;

    @Override
    public void addRefreshment(int ID, float attendance, String localisation) 
            throws IntegrityException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(attendance >= 0 && attendance <= 100);
        Assert.notNull(localisation);
        Assert.isTrue(localisation.length() > 0);
        
        if (refreshmentExists(ID)) throw new IntegrityException("A refreshement"
                + " already exists in the database with the ID : " + ID);
        
        RefreshmentData data = toData(ID, attendance, localisation);
        long rows = queryFactory.insert(REFRESHMENT).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public Refreshment getRefreshment(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        RefreshmentData data = queryFactory.selectFrom(REFRESHMENT)
                .where(REFRESHMENT.id.eq(ID)).fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("Refreshement " 
                + ID + " has not been found in the database");
        
        return toRefreshment(data);
    }

    @Override
    public boolean refreshmentExists(int ID) {
        Assert.isTrue(ID >= 0);
        
        RefreshmentData data = queryFactory.selectFrom(REFRESHMENT)
                .where(REFRESHMENT.id.eq(ID)).fetchFirst();
        closeConnection();
        
        return (data != null);
    }

    @Override
    public float getAttendance(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        if (!refreshmentExists(ID)) throw new NotFoundException("Refreshement " 
                + ID + " has not been found in the database");
        
        RefreshmentData data = queryFactory.selectFrom(REFRESHMENT)
                .where(REFRESHMENT.id.eq(ID)).fetchFirst();
        closeConnection();
        
        return data.getAttendance();
    }

    @Override
    public void setAttendance(int ID, float attendance) 
            throws NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.isTrue(attendance >= 0 && attendance <= 100);
        
        if (!refreshmentExists(ID)) throw new NotFoundException("Refreshement " 
                + ID + " has not been found in the database");
        
        SQLUpdateClause update = queryFactory.update(REFRESHMENT);
        
        long rows = update.set(REFRESHMENT.attendance, attendance)
                .where(REFRESHMENT.id.eq(ID))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public String getLocalisation(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        if (!refreshmentExists(ID)) throw new NotFoundException("Refreshement " 
                + ID + " has not been found in the database");
        
        RefreshmentData data = queryFactory.selectFrom(REFRESHMENT)
                .where(REFRESHMENT.id.eq(ID)).fetchFirst();
        closeConnection();
        
        return data.getLocalisation();
    }
    
    private RefreshmentData toData(int ID, float attendance, 
            String localisation){
        Assert.isTrue(ID >= 0);
        Assert.isTrue(attendance >= 0 && attendance <= 100);
        Assert.notNull(localisation);
        Assert.isTrue(localisation.length() > 0);
        
        RefreshmentData data = new RefreshmentData();
        data.setId(ID);
        data.setAttendance(attendance);
        data.setLocalisation(localisation);
        return data;
    }
    
    private Refreshment toRefreshment(RefreshmentData data){
        Refreshment returnValue = new Refreshment();
        returnValue.setAttendance(data.getAttendance());
        returnValue.setId(data.getId());
        returnValue.setLocalisation(data.getLocalisation());
        
        return returnValue;
    }
}
