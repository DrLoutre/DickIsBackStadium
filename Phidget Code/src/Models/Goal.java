package Models;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;

/**
 * Created by bri_e on 07-03-17.
 */
public class Goal {


    private double vibrationHappend;
    private double passageHappend;
    private int indexIRSensor;
    private int indexVibrationSensor;
    private int goal;
    private InterfaceKitPhidget interfaceKitPhidget;

    public Goal(InterfaceKitPhidget ifk, int indexPassage, int indexVibration) throws PhidgetException {
        interfaceKitPhidget = ifk;
        indexIRSensor = indexPassage;
        indexVibrationSensor = indexVibration;
        passageHappend = 0;
        vibrationHappend = 0;
        goal = 0;
        ifk.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent inputChangeEvent) {
                System.out.println("Passage !");
                try {
                    if (ifk.getSensorValue(indexIRSensor) < 100) {
                        passageHappend = System.currentTimeMillis();
                    }
                } catch(PhidgetException e) {
                    System.out.println("Exception à la lecture du phidget de proximité : " + e);
                }
            }
        });
        ifk.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent inputChangeEvent) {
                System.out.println("Vibration !");
                try {
                    if (ifk.getSensorValue(indexVibration) > 700) {
                        vibrationHappend = System.currentTimeMillis();
                    }
                } catch(PhidgetException e){
                    System.out.println("Exception à la lecture du phidget de vibration : " + e);
                }
                if((vibrationHappend-passageHappend)<10){
                    goal++;
                    System.out.println("Gooaaaaaaaaaaal !!!!!!");
                }
            }
        });
    }

    public int getGoal(){
        return goal;
    }




}
