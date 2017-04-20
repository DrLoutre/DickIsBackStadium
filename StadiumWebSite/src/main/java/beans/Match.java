package beans;

//import exceptions.NotFoundException;
import java.util.Date;
import javafx.util.Pair;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Match {
    
    private int ID;
    private int id_team_1;
    private int id_team_2;
    private int goals_1;
    private int goals_2;
    private Date date;
    private boolean ended;
    
    public Match(){
        
    }
    
    public int getID(){
        return ID;
    }
    
    public void setID(int id){
        ID = id;
    }
    
    public Pair<Integer,Integer> getTeamID(){
        return new Pair<>(id_team_1,id_team_2);
    }
    
    public void setTeamID(int idTeam1, int idTeam2){
        id_team_1 = idTeam1;
        id_team_2 = idTeam2;
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
    
    public Pair<Integer,Integer> getGoals(){
        return new Pair<>(goals_1,goals_2);
    }
    
    public void setGoals(int goals1, int goals2){
        goals_1 = goals1;
        goals_2 = goals2;
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
    
//    public void setTeamGoals(int id_team, int goals) throws NotFoundException {
//        if(id_team != id_team_1 && id_team != id_team_2) throw new
//                NotFoundException("There is not Team " + id_team 
//                        + " in this match");
//        
//        if(id_team == id_team_1) goals_1 = goals;
//        else goals_2 = goals;
//    }
//    
//    public int getTeamGoals(int id_team) throws NotFoundException {
//        if(id_team != id_team_1 && id_team != id_team_2) throw new
//                NotFoundException("There is not Team " + id_team 
//                        + " in this match");
//        
//        if(id_team == id_team_1) return goals_1;
//        else return goals_2;
//    }
    
    public Date getDate(){
        return (Date)date.clone();
    }
    
    public void setDate(Date date){
        this.date = (Date)date.clone();
    }
    
    public boolean getEnded(){
        return ended;
    }
    
    public void setEnded(boolean ended){
        this.ended = ended;
    }
}
