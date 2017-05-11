package dao;

import beans.Lap;
import exceptions.NotFoundException;
import java.util.List;

public interface LapDao {
    
    /**
     * Add a lap in the database
     * @param year : the year when the lap has been run
     * @param month : the month when the lap has been run
     * @param day : the day when the lap has been run
     * @param temp_hour : the hour of the begining of the race if isBeginning 
     *      is true, the hours spent during the lap otherwise
     * @param temp_min : the minute of the begining of the race if isBeginning 
     *      is true, the minutes spent during the lap otherwise
     * @param temp_sec : the second of the begining of the race if isBeginning 
     *      is true, the seconds spent during the lap otherwise
     * @param temp_ms : the milisecond of the begining of the race if 
     *      isBeginning is true, the miliseconds spent during the lap otherwise
     * @param isBeginning : true if it's initialisation lap, false otherwise
     * @param id_race : the id of the race of the lap
     * @return the id of the lap added in the database
     * @throws NotFoundException if the aimed race does not exist
     */
    int addLap(int year, int month, int day, int temp_hour, int temp_min, 
            int temp_sec, int temp_ms, boolean isBeginning, int id_race) 
            throws NotFoundException;
    
    /**
     * Get all the lap information from the database
     * @param ID the ID of the searched lap
     * @return the lap bean with all the information of the lap
     * @throws NotFoundException if the aimed lap has not been found in the 
     *      database
     */
    Lap getLap(int ID) throws NotFoundException;
    
    /**
     * Check if a specific map exisit in the dababase
     * @param ID the id of the searched lap
     * @return true if there's a lap with the specific id in the dabaase, false 
     *      otherwise
     */
    boolean lapExists(int ID);
    
    /**
     * Update the informations about a specific lap in the database
     * @param ID the id of the specific lap
     * @param year : the year when the lap has been run
     * @param month : the month when the lap has been run
     * @param day : the day when the lap has been run
     * @param temp_hour : the hour of the begining of the race if isBeginning 
     *      is true, the hours spent during the lap otherwise
     * @param temp_min : the minute of the begining of the race if isBeginning 
     *      is true, the minutes spent during the lap otherwise
     * @param temp_sec : the second of the begining of the race if isBeginning 
     *      is true, the seconds spent during the lap otherwise
     * @param temp_ms : the milisecond of the begining of the race if 
     *      isBeginning is true, the miliseconds spent during the lap otherwise
     * @param isBeginning : true if it's initialisation lap, false otherwise
     * @throws NotFoundException if the aimed race does not exist
     */
    void updateLap(int ID, int year, int month, int day, int temp_hour, 
            int temp_min, int temp_sec, int temp_ms, boolean isBeginning)
            throws NotFoundException;
    
    /**
     * Get all the lap of the last race of a specific athletic
     * @param athleticNFC the NFC (id) of the specific athletic
     * @return all the lap bean of the last race of the specific athletic
     * @throws NotFoundException if there is no athletic with the given ID in 
     *      the database, or if there is no race of this athletic in the 
     *      database, or if there is not any lap for the last race of the 
     *      athletic
     */
    List<Lap> getLastRace(String athleticNFC) throws NotFoundException;
    
    /**
     * Get all the lap of specific race
     * @param raceId the id of the specific race
     * @return all the lap bean of the specific race
     * @throws NotFoundException if there is no race with the given id in the 
     *      database, or if there is no lap of the race in the database
     */
    List<Lap> getAllLap(int raceId) throws NotFoundException;
}
