package Models.In;

import Events.EventType;
import Events.HeatEvent;
import Events.PassageEvent;
import Models.BlackBox.BlackBox;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

/**
 * Created by bri_e on 28-02-17.
 */
public class Heat {

    private static double MILI_INTERVAL = 1000;

    private double heat;
    private int sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;
    private BlackBox blackBox;

    public Heat(InterfaceKitPhidget ifk, int index, BlackBox bbx) throws PhidgetException {
        interfaceKitPhidget = ifk;
        blackBox = bbx;
        sensorIndex = index;
        heat = retHeat();
        interfaceKitPhidget.addSensorChangeListener(new SensorChangeListener() {
            @Override
            public void sensorChanged(SensorChangeEvent sensorChangeEvent) {
                if (sensorChangeEvent.getIndex() == sensorIndex) {
                    double newval = 0;
                    try {
                        newval = (interfaceKitPhidget.getSensorValue(sensorIndex) * 0.22222) - 61.11;
                    } catch (PhidgetException e) {
                        System.out.println("Error while listening Phidget : " + e);
                    }
                    //System.out.println("new val = " + newval + " vs old val = " + heat);
                    if (Math.abs(newval - heat) > 0.5) {
                        if ((blackBox.noEvent(EventType.HEAT_EVENT))
                                || ((System.currentTimeMillis() - blackBox.getLast(EventType.HEAT_EVENT).removeLast().getTime()) > MILI_INTERVAL)) {
                            blackBox.processElement(new HeatEvent(System.currentTimeMillis()));
                        }
                    }
                }
            }
        });


    }

    public void refreshHeat() throws PhidgetException {
        heat = retHeat();
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
