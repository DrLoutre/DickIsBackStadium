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
    
    void setFirstName(String NFC, String firstName) throws NotFoundException;
    
    void setLastName(String NFC, String lastName) throws NotFoundException;
    
    void setPassword(String NFC, String password) throws NotFoundException;
    
    void setAge(String NFC, int age) throws NotFoundException;
    
    void setSex(String NFC, String sex) throws NotFoundException;
}
