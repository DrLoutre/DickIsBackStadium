package Events;

/**
 * Created by bri_e on 09-04-17.
 */
public class NewMatchPlanEvent extends Event{
    public NewMatchPlanEvent(double time) {
        super(time, EventType.NEWMATCHPLAN_EVENT);
    }
}
