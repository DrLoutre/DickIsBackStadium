package dao;

import beans.Lap;
import exceptions.NotFoundException;
import java.util.List;

public interface LapDao {
    
    /*
        NotFoundException if the race does not exists
    */
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
     * @param isBeginning
     * @param id_race
     * @return
     * @throws NotFoundException 
     */
    int addLap(int year, int month, int day, int temp_hour, int temp_min, 
            int temp_sec, int temp_ms, boolean isBeginning, int id_race) 
            throws NotFoundException;
    
    Lap getLap(int ID) throws NotFoundException;
    
    boolean lapExists(int ID);
    
    void updateLap(int ID, int year, int hour, int day, int temp_hour, 
            int temp_min, int temp_sec, int temp_ms, boolean isBeginning)
            throws NotFoundException;
    
    List<Lap> getLastRace(String athleticNFC) throws NotFoundException;
    
    List<Lap> getAllLap(int raceId) throws NotFoundException;
}
