package dao;

import beans.Spectator;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import javafx.util.Pair;

public interface SpectatorDao {
    
    boolean spectatorExists(int ID);
    
    void addSpetator(int ID, String lastName, String firstName, 
            String tribuneNFC, int IDMatch) 
            throws IntegrityException,NotFoundException;
    
    Spectator getSpectator(int ID) throws NotFoundException;

    void deleteSpectator(int ID) throws NotFoundException;
    
    String getTribune(int ID) throws NotFoundException;
    
    Pair<String,String> getName(int ID) throws NotFoundException;
    
    int getMatch(int ID) throws NotFoundException;
    
    ArrayList<Spectator> getAllSpectator() throws NotFoundException;
    
    ArrayList<Spectator> getAllSpectator(String tribuneNFC) 
            throws NotFoundException;
    
    ArrayList<Spectator> getAllSpectator(int matchID) throws NotFoundException;
    
    ArrayList<Spectator> getAllSpectator(String tribuneNFC,int matchID) 
            throws NotFoundException;
}
