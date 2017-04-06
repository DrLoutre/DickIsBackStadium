package Models.In;

import Models.BlackBox.BlackBox;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

/**
 * Created by bri_e on 28-02-17.
 */
public class Heat {


    private double heat;
    private int sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;
    private BlackBox blackBox;

    public Heat(InterfaceKitPhidget ifk, int index, BlackBox bbx) throws PhidgetException {



    }



    public double getHeat() {
        return heat;
    }

    private double retHeat() throws PhidgetException {
        int sensorvalue = interfaceKitPhidget.getSensorValue(sensorIndex);
        double roomtemp = (sensorvalue * 0.22222) - 61.11;
        return roomtemp;
    }

}
