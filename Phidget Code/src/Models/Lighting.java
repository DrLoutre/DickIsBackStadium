package Models;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;

/**
 * Created by bri_e on 22-02-17.
 */
public class Lighting {

    private static int MAX_POWER = 3;
    private static int MIN_POWER = 0;


    private int power;
    private InterfaceKitPhidget interfaceKitPhidget;

    public Lighting(InterfaceKitPhidget ifk) throws PhidgetException{
        interfaceKitPhidget = ifk;
        power = 0;
    }

    public void increasePower() throws PhidgetException {
        if ((power+1)<=MAX_POWER) {
            power++;
            refreshLighting();
        }
    }

    public void decreasePower()throws PhidgetException {
        if ((power-1)>=MIN_POWER){
            power--;
            refreshLighting();
        }
    }

    public boolean isAtMax() {
        return (power == MAX_POWER);
    }

    public boolean isAtMin() {
        return (power == MIN_POWER);
    }

    public void setMaxPower() throws PhidgetException {
        power = MAX_POWER;
        refreshLighting();
    }

    public void setLightingOff() throws PhidgetException {
        power = MIN_POWER;
        refreshLighting();
    }



    private void refreshLighting() throws PhidgetException {
        try {
            for (int i = power; i < MAX_POWER; i++) {
                if (interfaceKitPhidget.getOutputState(i)) {
                    //System.out.println("On ferme : " + i);
                    interfaceKitPhidget.setOutputState(i, false);
                }
            }
            //System.out.println("Power da : " + power);
            for (int x = MIN_POWER; x < power; x++) {
                if (!interfaceKitPhidget.getOutputState(x)) {
                    //System.out.println("On ouvre : " + i);
                    interfaceKitPhidget.setOutputState(x, true);
                }
            }
        } catch (PhidgetException e) {
            System.out.println("Error while setting a new light intensity : " + e);
        }

    }

}
