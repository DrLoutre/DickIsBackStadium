package Models.In;

import java.util.Date;

/**
 * Created by bri_e on 09-04-17.
 * This class will listen to every incoming communication.
 * Those communication will whether inform of new incoming matches or will command the initiation of demo phases.
 */
public class CommunicationListener {

    public CommunicationListener(){
        //Todo : install a communication listener and when a new match is received, add it to the event processing.
    }

    public void addReceivedMatch(MatchPlanning planning) {
        //Todo : Parse from the received data the new match to Add
        planning.addMatchToList(new Date(), new Date());
    }



}
