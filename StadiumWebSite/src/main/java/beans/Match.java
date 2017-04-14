package beans;

import javafx.util.Pair;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Match {
    
    private int ID;
    private int id_team_1;
    private int id_team_2;
    private int goals_1;
    private int goals_2;
    
    public Match(){
        
    }
    
    public int getID(){
        return ID;
    }
    
    public void setID(int id){
        ID = id;
    }
    
    public Pair<Integer,Integer> getTeamID(){
        return new Pair<Integer, Integer>(id_team_1,id_team_2);
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
        return new Pair<Integer, Integer>(goals_1,goals_2);
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
}
