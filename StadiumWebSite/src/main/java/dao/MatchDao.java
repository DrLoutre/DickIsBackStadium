package dao;

import beans.Match;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Date;
import javafx.util.Pair;

public interface MatchDao {
    
    void addMatch(int ID, int idTeam1, int idTeam2, int goals1, int goals2, 
            Date date, boolean ended) 
            throws IntegrityException, NotFoundException;
    
    Match getMatch(int ID) throws NotFoundException;

    void deleteMatch(int ID) throws NotFoundException;
    
    boolean matchExists(int ID);
    
    ArrayList<Match> getAllMatch() throws NotFoundException;
    
    Pair<Integer,Integer> getGoals(int ID) throws NotFoundException;
    
    void setGoals(int ID, int goals1, int goals2) throws NotFoundException;
    
    /*
    Throws a NotFoundException if the Match has not been found in the database
    Throws an IntegrityException if the Match has been found but there is no
        team referenced with the specified id.
    */
    void setTeamGoals(int ID, int idTeam, int goals) 
            throws IntegrityException, NotFoundException;
    
    /*
    Throws a NotFoundException if the Match has not been found in the database 
        or if the Match has been found but there is no team referenced with at 
        least one of the specified id.
    */
    void SetIDTeam(int ID, int idTeam1, int idTeam2) throws NotFoundException;
    
    /*
    Throws a NotFoundException if the Match has not been found in the database 
        or if the Match has been found but there is no team referenced with the 
        specified id.
    */
    void setIDTeam1(int ID, int idTeam) throws NotFoundException;
    
    /*
    Throws a NotFoundException if the Match has not been found in the database 
        or if the Match has been found but there is no team referenced with the 
        specified id.
    */
    void setIDTeam2(int ID, int idTeam) throws NotFoundException;
    
    ArrayList<Match> getEndedMatch() throws NotFoundException;
    
    ArrayList<Match> getNotEndedMatch() throws NotFoundException;
}
