package Models;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

/**
 * Created by bri_e on 07-03-17.
 */
public class Goal {


    private double vibrationHappend;
    private double passageHappend;
    private int indexIRSensor;
    private int indexVibrationSensor;
    private int goal;
    private double lastGoal;
    private InterfaceKitPhidget interfaceKitPhidget;

    public Goal(InterfaceKitPhidget ifk, int indexPassage, int indexVibration) throws PhidgetException {
        interfaceKitPhidget = ifk;
        indexIRSensor = indexPassage;
        indexVibrationSensor = indexVibration;
        passageHappend = 0;
        vibrationHappend = 0;
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
                                passageHappend = System.currentTimeMillis();
                            }
                        } catch (PhidgetException e) {
                            System.out.println("Exception à la lecture du phidget de proximité : " + e);
                        }
                        break;
                    case 4:
                        //System.out.println("Vibration !");
                        try {
                            if (ifk.getSensorValue(indexVibrationSensor) > 700) {
                                vibrationHappend = System.currentTimeMillis();
                            }
                        } catch(PhidgetException e){
                            System.out.println("Exception à la lecture du phidget de vibration : " + e);
                        }
                        if(((vibrationHappend-passageHappend)<10)){
                            if((int)(System.currentTimeMillis()-lastGoal)>8000){
                                lastGoal = System.currentTimeMillis();
                                goal++;
                                System.out.println("Gooaaaaaaaaaaal !!!!!!  " + getGoal());
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




}
