package beans;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Match implements Comparable<Match> {
    
    private int ID;
    private int id_team_1;
    private int id_team_2;
    private int goals_1;
    private int goals_2;
    private String date;
    private boolean ended;
    
    public Match(){
        
    }
    
    public int getID(){
        return ID;
    }
    
    public void setID(int id){
        ID = id;
    }
    
    public int getTeamID1(){
        return id_team_1;
    }
    
    public void setTeamID1(int idTeam1){
        id_team_1 = idTeam1;
    }
    
    public int getTeamID2(){
        return id_team_2;
    }
    
    public void setTeamID2(int idTeam2){
        id_team_2 = idTeam2;
    }
    
    public int getGoals1(){
        return goals_1;
    }
    
    public void setGoals1(int goals1){
        goals_1 = goals1;
    }
    
    public int getGoals2(){
        return goals_2;
    }
    
    public void setGoals2(int goals2){
        goals_2 = goals2;
    }
    
    public String getDate(){
        return date;
    }
    
    public void setDate(String date){
        this.date = date;
    }
    
    public boolean getEnded(){
        return ended;
    }
    
    public void setEnded(boolean ended){
        this.ended = ended;
    }

    @Override
    public int compareTo(@NotNull Match m) {
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return df.parseDateTime(this.getDate()).compareTo(df.parseDateTime(m.getDate()));
    }
}
