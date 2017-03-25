/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.sql.Time;
import javafx.util.Pair;

/**
 *
 * @author Dwade
 */
public interface LapDao {
    
    /*
        NotFoundException if the race does not exists
    */
    void addLap(int ID, int temp_min, int temp_sec, int temp_ms, int id_race) 
            throws IntegrityException, NotFoundException;
    
    boolean lapExists(int ID);
    
    void setTime(int ID, int temp_min, int temp_sec, int temp_ms) 
            throws NotFoundException;
    
    Pair<Time,Integer> getTime(int ID) throws NotFoundException;
}
