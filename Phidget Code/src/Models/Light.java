package Models;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;

/**
 * Created by bri_e on 21-02-17.
 */
public class Light{
    private static int INTENSITY_STEP = 5;
    private static double MAX_INTENSITY = 1000.00;


    private int intensity;
    private int sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;



    public Light(InterfaceKitPhidget ifk, int index) throws PhidgetException {
        interfaceKitPhidget = ifk;
        sensorIndex = index;
        intensity = retLightIntensity();

    }


    public int refreshLight() throws PhidgetException {
        intensity = retLightIntensity();
        return intensity;
    }


    private int retLightIntensity()throws PhidgetException{
        double x = 0;
        try {
            x = interfaceKitPhidget.getSensorValue(sensorIndex);
        } catch(PhidgetException e) {
            System.out.println("Error while getting light sensor value : " + e);
        }
        return (int)(x/10) +1;
    }

}
