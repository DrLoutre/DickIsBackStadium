package beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lap {
    
    private int ID;
    private int temp_hour;
    private int temp_min;
    private int temp_sec;
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

    public int getTempHour(){
        return temp_hour;
    }

    public int getTempMin(){
        return temp_min;
    }

    public int getTempSec(){
        return temp_sec;
    }

    public int getTempMs(){
        return temp_ms;
    }

    public void setTemp(int hour, int min, int sec, int ms){
        temp_min = min;
        temp_sec = sec;
        temp_ms = ms;
    }

    public int getIdRace(){
        return id_race;
    }
    
    public void setIdRace(int idRace){
        id_race = idRace;
    }
}
