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
public class Seat {
    
    private int id;
    private String tribuneNFC;
    private boolean occupied;
    
    public Seat(){
        
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    public String getTribuneNFC(){
        return tribuneNFC;
    }
    
    public void setTribuneNFC(String TribuneNFC){
        tribuneNFC = TribuneNFC;
    }
    
    public boolean getOccupied(){
        return occupied;
    }
    
    public void setOccupied(boolean Occupied){
        occupied = Occupied;
    }
}
