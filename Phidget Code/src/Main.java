import Models.*;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.event.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        try {

            AttachListener attachListener;
            DetachListener detachListener;
            ErrorListener errorListener;
            InputChangeListener inputChangeListener;
            OutputChangeListener outputChangeListener;
            SensorChangeListener sensorChangeListener;

            attachListener = new AttachListener() {
                @Override
                public void attached(AttachEvent ae) {
                }
            };

            detachListener = new DetachListener() {
                @Override
                public void detached(DetachEvent detachEvent) {

                }
            };

            errorListener = new ErrorListener() {
                @Override
                public void error(ErrorEvent errorEvent) {

                }
            };

            inputChangeListener = new InputChangeListener() {
                @Override
                public void inputChanged(InputChangeEvent inputChangeEvent) {

                }
            };

            outputChangeListener = new OutputChangeListener() {
                @Override
                public void outputChanged(OutputChangeEvent outputChangeEvent) {

                }
            };

            sensorChangeListener = new SensorChangeListener() {
                @Override
                public void sensorChanged(SensorChangeEvent sensorChangeEvent) {

                }
            };

            InterfaceKitPhidget interfaceKitPhidget = new InterfaceKitPhidget();
            interfaceKitPhidget.addAttachListener(attachListener);
            interfaceKitPhidget.addDetachListener(detachListener);
            interfaceKitPhidget.addErrorListener(errorListener);
            interfaceKitPhidget.addInputChangeListener(inputChangeListener);
            interfaceKitPhidget.addOutputChangeListener(outputChangeListener);
            interfaceKitPhidget.addSensorChangeListener(sensorChangeListener);
            interfaceKitPhidget.open(329107);
            interfaceKitPhidget.waitForAttachment();


            /*
            Test code for Smart Lighting
            */

            Lighting    lighting = new Lighting(interfaceKitPhidget);
            System.out.println("Lighting created");
            Light       light    = new Light(interfaceKitPhidget, 0);

            int val = 0;

            while(true){
                val = light.refreshLight();
                switch (val){
                    case 0 :
                        lighting.setMaxPower();
                        break;
                    case 1 :
                        if (lighting.isAtMax()) lighting.decreasePower();
                        if (lighting.isAtMin()) lighting.increasePower();
                        break;
                    case 2 :
                        lighting.setLightingOff();
                        break;
                }
                System.out.println("Ambient Light : " + val);

            }
/*
            */

            /*
            Test Code for Smart LapCounter
             */
            /*
            LapCalculator lapCalculator = new LapCalculator();
            while(true){

            }



            Bar bar1 = new Bar(interfaceKitPhidget, 7);
            Bar bar2 = new Bar(interfaceKitPhidget, 6);

            while(true){
                System.out.println("Affluence Bar 1 = " + bar1.refreshAttendance());
                System.out.println("Affluence Bar 2 = " + bar2.refreshAttendance());
            }
 */

        } catch(Exception e) {
            System.out.println(e);
        }

    }




}
