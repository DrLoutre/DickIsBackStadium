package dao;

import beans.Lap;
import exceptions.NotFoundException;
import java.sql.Time;
import java.util.List;
import javafx.util.Pair;

public interface LapDao {
    
    /*
        NotFoundException if the race does not exists
    */
    int addLap(int temp_hour, int temp_min, int temp_sec, int temp_ms, 
            int id_race) throws NotFoundException;
    
    Lap getLap(int ID) throws NotFoundException;
    
    boolean lapExists(int ID);
    
    void setTime(int ID, int temp_hour, int temp_min, int temp_sec, int temp_ms) 
            throws NotFoundException;
    
//    Pair<Time,Integer> getTime(int ID) throws NotFoundException;
    
    List<Lap> getLastRace(String athleticNFC) throws NotFoundException;
}
