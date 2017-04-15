package dao;

import beans.Seat;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.List;

public interface SeatDao {
    
    boolean seatExists(int ID);
    
    void addSeat(int ID, String tribuneNFC, boolean occupied) 
            throws IntegrityException, NotFoundException;
    
    Seat getSeat(int ID) throws NotFoundException;
    
    void setOccupiedState(int ID, boolean occupied) throws NotFoundException;
    
    List<Integer> getTribuneSeatsID(String tribuneNFC) throws NotFoundException;
    
    List<Seat> getTribuneSeats(String tribuneNFC) throws NotFoundException;
}
