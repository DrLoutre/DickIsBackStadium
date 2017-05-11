package dao;

import beans.Tribune;
import exceptions.IntegrityException;
import exceptions.NotFoundException;

import java.util.ArrayList;

public interface TribuneDao {
    
    boolean tribuneExists(int NFC);
    
    void addTribune(int NFC, int places, String localisation, 
            String texteExplanation) throws IntegrityException;
    
    Tribune getTribune(int NFC) throws NotFoundException;

    ArrayList<Tribune> getAllTribune() throws NotFoundException;
    
    int getPlaces(int NFC) throws NotFoundException;
    
    void setPlaces(int NFC, int places) throws NotFoundException;
    
    String getLocalisation(int NFC) throws NotFoundException;
    
    String getExplanation(int NFC) throws NotFoundException;
    
}
