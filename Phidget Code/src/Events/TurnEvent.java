package Events;

/**
 * Created by bri_e on 13-03-17.
 */
public class TurnEvent extends Event {

    public TurnEvent(double time){
        super(time, EventType.TURN_EVENT);
    }
}
