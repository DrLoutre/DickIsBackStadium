package beans;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class Team {

    private int id;
    private String nom;
    private List<String> players;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public List<String> getPlayerList(){
        List<String> returnValue = new LinkedList<String>();
        for(String player_id : players){
            returnValue.add(player_id);
        }
        return returnValue;
    }
    
    public void setPlayerList(List<String> id_list){
        List<String> returnValue = new LinkedList<String>();
        for(String player_id : id_list){
            returnValue.add(player_id);
        }
        players = returnValue;
    }
    
    public boolean IsPlaying(String id_player){
        if(id_player == null) return false;
        for(String player_id : players){
            if(id_player.equals(player_id)) return true;
        }
        return false;
    }
    
    public void addPlayer(String id_player){
        if(!IsPlaying(id_player)){
            players.add(id_player);
        }
    }
}
