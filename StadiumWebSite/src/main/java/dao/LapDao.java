package dao;

import beans.Lap;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.sql.Time;
import javafx.util.Pair;

public interface LapDao {
    
    /*
        NotFoundException if the race does not exists
    */
    void addLap(int ID, int temp_min, int temp_sec, int temp_ms, int id_race) 
            throws IntegrityException, NotFoundException;
    
    Lap getLap(int ID) throws NotFoundException;
    
    boolean lapExists(int ID);
    
    void setTime(int ID, int temp_min, int temp_sec, int temp_ms) 
            throws NotFoundException;
    
    Pair<Time,Integer> getTime(int ID) throws NotFoundException;
}
