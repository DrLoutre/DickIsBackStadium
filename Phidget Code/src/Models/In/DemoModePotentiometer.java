package Models.In;

import Events.DemoPhaseEvent;
import Events.EventType;
import Models.BlackBox.BlackBox;
import Modes.Mode;
import Modes.ModeType;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

import static java.lang.Thread.sleep;

/**
 * Created by bri_e on 09-04-17.
 */
public class DemoModePotentiometer {

    private static int MILI_INTERVAL = 500;
    private static int MIN_CURSVAL   = 0;
    private static int MAX_CURSVAL   = 1000;
    private static int NBR_DMODES    = 2;

    private int cursor;
    private int     sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;
    private BlackBox blackBox;

    public DemoModePotentiometer(InterfaceKitPhidget ifk, int index, BlackBox bbx){
        interfaceKitPhidget = ifk;
        blackBox = bbx;
        sensorIndex = index;
        cursor = 0;
        interfaceKitPhidget.addSensorChangeListener(new SensorChangeListener() {
            @Override
            public void sensorChanged(SensorChangeEvent sensorChangeEvent) {
                if (sensorChangeEvent.getIndex() == sensorIndex && blackBox.noEvent(EventType.DEMOPHASE_EVENT)
                        || ((System.currentTimeMillis() - blackBox.getLast(EventType.DEMOPHASE_EVENT).removeLast().getTime()) > MILI_INTERVAL)) {
                    try {
                        sleep(2000);
                        blackBox.processElement(new DemoPhaseEvent(System.currentTimeMillis()));
                    } catch(Exception e) {
                        System.out.println("Error while processing potentiometer event" + e);
                    }
                }
            }
        });
    }

    public Mode getCurrentMode() throws PhidgetException {
        cursor = interfaceKitPhidget.getSensorValue(sensorIndex);
        int value = cursor/(MAX_CURSVAL/NBR_DMODES) % NBR_DMODES;
        System.out.println(cursor + "==> outputed " + value + " with ");
        switch (value) {
            case 0 :
                System.out.println("Setting to normal mode... ");
                return new Mode();
            case 1 :
                return new Mode(ModeType.NORMAL_MODE, true);
            case 2 :
                System.out.println("Demo mode : 1... ");
                return new Mode(ModeType.DEMO1_MODE, false);
            case 3 :
                // Todo : add new demo modes
                System.out.println("Demo mode : 2 \n    ==> not yet implemented, setting to normal mode...");
                return new Mode();
            default :
                System.out.println("Error : unknown Mode, setting to Normal Mode...");
                return new Mode();
        }
    }


}
