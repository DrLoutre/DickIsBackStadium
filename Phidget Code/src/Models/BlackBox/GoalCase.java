package Models.BlackBox;

import Events.PassageEvent;
import Events.VibrationEvent;

/**
 * Created by bri_e on 14-03-17.
 */
public class GoalCase {

    private PassageEvent passageEvent;
    private VibrationEvent vibrationEvent;

    public GoalCase(PassageEvent firstEvent){
        passageEvent = firstEvent;
        vibrationEvent = null;
    }

    public boolean hasGoalHappened(VibrationEvent secondEvent){
        vibrationEvent = secondEvent;
        if((vibrationEvent.getTime() - passageEvent.getTime())<1000){
            return true;
        } else {
            return false;
        }
    }

}
