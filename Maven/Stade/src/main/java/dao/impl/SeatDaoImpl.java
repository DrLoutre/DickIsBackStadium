/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import core.Assert;
import dao.Dao;
import dao.SeatDao;
import dao.TribuneDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.LinkedList;
import java.util.List;
import stade.data.QSeat;
import stade.data.SeatData;

/**
 *
 * @author Thibaut
 */
public class SeatDaoImpl extends Dao implements SeatDao{

    private final TribuneDao tribuneDao;
    
    private static final QSeat SEAT = QSeat.seat;
    
    public SeatDaoImpl(){
        super();
        tribuneDao = new TribuneDaoImpl();
    }
    
    @Override
    public boolean seatExists(int ID) {
        Assert.isTrue(ID >= 0);
        
        SeatData data = queryFactory.selectFrom(SEAT)
                .where(SEAT.id.eq(ID))
                .fetchFirst();
        closeConnection();
        
        return(data != null);
    }

    @Override
    public void addSeat(int ID, String tribuneNFC, boolean occupied) 
            throws IntegrityException, NotFoundException {
        Assert.isTrue(ID >= 0);
        Assert.notNull(tribuneNFC);
        Assert.isTrue(tribuneNFC.length() > 0);
        
        if(seatExists(ID)) throw new IntegrityException("A seat " + ID 
                + " already exists in the database");
        if(!tribuneDao.tribuneExists(tribuneNFC)) 
            throw new NotFoundException("The tribune " + tribuneNFC 
                    + " does not exists in the database");
        
        SeatData data = toData(ID,tribuneNFC,occupied);
        long rows = queryFactory.insert(SEAT).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setOccupiedState(int ID, boolean occupied) 
            throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        if(!seatExists(ID)) throw new NotFoundException("The seat " + ID 
                + " does not exist in the database");
        
        long rows = queryFactory.update(SEAT).where(SEAT.id.eq(ID))
                .set(SEAT.occupied, occupied).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public List<Integer> getTribuneSeats(String tribuneNFC) 
            throws NotFoundException {
        Assert.notNull(tribuneNFC);
        Assert.isTrue(tribuneNFC.length() > 0);
        
        if(!tribuneDao.tribuneExists(tribuneNFC)) 
            throw new NotFoundException("The tribune " + tribuneNFC 
                    + " does not exists in the database");
        
        List<SeatData> datas = queryFactory.selectFrom(SEAT)
                .where(SEAT.tribuneNFC.eq(tribuneNFC)).fetch();
        closeConnection();
        
        if(datas == null || datas.isEmpty()) throw new NotFoundException("There"
                + " is no seat referenced with this tribune in the database");
        else {
            List<Integer> IDList = new LinkedList();
            for(SeatData data : datas){
                IDList.add(data.getId());
            }
            return IDList;
        }
    }
    
    SeatData toData(int ID, String tribuneNFC, boolean occupied){
        Assert.isTrue(ID >= 0);
        Assert.notNull(tribuneNFC);
        Assert.isTrue(tribuneNFC.length() > 0);
        
        SeatData data = new SeatData();
        data.setId(ID);
        data.setOccupied(occupied);
        data.setTribuneNFC(tribuneNFC);
        return data;
    }
}
