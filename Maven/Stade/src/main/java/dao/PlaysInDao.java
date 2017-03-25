/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.List;

/**
 *
 * @author Thibaut
 */
public interface PlaysInDao {
    
    boolean isPlaying(String NFC, int idTeam);
    
    void addEntry(String NFC, int idTeam) 
            throws IntegrityException, NotFoundException;
    
    int getTeam(String NFC) throws NotFoundException;
    
    List<String> getPlayers(int idTeam) throws NotFoundException;
}
