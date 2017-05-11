package dao;

import beans.Match;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public interface MatchDao {
    
    /**
     * Add a match to the database
     * @param idTeam1 : the id of the first team who plays the match
     * @param idTeam2 : the id of the second team who plays the match
     * @param goals1 : the goals of the first team
     * @param goals2 : the goals of the second team
     * @param date : the date of the match, on the format "yyyy-MM-dd HH:mm:ss"
     * @param ended : the status of the match. True if it's ended, false 
     *      otherwise
     * @return the id of the match added in the database
     * @throws NotFoundException if one of the team id does not exist in the 
     *      database
     */
    int addMatch(int idTeam1, int idTeam2, int goals1, int goals2, String date, 
            boolean ended) throws NotFoundException;
    
    /**
     * Get the data of a specific match in the database
     * @param ID : the ID of the specific match
     * @return the Match Bean with all the data of the specific Match
     * @throws NotFoundException if theres no match with this id on the database
     */
    Match getMatch(int ID) throws NotFoundException;

    /**
     * delete a specific match in the database
     * @param ID : the ID of the specific match
     * @throws NotFoundException if theres no match with this id on the database
     */
    void deleteMatch(int ID) throws NotFoundException;
    
    /**
     * Check if a specific match exists in the database
     * @param ID : the ID of the specific match
     * @return true if there is a match with the specific ID in the database, 
     * false otherwise
     */
    boolean matchExists(int ID);
    
    /**
     * Get all the match saved in the database
     * @return all the implemented match bean
     * @throws NotFoundException if there is no match saved in the database
     */
    ArrayList<Match> getAllMatch() throws NotFoundException;
    
    /**
     * Get the 2 goals value for a specific match, the first goal value 
     *      is for the first team, and the second for the second team
     * @param ID : the ID of the specific match
     * @return the 2 goals value for a specific match, the first goal value 
     *      is for the first team, and the second for the second team
     * @throws NotFoundException if there is no match with the specific ID in 
     *      the database 
     */
    Pair<Integer,Integer> getGoals(int ID) throws NotFoundException;
    
    /**
     * Set the goals values for a specific match
     * @param ID : the ID of the specific match
     * @param goals1 : the new goal value for the first team
     * @param goals2 : the new goal value for the second team
     * @throws NotFoundException if there is no match with the specific ID in 
     *      the database 
     */
    void setGoals(int ID, int goals1, int goals2) throws NotFoundException;
    
    /**
     * Set the tow team ID of a specific match
     * @param ID : the ID of the specific match
     * @param idTeam1 : the new id of the first team
     * @param idTeam2 : the new id of the second team
     * @throws NotFoundException if the Match has not been found in the 
     *      database or if the Match has been found but there is no team 
     *      referenced with at least one of the specified id.
     */
    void SetIDTeam(int ID, int idTeam1, int idTeam2) throws NotFoundException;
    
    /**
     * Set the id of the first team for a specific match
     * @param ID : the ID of the specific match
     * @param idTeam : the new team id
     * @throws NotFoundException if the Match has not been found in the 
     *      database or if the Match has been found but there is no team 
     *      referenced with the specified id.
     */
    void setIDTeam1(int ID, int idTeam) throws NotFoundException;
    
    /**
     * Set the id of the second team for a specific match
     * @param ID : the ID of the specific match
     * @param idTeam : the new team id
     * @throws NotFoundException if the Match has not been found in the 
     *      database or if the Match has been found but there is no team 
     *      referenced with the specified id.
     */
    void setIDTeam2(int ID, int idTeam) throws NotFoundException;
    
    /**
     * Get all the ended match of the database
     * @return all the match bean
     * @throws NotFoundException if there is no ended match in the database
     */
    ArrayList<Match> getEndedMatch() throws NotFoundException;
    
    /**
     * Get all the not ended match of the database
     * @return al the match bean
     * @throws NotFoundException if the database does not store a not ended 
     *      match
     */
    ArrayList<Match> getNotEndedMatch() throws NotFoundException;
    
    /**
     * Set the date of a specific match in the database
     * @param ID : the ID of the specific match
     * @param date : the date of the match, on the format "yyyy-MM-dd HH:mm:ss"
     * @throws NotFoundException if the Match has not been found in the 
     *      database
     */
    void setDate(int ID, String date) throws NotFoundException;
    
    /**
     * Set the state of a specific match in the database
     * @param ID : the ID of the specific match
     * @param ended : the new status of the specific match
     * @throws NotFoundException if the Match has not been found in the 
     *      database
     */
    void setState(int ID, boolean ended) throws NotFoundException;
    
    /**
     * Get all the not ended match wich involve a specific athletic in at least 
     *      one of the teams
     * @param athleticNFC : the NFC (id) of the specific athletic
     * @return all the not ended match wich involve a specific athletic in at 
     *      least one of the teams
     * @throws NotFoundException if there is no match in the database wich  
     *      justify the conditions
     */
    List<Match> getNotEndedMatch(String athleticNFC) throws NotFoundException;
}
