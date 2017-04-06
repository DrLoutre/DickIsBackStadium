/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Race;
import exceptions.IntegrityException;
import exceptions.NotFoundException;

/**
 *
 * @author Dwade
 */
public interface RaceDao {
    
    void addRace(int ID, String athleticNFC) 
            throws IntegrityException,NotFoundException;
    
    Race getRace(int ID) throws NotFoundException;
    
    boolean raceExists(int ID);
    
    void setAthleticNFC(int ID, String athleticNFC) throws NotFoundException;
}
