/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import exceptions.IntegrityException;
import exceptions.NotFoundException;

/**
 *
 * @author Dwade
 */
public interface RefreshmentDao {
    
    void addRefreshment(int ID, float attendance, String localisation) 
            throws IntegrityException;
    
    boolean refreshmentExists(int ID);
    
    float getAttendance(int ID) throws NotFoundException;
    
    void setAttendance(int ID, float attendance) throws NotFoundException;
    
    String getLocalisation(int ID) throws NotFoundException;
    
}
