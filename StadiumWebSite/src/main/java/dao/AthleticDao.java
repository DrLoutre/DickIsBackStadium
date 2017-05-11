package dao;

import beans.Athletic;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.ArrayList;

public interface AthleticDao {
    /**
     * Add an athletic in the database
     * @param NFC : the NFC (id) of the athletic
     * @param firstName : the first name of the athletic
     * @param lastName : the last name of the athletic
     * @param age : the age of the athletic
     * @param sex : the sex of the athletic
     * @param password : the password of the athletic
     * @throws IntegrityException if there is already an athletic with the
     *      specific NFC in the database
     */
    void addAthletic(String NFC, String firstName, String lastName, int age, 
            String sex, String password) throws IntegrityException;
    
    /**
     * Get an athletic with a specific NFC (id) from the database
     * @param NFC : the NFC (id) of the searched athletic
     * @return the athletic bean with all the informations of the searched 
     *      athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    Athletic getAthletic(String NFC) throws NotFoundException;
    
    /**
     * Check if there is an athletic with the specific NFC in the database
     * @param NFC : the NFC (id) of the searched athletic
     * @return true if is existsn, false otherwise
     */
    boolean athleticExists(String NFC);

    /**
     * Delete an ahtletic from the database
     * @param NFC : the NFC (id) of the athletic to delete
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    void deleteAthletic(String NFC) throws NotFoundException;
    
    /**
     * Get the first name of an athletic in the database
     * @param NFC : the NFC (id) of the searched athletic
     * @return the first name of the searchd athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    String getFirstName(String NFC) throws NotFoundException;
    
    /**
     * Get the last name of an athletic in the database
     * @param NFC : the NFC (id) of the searched athletic
     * @return the last name of the searchd athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    String getLastName(String NFC) throws NotFoundException;
    
    /**
     * Get the hashed password of an athletic in the database
     * @param NFC : the NFC (id) of the searched athletic
     * @return the hashed password of the searchd athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    String getHashedPassword(String NFC) throws NotFoundException;
    
    /**
     * Get all the athletic of the database
     * @return the list of all the athletic bean
     * @throws NotFoundException if there's not any athletic in the database
     */
    ArrayList<Athletic> getAllAthletic() throws NotFoundException;
    
    /**
     * Set the first name of a specific athletic in the database
     * @param NFC : the NFC (id) of the specific athletic
     * @param firstName : the new first name of the athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    void setFirstName(String NFC, String firstName) throws NotFoundException;
    
    /**
     * Set the last name of a specific athletic in the database
     * @param NFC : the NFC (id) of the specific athletic
     * @param lastName : the new last name of the athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    void setLastName(String NFC, String lastName) throws NotFoundException;
    
    /**
     * Set the password of a specific athletic in the database
     * @param NFC : the NFC (id) of the specific athletic
     * @param password : the new password of the athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    void setPassword(String NFC, String password) throws NotFoundException;
    
    /**
     * Set the age of a specific athletic in the database
     * @param NFC : the NFC (id) of the specific athletic
     * @param age : the new age of the athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    void setAge(String NFC, int age) throws NotFoundException;
    
    /**
     * Set the sex of a specific athletic in the database
     * @param NFC : the NFC (id) of the specific athletic
     * @param sex : the new sex of the athletic
     * @throws NotFoundException if there's no athletic with the given NFC in 
     *      the database
     */
    void setSex(String NFC, String sex) throws NotFoundException;
    
    /**
     * Check the connection information of an athletic
     * @param NFC : the NFC (id) of the specific athletic
     * @param password : the password
     * @return false if there's no athletic with the given NFC in 
     *      the database of if it's not the good password, true otherwise
     */
    boolean connect(String NFC, String password);
}
