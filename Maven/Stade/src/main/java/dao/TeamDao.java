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
public interface TeamDao {
    
    void addTeam(int ID, String Name) throws IntegrityException;
    
    boolean teamExists(int ID);
    
    String getName(int ID) throws NotFoundException;
}
