package beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Spectator {
    
    private int id;
    private String lastName;
    private String firstName;
    private int tribuneNFC;
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
    
    public int geTribuneNFC(){
        return tribuneNFC;
    }
    
    public void setTribuneNFC(int TribuneNFC){
        tribuneNFC = TribuneNFC;
    }
    
    public int getIDMatch(){
        return id_Match;
    }
    
    public void setIDMatch(int id_match){
        id_Match = id_match;
    }
}
