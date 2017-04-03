/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author Thibaut
 */
public class Spectator {
    
    private int id;
    private String lastName;
    private String firstName;
    private String tribuneNFC;
    private int id_Match;
    
    public Spectator(){
        
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public void setFirstName(String FirstName){
        firstName = FirstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public void setLastName(String LastName){
        lastName = LastName;
    }
    
    public String geTribuneNFC(){
        return tribuneNFC;
    }
    
    public void setTribuneNFC(String TribuneNFC){
        tribuneNFC = TribuneNFC;
    }
    
    public int getIDMatch(){
        return id_Match;
    }
    
    public void setIDMatch(int id_match){
        id_Match = id_match;
    }
}
