package Models.In;

import Events.BarEvent;
import Events.EventType;
import Models.BlackBox.BlackBox;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.Phidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

import java.time.format.SignStyle;

/**
 * Created by bri_e on 28-02-17.
 */
public class Bar {

    private static double MILI_INTERVAL = 100;

    private double attendance;
    private int     sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;
    private BlackBox blackBox;

    public Bar(InterfaceKitPhidget ifk, int index, BlackBox bbx) throws PhidgetException{
        interfaceKitPhidget = ifk;
        blackBox = bbx;
        sensorIndex = index;
        retAttendance();
        interfaceKitPhidget.addSensorChangeListener(new SensorChangeListener() {
            @Override
            public void sensorChanged(SensorChangeEvent sensorChangeEvent) {
                if (sensorChangeEvent.getIndex() == sensorIndex) {
                    if (blackBox.noEvent(EventType.BAR_EVENT)
                            || ((System.currentTimeMillis() - blackBox.getLast(EventType.BAR_EVENT).removeLast().getTime()) > MILI_INTERVAL)) {
                        blackBox.processElement(new BarEvent(System.currentTimeMillis()));
                    }
                }
            }
        });
    }

    private void retAttendance() throws PhidgetException{
        try{
            attendance = interfaceKitPhidget.getSensorValue(sensorIndex);
        } catch (PhidgetException e) {
            System.out.println("Error while getting bar affluence value : " + e);
        }
    }

    public int getAffluence()throws PhidgetException{
        retAttendance();
        return (int)attendance/10;
    }

}
