package dao;

import beans.Tribune;
import exceptions.IntegrityException;
import exceptions.NotFoundException;

import java.util.ArrayList;

public interface TribuneDao {
    
    /**
     * Check if a specific Tribune exists uin the database
     * @param NFC : the specific Tribune NFC (ID)
     * @return True if the Tribune exists, false otherwise
     */
    boolean tribuneExists(int NFC);
    
    /**
     * Add a Tribune to the database
     * @param NFC : The Tribune NFC (ID)
     * @param places : The Tribune places number
     * @param localisation : The Tribune localication
     * @param texteExplanation : The Tribune explanation
     * @throws IntegrityException if there is already a Tribune in the database 
     *      with the given ID
     */
    void addTribune(int NFC, int places, String localisation, 
            String texteExplanation) throws IntegrityException;
    
    /**
     * Get the Bean of a specific Tribune from the database
     * @param NFC : the specific Tribune NFC (ID)
     * @return The Tribune Bean
     * @throws NotFoundException if the Tribune does not exists in the database
     */
    Tribune getTribune(int NFC) throws NotFoundException;

    /**
     * Get all the Tribune from the database
     * @return All the Tribune Bean
     * @throws NotFoundException if there is not any Tribune in the databse 
     */
    ArrayList<Tribune> getAllTribune() throws NotFoundException;
    
    /**
     * Get the places number of a specific Tribune
     * @param NFC : the specific Tribune NFC (ID)
     * @return the places number
     * @throws NotFoundException if the Tribune does not exists in the database
     */
    int getPlaces(int NFC) throws NotFoundException;
    
    /**
     * Set the places number of a specific Tribune
     * @param NFC : the specific Tribune NFC (ID)
     * @param places : the new places number
     * @throws NotFoundException if the Tribune does not exists in the database
     */
    void setPlaces(int NFC, int places) throws NotFoundException;
    
    /**
     * Get the localisation of a specific Tribune
     * @param NFC : the specific Tribune NFC (ID)
     * @return The localisation
     * @throws NotFoundException if the Tribune does not exists in the database
     */
    String getLocalisation(int NFC) throws NotFoundException;
    
    /**
     * Get the explanation of a specific Tribune
     * @param NFC : the specific Tribune NFC (ID)
     * @return The explanation
     * @throws NotFoundException if the Tribune does not exists in the database
     */
    String getExplanation(int NFC) throws NotFoundException;
    
}
