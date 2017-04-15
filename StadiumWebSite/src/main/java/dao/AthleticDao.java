package dao;

import beans.Athletic;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.ArrayList;

public interface AthleticDao {
    
    void addAthletic(String NFC, String firstName, String lastName, int age, 
            String sex, String password) throws IntegrityException;
    
    Athletic getAthletic(String NFC) throws NotFoundException;
    
    boolean athleticExists(String NFC);

    void deleteAthletic(String NFC) throws NotFoundException;
    
    String getFirstName(String NFC) throws NotFoundException;
    
    String getLastName(String NFC) throws NotFoundException;
    
    String getPassword(String NFC) throws NotFoundException;
    
    ArrayList<Athletic> getAllAthletic() throws NotFoundException;
}