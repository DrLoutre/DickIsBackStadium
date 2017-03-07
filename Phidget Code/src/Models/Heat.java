package Models;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

/**
 * Created by bri_e on 28-02-17.
 */
public class Heat {

    private double heat;
    private int sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;

    public Heat(InterfaceKitPhidget ifk, int index) throws PhidgetException {
        interfaceKitPhidget = ifk;
        sensorIndex = index;
        heat = retHeat();
    }

    public double refreshHeat() throws PhidgetException {
        heat = retHeat();
        return heat;
    }


    private double retHeat() throws PhidgetException {
        int sensorvalue = interfaceKitPhidget.getSensorValue(sensorIndex);
        double roomtemp = (sensorvalue * 0.22222) - 61.11;
        return roomtemp;
    }

}
