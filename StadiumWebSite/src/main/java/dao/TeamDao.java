package dao;

import beans.Team;
import exceptions.NotFoundException;
import java.util.ArrayList;

public interface TeamDao {
    
    /**
     * Add a Team in the database
     * @param Name : the Team name
     * @return the ID of the added Team
     */
    int addTeam(String Name);
    
    /**
     * Check if a Team exists in the database
     * @param ID : the specific Team ID
     * @return True if the Team exists in the database, false otherwise
     */
    boolean teamExists(int ID);
    
    /**
     * Get all the Team from the database
     * @return All the Team Bean
     * @throws NotFoundException if there is not any Team in the database
     */
    ArrayList<Team> getAllTeam() throws NotFoundException;
    
    /**
     * Get a specific Team from the database
     * @param ID : the specific Team ID
     * @return The Team Bean
     * @throws NotFoundException if the Team does not exists in the database
     */
    Team getTeam(int ID) throws NotFoundException;

    /**
     * Delete a specific Team from the database
     * @param ID : the specific Team ID
     * @throws NotFoundException if the Team does not exists in the database
     */
    void deleteTeam(int ID) throws NotFoundException;
    
    /**
     * Get the Name of a specific Team from the database
     * @param ID : the specific Team ID
     * @return the Team Name
     * @throws NotFoundException if the Team does not exists in the database
     */
    String getName(int ID) throws NotFoundException;

    /**
     * Set the Name of a specific Team in the database
     * @param ID : the specific Team ID
     * @param name : the new name
     * @throws NotFoundException if the Team does not exists in the database
     */
    void setName(int ID, String name) throws NotFoundException;
}
