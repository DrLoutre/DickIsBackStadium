/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Tribune;
import exceptions.IntegrityException;
import exceptions.NotFoundException;

/**
 *
 * @author Thibaut
 */
public interface TribuneDao {
    
    boolean tribuneExists(String NFC);
    
    void addTribune(String NFC, int places, String localisation, 
            String texteExplanation) throws IntegrityException;
    
    Tribune getTribune(String NFC) throws NotFoundException;
    
    int getPlaces(String NFC) throws NotFoundException;
    
    void setPlaces(String NFC, int places) throws NotFoundException;
    
    String getLocalisation(String NFC) throws NotFoundException;
    
    String getExplanation(String NFC) throws NotFoundException;
    
}
