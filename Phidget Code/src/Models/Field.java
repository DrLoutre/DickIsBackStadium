package Models;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

import javax.xml.parsers.FactoryConfigurationError;
import java.awt.*;

/**
 * Created by bri_e on 08-03-17.
 */
public class Field {

    private InterfaceKitPhidget interfaceKitPhidget;
    private int indexRed;
    private int indexBlue;
    private int indexGreen;
    private boolean isWatering;
    private boolean isHeating;

    public Field(InterfaceKitPhidget ifk, int iR, int iB, int iG){
        interfaceKitPhidget = ifk;
        indexRed = iR;
        indexBlue = iB;
        indexGreen = iG;
        isWatering = false;
        isHeating = false;
    }

    public void setWatering(boolean state){
        isWatering = state;
        if (isWatering) isHeating = false;
    }

    public boolean isWatering(){
        return isWatering;
    }

    public void setHeating(boolean state){
        isHeating = state;
        if (isHeating) isWatering = false;
    }

    public boolean isHeating(){
        return isHeating;
    }

    private void setLight(){
        if (isWatering) {
            if (isHeating) System.out.println("Erreur : état incorrect !!"); //throw new IllegalStateException?
            else {
                try {
                    interfaceKitPhidget.setOutputState(indexGreen, false);
                    interfaceKitPhidget.setOutputState(indexRed, false);
                    interfaceKitPhidget.setOutputState(indexBlue, true);
                } catch(PhidgetException e){
                    System.out.println("Exception while setting RGBLED to Blue : " + e);
                }
            }
        }
        if (isHeating) {
            if (isWatering) System.out.println("Erreur : état incorrect !!"); //thow new IllegalStateException?
            else {
                try {
                    interfaceKitPhidget.setOutputState(indexBlue, false);
                    interfaceKitPhidget.setOutputState(indexGreen, false);
                    interfaceKitPhidget.setOutputState(indexRed, true);
                } catch (PhidgetException e) {
                    System.out.println("Exception while setting RGBLED to Red ");
                }
            }
        }
    }

}
