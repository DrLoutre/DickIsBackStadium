package dao;

import beans.Refreshment;
import exceptions.IntegrityException;
import exceptions.NotFoundException;

import java.util.ArrayList;

public interface RefreshmentDao {
    
    void addRefreshment(int ID, float attendance, String localisation) 
            throws IntegrityException;
    
    Refreshment getRefreshment(int ID) throws NotFoundException;

    ArrayList<Refreshment> getAllRefreshment() throws NotFoundException;
    
    boolean refreshmentExists(int ID);
    
    float getAttendance(int ID) throws NotFoundException;
    
    void setAttendance(int ID, float attendance) throws NotFoundException;
    
    String getLocalisation(int ID) throws NotFoundException;
    
}
