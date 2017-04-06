/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.sql.Time;
import javafx.util.Pair;

/**
 *
 * @author Thibaut
 */
public class Lap {
    
    private int ID;
    private Time temp;
    private int temp_ms;
    private int id_race;
    
    public Lap(){
        
    }
    
    public int getID(){
        return ID;
    }
    
    public void setID(int id){
        ID = id;
    }
    
    public Pair<Time,Integer> getTemp(){
        return new Pair<Time, Integer>(temp,temp_ms);
    }
    
    public void setTemp(Time temp, int temp_ms){
        this.temp = (Time)temp.clone();
        this.temp_ms = temp_ms;
    }
    
    public int getIdRace(){
        return id_race;
    }
    
    public void setIdRace(int idRace){
        id_race = idRace;
    }
}
