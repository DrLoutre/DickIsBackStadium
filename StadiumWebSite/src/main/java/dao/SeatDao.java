/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Seat;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.List;

/**
 *
 * @author Thibaut
 */
public interface SeatDao {
    
    boolean seatExists(int ID);
    
    void addSeat(int ID, String tribuneNFC, boolean occupied) 
            throws IntegrityException, NotFoundException;
    
    Seat getSeat(int ID) throws NotFoundException;
    
    void setOccupiedState(int ID, boolean occupied) throws NotFoundException;
    
    List<Integer> getTribuneSeatsID(String tribuneNFC) throws NotFoundException;
    
    List<Seat> getTribuneSeats(String tribuneNFC) throws NotFoundException;
}
