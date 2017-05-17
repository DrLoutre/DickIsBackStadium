package dao;

import beans.Seat;
import exceptions.NotFoundException;
import java.util.List;

public interface SeatDao {
    
    /**
     * Check if a specific Seat exists in the database
     * @param ID : the given seat ID
     * @return true if the seat exists in the database, false otherwise
     */
    boolean seatExists(int ID);
    
    /**
     * Add a seat in the database
     * @param tribuneNFC : the tribune id of the seat
     * @param occupied : the occupied state of the seat (true if occupied)
     * @return the id of the added seat
     * @throws NotFoundException if the Tribune does not exist in the database
     */
    int addSeat(int tribuneNFC, boolean occupied) throws NotFoundException;
    
    /**
     * Get the informations of a specific seat
     * @param ID : the ID of the specific seat
     * @return the Seat Bean
     * @throws NotFoundException if the seat does not exist in the database
     */
    Seat getSeat(int ID) throws NotFoundException;
    
    /**
     * Set the state of a specifi Seat
     * @param ID : the ID of the specific seat
     * @param occupied : the new state
     * @throws NotFoundException if the seat does not exist in the database
     */
    void setOccupiedState(int ID, boolean occupied) throws NotFoundException;
    
    /**
     * Get all the seat of a specific Tribune
     * @param tribuneNFC : the Tribune ID
     * @return all the Seat ID of the Tribune
     * @throws NotFoundException if the Tribune does not exist in the database
     *      or if there is no Seat on this Tribune
     */
    List<Integer> getTribuneSeatsID(int tribuneNFC) throws NotFoundException;
    
    /**
     * Get all the seat of a specific Tribune
     * @param tribuneNFC : the Tribune ID
     * @return all the Seat Bean of the Tribune
     * @throws NotFoundException if the Tribune does not exist in the database
     *      or if there is no Seat on this Tribune
     */
    List<Seat> getTribuneSeats(int tribuneNFC) throws NotFoundException;
}
