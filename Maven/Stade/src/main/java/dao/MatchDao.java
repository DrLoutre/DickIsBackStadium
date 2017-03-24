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
 * @author Dwade
 */
public interface MatchDao {
    
    void addMatch(int ID, int idTeam1, int idTeam2) 
            throws IntegrityException, NotFoundException;
    
    boolean matchExists(int ID);
    
    Pair<Integer,Integer> getGoals(int ID) throws NotFoundException;
}
