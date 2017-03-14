package Events;

/**
 * Created by bri_e on 13-03-17.
 */
public class BarEvent extends Event {

    public BarEvent(double time){
        super(time, EventType.BAR_EVENT);
    }

}
