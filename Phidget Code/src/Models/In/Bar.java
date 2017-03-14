package Models.In;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.Phidget;
import com.phidgets.PhidgetException;

/**
 * Created by bri_e on 28-02-17.
 */
public class Bar {

    private double attendance;
    private int     sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;

    public Bar(InterfaceKitPhidget ifk, int index) throws PhidgetException{
        interfaceKitPhidget = ifk;
        sensorIndex = index;
        attendance = retAttendance();
    }

    public double refreshAttendance() throws PhidgetException{
        retAttendance();
        return attendance;
    }


    private double retAttendance() throws PhidgetException{
        try{
            attendance = interfaceKitPhidget.getSensorValue(sensorIndex);
        } catch (PhidgetException e) {
            System.out.println("Error while getting bar affluence value : " + e);
        }
        return attendance;
    }

}
