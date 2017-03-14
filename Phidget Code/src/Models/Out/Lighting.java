package Models.Out;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;

/**
 * Created by bri_e on 22-02-17.
 */
public class Lighting {

    private static int connectedLEDS = 5;

    private int power;
    private InterfaceKitPhidget interfaceKitPhidget;

    public Lighting(InterfaceKitPhidget ifk) throws PhidgetException{
        interfaceKitPhidget = ifk;
        power = 0;
    }

    public void updatePower(int lightIntensity) throws PhidgetException {
        power = (100/lightIntensity);
        refreshLighting();
    }



    private void refreshLighting() throws PhidgetException {
        try {
            int rest = power;
            int step = 100/connectedLEDS;
            int ledNumber = 0;
            while(step <=  rest) {
                ledNumber++;
                rest -= step;
            }

            for (int x = 0 ; x < ledNumber ; x++) {
                if (!interfaceKitPhidget.getOutputState(x)) {
                    //System.out.println("On ouvre : " + i);
                    interfaceKitPhidget.setOutputState(x, true);
                }
            }

            for (int i = ledNumber; i < connectedLEDS; i++) {
                if (interfaceKitPhidget.getOutputState(i)) {
                    //System.out.println("On ferme : " + i);
                    interfaceKitPhidget.setOutputState(i, false);
                }
            }

        } catch (PhidgetException e) {
            System.out.println("Error while setting a new light intensity : " + e);
        }

    }

}
