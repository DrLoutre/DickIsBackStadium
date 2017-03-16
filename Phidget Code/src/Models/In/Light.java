package Models.In;

import Events.EventType;
import Events.HeatEvent;
import Events.LightEvent;
import Models.BlackBox.BlackBox;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;

/**
 * Created by bri_e on 21-02-17.
 */
public class Light{
    private static double MILI_INTERVAL = 1000;


    private int intensity;
    private int sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;
    private BlackBox blackBox;



    public Light(InterfaceKitPhidget ifk, int index, BlackBox bbx) throws PhidgetException {
        interfaceKitPhidget = ifk;
        blackBox = bbx;
        sensorIndex = index;
        intensity = retLightIntensity();
        interfaceKitPhidget.addSensorChangeListener(new SensorChangeListener() {
            @Override
            public void sensorChanged(SensorChangeEvent sensorChangeEvent) {
                if ((blackBox.noEvent(EventType.LIGHT_EVENT))
                        || ((System.currentTimeMillis() - blackBox.getLast(EventType.LIGHT_EVENT).removeLast().getTime()) > MILI_INTERVAL)) {
                    blackBox.processElement(new LightEvent(System.currentTimeMillis()));
                }
            }
        });

    }


    public void refreshLight() throws PhidgetException {
        intensity = retLightIntensity();
    }

    public int getIntensityStep(){
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
