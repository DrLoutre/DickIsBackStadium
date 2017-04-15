package dao;

import beans.Team;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.ArrayList;

public interface TeamDao {
    
    void addTeam(int ID, String Name) throws IntegrityException;
    
    boolean teamExists(int ID);
    
    ArrayList<Team> getAllTeam() throws NotFoundException;
    
    Team getTeam(int ID) throws NotFoundException;

    void deleteTeam(int ID) throws NotFoundException;
    
    String getName(int ID) throws NotFoundException;
}
