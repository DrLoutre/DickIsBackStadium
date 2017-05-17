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
     * @return the refreshment Bean
     * @throws NotFoundException if there is no refreshment with the given ID 
     *      in the database
     */
    Refreshment getRefreshment(int ID) throws NotFoundException;

    /**
     * Get all the refreshment of the database
     * @return A list of refreshment Bean
     * @throws NotFoundException if there is no refreshment in the database
     */
    ArrayList<Refreshment> getAllRefreshment() throws NotFoundException;
    
    /**
     * Check if a refreshment exists in the database
     * @param ID : the id of the refreshment
     * @return true if there is a refreshment with the given ID in the database,
     *      false otherwise
     */
    boolean refreshmentExists(int ID);
    
    /**
     * Get the attendance of a specific Refreshment
     * @param ID : the id of the refreshment
     * @return a float value between 0 and 1
     * @throws NotFoundException if there is no refreshment with the given ID 
     *      in the database
     */
    float getAttendance(int ID) throws NotFoundException;
    
    /**
     * Set the attendance of a specific Refreshment
     * @param ID : the id of the refreshment
     * @param attendance : the now attendance value
     * @throws NotFoundException if there is no refreshment with the given ID 
     *      in the database
     */
    void setAttendance(int ID, float attendance) throws NotFoundException;
    
    /**
     * Get the localisation  of a specific Refreshment
     * @param ID : the id of the refreshment
     * @return the localisation of the refreshment
     * @throws NotFoundException if there is no refreshment with the given ID 
     *      in the database
     */
    String getLocalisation(int ID) throws NotFoundException;
    
}
