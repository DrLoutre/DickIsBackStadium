package dao;

import beans.Race;
import exceptions.NotFoundException;
import java.util.List;

public interface RaceDao {
    
    /**
     * Add a race to the database for an athletic
     * @param athleticNFC : the athletic NFC (id)
     * @return the id of the added race
     * @throws NotFoundException if there is no athletic with this NFC in the 
     *      database
     */
    int addRace(String athleticNFC) throws NotFoundException;
    
    /**
     * Get a race
     * @param ID : the id of the race
     * @return the race bean
     * @throws NotFoundException there is not race with the given id in the 
     * database
     */
    Race getRace(int ID) throws NotFoundException;
    
    /**
     * Check if a race exists
     * @param ID : the race ID
     * @return true if the race exists, false otherwise
     */
    boolean raceExists(int ID);
    
    /**
     * Set the athletic NFC of a race
     * @param ID the race ID
     * @param athleticNFC the new athletic NFC
     * @throws NotFoundException if there is no race or no athletic in the 
     * database
     */
    void setAthleticNFC(int ID, String athleticNFC) throws NotFoundException;
    
    /**
     * Get all the race of an athletic
     * @param athleticNFC : the athletic NFC (id)
     * @return all the race of the athletic
     * @throws NotFoundException if there is no race of the athletic in the 
     *      database
     */
    List<Race> getRacesList(String athleticNFC) throws NotFoundException;
}
