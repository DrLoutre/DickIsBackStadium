/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import exceptions.IntegrityException;
import exceptions.NotFoundException;
import javafx.util.Pair;

/**
 *
 * @author Thibaut
 */
public interface SpectatorDao {
    
    boolean spectatorExists(int ID);
    
    void addSpetator(int ID, String lastName, String firstName, 
            String tribuneNFC, int IDMatch) 
            throws IntegrityException,NotFoundException;
    
    String getTribune(int ID) throws NotFoundException;
    
    Pair<String,String> getName(int ID) throws NotFoundException;
    
    int getMatch(int ID) throws NotFoundException;
}
