package dao;

import beans.Spectator;
import exceptions.NotFoundException;
import java.util.ArrayList;
import javafx.util.Pair;

public interface SpectatorDao {
    
    /**
     * Check if a given Spectator exists
     * @param ID : the given Spectator ID
     * @return True if the Spectator exists in the database, false otherwise
     */
    boolean spectatorExists(int ID);
    
    /**
     * Add a Spectator in the database
     * @param lastName : the Spectator last name
     * @param firstName : the Spectator first name
     * @param tribuneNFC : the Tribune ID of the Spectator
     * @param IDMatch : the Match ID of the Spectator
     * @return the id of the added Spectator
     * @throws NotFoundException if the Tribune or the Match does not exist in 
     *      the database
     */
    int addSpetator(String lastName, String firstName, int tribuneNFC, 
            int IDMatch) throws NotFoundException;
    
    /**
     * Get the spectator Bean
     * @param ID : the ID of the specific Spectator
     * @return the spectator Bean
     * @throws NotFoundException if the spectator does not exists in the 
     *      database
     */
    Spectator getSpectator(int ID) throws NotFoundException;

    /**
     * Delete a specific Specator of the database
     * @param ID : the ID of the specific Spectator
     * @throws NotFoundException if the spectator does not exists in the 
     *      database
     */
    void deleteSpectator(int ID) throws NotFoundException;
    
    /**
     * Get the Tribune ID of a specific spectator
     * @param ID : the ID of the specific Spectator
     * @return the Tribune ID
     * @throws NotFoundException if the spectator does not exists in the 
     *      database
     */
    int getTribune(int ID) throws NotFoundException;
    
    /**
     * Get the first and last name of a specific Spectator
     * @param ID : the ID of the specific Spectator
     * @return the first and last name
     * @throws NotFoundException if the spectator does not exists in the 
     *      database
     */
    Pair<String,String> getName(int ID) throws NotFoundException;
    
    /**
     * Get the Match ID of a specific Spectator
     * @param ID : the ID of the specific Spectator
     * @return the Match ID
     * @throws NotFoundException if the spectator does not exists in the 
     *      database
     */
    int getMatch(int ID) throws NotFoundException;
    
    /**
     * Get all the spectator of the database
     * @return All the spectator Bean
     * @throws NotFoundException if there is not any Spectator in the database
     */
    ArrayList<Spectator> getAllSpectator() throws NotFoundException;
    
    /**
     * Get all the spectator of a specific Tribune of the database
     * @param tribuneNFC : the Tribune ID
     * @return All the spectator Bean
     * @throws NotFoundException if the Tribune does non exist or if there is 
     *      not any Spectator of the Tribune in the databas
     */
    ArrayList<Spectator> getAllSpectatorFromTribune(int tribuneNFC) 
            throws NotFoundException;
    
    /**
     * Get all the spectator of a specific Match of the database
     * @param matchID : the Match ID
     * @return All the spectator Bean
     * @throws NotFoundException if the Match does non exist or if there is not 
     *      any Spectator of the Match in the databas
     */
    ArrayList<Spectator> getAllSpectatorForMatch(int matchID) 
            throws NotFoundException;
    
    /**
     * Get all the spectator of a specific Match and in a specific Tribune from 
     *      the database
     * @param tribuneNFC : the Tribune ID
     * @param matchID : the Match ID
     * @return All the spectator Bean
     * @throws NotFoundException if the Tribune does non exist or if the Match 
     *      does non exist or if there is not any Spectator of the Match in the 
     *      Tribune in the databas
     */
    ArrayList<Spectator> getAllSpectator(int tribuneNFC,int matchID) 
            throws NotFoundException;
}
