package Models.In;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bri_e on 07-04-17.
 */
public class MatchPlanning {

    private ArrayList<Match> planned;

    public MatchPlanning(){
        planned = new ArrayList<>();
    }

    public void addMatchToList(Date start, Date end){
        deleteOldMatches();
        planned.add(new Match(start,end));
    }

    public boolean areWeDuringAMatch(){
        deleteOldMatches();
        ArrayList<Match> temporaryList = (ArrayList<Match>) planned.clone();
        temporaryList.removeIf(s -> !s.areWeDuringMatch());
        if (temporaryList.isEmpty()){
            return false;
        } else {
            //Normaly the DB must contain only one match at one hour.
            return true;
        }
    }

    public ArrayList<Match> matchArrayList(){
        deleteOldMatches();
        return planned;
    }


    private void deleteOldMatches(){
        planned.removeIf(s -> s.isMatchFinished());
    }
}
