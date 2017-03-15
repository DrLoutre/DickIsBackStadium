package Events;

import static Events.EventType.PASSAGE_EVENT;

/**
 * Created by bri_e on 13-03-17.
 */
public class PassageEvent extends Event {

    public PassageEvent(double time) {
        super(time, PASSAGE_EVENT);
        //System.out.println("Creation passage event " + this.getTime());
    }

}
