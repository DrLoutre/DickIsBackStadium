package Events;

/**
 * Created by bri_e on 13-03-17.
 */
public class LightEvent extends Event {

    public LightEvent(double time){
        super(time, EventType.LIGHT_EVENT);
    }
}
