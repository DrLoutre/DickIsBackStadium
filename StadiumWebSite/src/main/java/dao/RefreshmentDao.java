package dao;

import beans.Refreshment;
import exceptions.NotFoundException;

import java.util.ArrayList;

public interface RefreshmentDao {
    
    /**
     * add a refreshment in the database
     * @param attendance : the attendance of the refreshment
     * @param localisation : the localisation description of the refreshment
     * @return the id of the added refreshment
     */
    int addRefreshment(float attendance, String localisation);
    
    /**
     * get a specific refreshment
     * @param ID : the id of the refreshment
     * @return
     * @throws NotFoundException 
     */
    Refreshment getRefreshment(int ID) throws NotFoundException;

    ArrayList<Refreshment> getAllRefreshment() throws NotFoundException;
    
    boolean refreshmentExists(int ID);
    
    float getAttendance(int ID) throws NotFoundException;
    
    void setAttendance(int ID, float attendance) throws NotFoundException;
    
    String getLocalisation(int ID) throws NotFoundException;
    
}
