package dao;

import beans.Race;
import exceptions.IntegrityException;
import exceptions.NotFoundException;

public interface RaceDao {
    
    void addRace(int ID, String athleticNFC) 
            throws IntegrityException,NotFoundException;
    
    Race getRace(int ID) throws NotFoundException;
    
    boolean raceExists(int ID);
    
    void setAthleticNFC(int ID, String athleticNFC) throws NotFoundException;
}
