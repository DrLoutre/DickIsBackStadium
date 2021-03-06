package dao.impl;

import beans.Refreshment;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import dao.Dao;
import dao.RefreshmentDao;
import exceptions.NotFoundException;
import stade.data.RefreshmentData;
import stade.data.QRefreshment;

import java.util.ArrayList;
import java.util.List;

public class RefreshmentDaoImpl extends Dao implements RefreshmentDao{
    
    private static final QRefreshment REFRESHMENT = QRefreshment.refreshment;

    @Override
    public int addRefreshment(float attendance, String localisation){
        Assert.isTrue(attendance >= 0 && attendance <= 1);
        Assert.notNull(localisation);
        Assert.isTrue(localisation.length() > 0);
        
        RefreshmentData data = toData(attendance, localisation);
        int ID = queryFactory.insert(REFRESHMENT).populate(data)
                .executeWithKey(REFRESHMENT.id);
        closeConnection();
        
        return ID;
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
    public ArrayList<Refreshment> getAllRefreshment() throws NotFoundException {
        List<RefreshmentData> data = queryFactory.select(REFRESHMENT)
                .from(REFRESHMENT).fetch();
        closeConnection();

        if (data.isEmpty()) throw new NotFoundException("Refreshments"
                + " has not been found in the database");

        ArrayList<Refreshment> refreshments = new ArrayList<>();
        for (RefreshmentData data1 : data) {
            refreshments.add(toRefreshment(data1));
        }

        return refreshments;
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
        Assert.isTrue(attendance >= 0 && attendance <= 1);
        
        if (!refreshmentExists(ID)) throw new NotFoundException("Refreshement " 
                + ID + " has not been found in the database");
        
        SQLUpdateClause update = queryFactory.update(REFRESHMENT);

        RefreshmentData data = toData(attendance, getRefreshment(ID).getLocalisation());

        long rows = update.populate(data)
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
    
    private RefreshmentData toData(float attendance, 
            String localisation){
        Assert.isTrue(attendance >= 0 && attendance <= 1);
        Assert.notNull(localisation);
        Assert.isTrue(localisation.length() > 0);
        
        RefreshmentData data = new RefreshmentData();
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
