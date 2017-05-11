package beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lap {
    
    private int ID;
    private int temp_hour;
    private int temp_min;
    private int temp_sec;
    private int temp_ms;
    private boolean is_beginning;
    private int id_race;
    
    public Lap(){
        
    }

    public Lap(int ID, int temp_hour, int temp_min, int temp_sec, int temp_ms, boolean is_beginning, int id_race) {
        this.ID = ID;
        this.temp_hour = temp_hour;
        this.temp_min = temp_min;
        this.temp_sec = temp_sec;
        this.temp_ms = temp_ms;
        this.is_beginning = is_beginning;
        this.id_race = id_race;
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

    public void setTempHour(int temp_hour) {
        this.temp_hour = temp_hour;
    }

    public void setTempMin(int temp_min) {
        this.temp_min = temp_min;
    }

    public void setTempSec(int temp_sec) {
        this.temp_sec = temp_sec;
    }

    public void setTempMs(int temp_ms) {
        this.temp_ms = temp_ms;
    }

    public boolean getIsBeginning() {
        return is_beginning;
    }

    public void setIsBeginning(boolean is_beginning) {
        this.is_beginning = is_beginning;
    }

    public int getIdRace(){
        return id_race;
    }
    
    public void setIdRace(int idRace){
        id_race = idRace;
    }
}
