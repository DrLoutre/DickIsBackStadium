package Events;

/**
 * Created by bri_e on 09-04-17.
 */
public class DemoPhaseEvent extends Event {

    public DemoPhaseEvent(double time) {
        super(time, EventType.DEMOPHASE_EVENT);
    }
}
