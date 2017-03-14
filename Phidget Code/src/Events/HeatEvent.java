package Events;

/**
 * Created by bri_e on 13-03-17.
 */
public class HeatEvent extends Event {

    public HeatEvent(double time){
        super(time, EventType.HEAT_EVENT);
    }
}
