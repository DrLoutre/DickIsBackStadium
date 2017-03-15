package Events;

import Models.In.Bar;

/**
 * Created by bri_e on 13-03-17.
 */
public class BarEvent extends Event {

    public BarEvent(double time){
        super(time, EventType.BAR_EVENT);
        //System.out.println("Creation of a Bar Event");
    }

}
