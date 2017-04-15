package dao;

import beans.Match;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import javafx.util.Pair;

public interface MatchDao {
    
    void addMatch(int ID, int idTeam1, int idTeam2) 
            throws IntegrityException, NotFoundException;
    
    Match getMatch(int ID) throws NotFoundException;

    void deleteMatch(int ID) throws NotFoundException;
    
    boolean matchExists(int ID);
    
    ArrayList<Match> getAllMatch() throws NotFoundException;
    
    Pair<Integer,Integer> getGoals(int ID) throws NotFoundException;
}
