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
public class Tribune {
    
    private String NFC;
    private int places;
    private String localisation;
    private String description;
    
    public Tribune(){
        
    }
    
    public String getNFC(){
        return NFC;
    }
    
    public void setNFC(String nfc){
        NFC = nfc;
    }
    
    public int getPlaces(){
        return places;
    }
    
    public void setPlaces(int Places){
        places = Places;
    }
    
    public String getLocalisation(){
        return localisation;
    }
    
    public void setLocalisation(String Localisation){
        localisation = Localisation;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setDescription(String Description){
        description = Description;
    }
}
