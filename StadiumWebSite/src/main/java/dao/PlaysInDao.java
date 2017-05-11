package dao;

import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.List;

public interface PlaysInDao {
    
    /**
     * Check if a specific athetic is playing in a specific team
     * @param NFC : the id of the specific athletic
     * @param idTeam : the id of the specific team
     * @return true is the athletic plays in the team, false otherwise
     */
    boolean isPlaying(String NFC, int idTeam);
    
    /**
     * Add an athletic to a team
     * @param NFC : the athletic ID
     * @param idTeam : the team ID
     * @throws IntegrityException if the atletic already plays in the team
     * @throws NotFoundException if the athletic or the athletic does not exist 
     *      in the database
     */
    void addEntry(String NFC, int idTeam) 
            throws IntegrityException, NotFoundException;
    
    /**
     * Get the team id of an athletic
     * @param NFC : thr id of the athletic
     * @return the id of the first team od the athletic
     * @throws NotFoundException if there is no team who involves the athletic
     */
    int getTeam(String NFC) throws NotFoundException;
    
    /**
     * Get all the players NFC (id) of a team
     * @param idTeam : the id of the team
     * @return all the players NFC (id) of a team
     * @throws NotFoundException if there is no player in the team
     */
    List<String> getPlayers(int idTeam) throws NotFoundException;
    
    /**
     * Get all the team id of an athetic
     * @param athleticNFC : the NFC (id) of the athletic
     * @return all the team id of an athetic
     * @throws NotFoundException if there is no team who involves the athletic
     */
    List<Integer> getAllTeamID(String athleticNFC) throws NotFoundException;
}
