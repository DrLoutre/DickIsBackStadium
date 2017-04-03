/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Athletic;
import exceptions.IntegrityException;
import exceptions.NotFoundException;

/**
 *
 * @author Dwade
 */
public interface AthleticDao {
    
    void addAthletic(String NFC, String firstName, String lastName, int age, 
            String sex, String password) throws IntegrityException;
    
    Athletic getAthletic(String NFC) throws NotFoundException;
    
    boolean athleticExists(String NFC);
    
    String getFirstName(String NFC) throws NotFoundException;
    
    String getLastName(String NFC) throws NotFoundException;
    
    String getPassword(String NFC) throws NotFoundException;
}
