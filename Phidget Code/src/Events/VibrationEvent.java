package Events;

/**
 * Created by bri_e on 13-03-17.
 */
public class VibrationEvent extends Event {

    public VibrationEvent(double time) {
        super(time, EventType.VIBRATION_EVENT);
        //System.out.println("Creation Vibration Event " + this.getTime());
    }
}
