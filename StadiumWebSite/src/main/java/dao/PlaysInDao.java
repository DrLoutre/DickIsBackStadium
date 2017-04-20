package dao;

import beans.Team;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.List;

public interface PlaysInDao {
    
    boolean isPlaying(String NFC, int idTeam);
    
    void addEntry(String NFC, int idTeam) 
            throws IntegrityException, NotFoundException;
    
    int getTeam(String NFC) throws NotFoundException;
    
    List<String> getPlayers(int idTeam) throws NotFoundException;
    
    List<Integer> getAllTeamID(String athleticNFC) throws NotFoundException;
}
