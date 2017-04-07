package Models.In;

import java.util.Date;

/**
 * Created by bri_e on 07-04-17.
 */
public class Match {

    private Date startDate;
    private Date endDate;

    public Match(Date begin, Date end){
        startDate = begin;
        endDate = end;
    }

    public boolean areWeDuringMatch(){
        Date today = new Date();

        if (today.after(startDate) && today.before(endDate)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMatchFinished(){
        Date today = new Date();
        if (today.after(endDate)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return "Date début : " + startDate + " et date de fin : " + endDate;
    }
}
