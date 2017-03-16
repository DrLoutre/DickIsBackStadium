package Models.In;

import Events.EventType;
import Events.PassageEvent;
import Events.VibrationEvent;
import Models.BlackBox.BlackBox;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

/**
 * Created by bri_e on 07-03-17.
 */
public class Goal {

    private static int MILI_INTERVAL = 100;

    private int indexIRSensor;
    private int indexVibrationSensor;
    private int goal;
    private double lastGoal;
    private BlackBox blackBox;
    private InterfaceKitPhidget interfaceKitPhidget;

    public Goal(InterfaceKitPhidget ifk, int indexPassage, int indexVibration, BlackBox bbx) throws PhidgetException {
        blackBox = bbx;
        interfaceKitPhidget = ifk;
        indexIRSensor = indexPassage;
        indexVibrationSensor = indexVibration;
        goal = 0;
        lastGoal=0;
        ifk.addSensorChangeListener(new SensorChangeListener() {
            @Override
            public void sensorChanged(SensorChangeEvent sensorChangeEvent) {
                switch(sensorChangeEvent.getIndex()) {
                    case 3 :
                        //System.out.println("Passage !");
                        try {
                            if (ifk.getSensorValue(indexIRSensor) < 100) {
                                if ((blackBox.noEvent(EventType.PASSAGE_EVENT))
                                        || ((System.currentTimeMillis() - blackBox.getLast(EventType.PASSAGE_EVENT).removeLast().getTime()) > MILI_INTERVAL)) {
                                    PassageEvent passageEvent = new PassageEvent(System.currentTimeMillis());
                                    blackBox.processElement(passageEvent);
                                }
                            }
                        } catch (PhidgetException e) {
                            System.out.println("Exception à la lecture du phidget de proximité : " + e);
                        }
                        break;
                    case 4:
                        //System.out.println("Vibration !");
                        if((blackBox.noEvent(EventType.VIBRATION_EVENT))
                                ||((System.currentTimeMillis() - blackBox.getLast(EventType.VIBRATION_EVENT).removeLast().getTime()) > MILI_INTERVAL)) {

                            try {
                                if (ifk.getSensorValue(indexVibrationSensor) > 500) {
                                    VibrationEvent vibrationEvent = new VibrationEvent(System.currentTimeMillis());
                                    blackBox.processElement(vibrationEvent);
                                }
                            } catch (PhidgetException e) {
                                System.out.println("Exception à la lecture du phidget de vibration : " + e);
                            }
                        }
                        break;
                    default:
                        break;

                }
            }
        });

    }

    public int getGoal(){
        return goal;
    }

    public double getLastGoal(){
        return lastGoal;
    }

    public void incrementGoal(double time){
        goal++;
        lastGoal = time;
        System.out.println("Goal ! : " + this.getGoal() + " at " + lastGoal);
    }




}
