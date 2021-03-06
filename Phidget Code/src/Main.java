import Models.BlackBox.BlackBox;
import com.phidgets.InterfaceKitPhidget;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        try {
            System.out.print("Initialising the InterfaceKit Phidget...");

            InterfaceKitPhidget interfaceKitPhidget = new InterfaceKitPhidget();

            System.out.println("  ... done !");
            /*
                                            ==> Tenter avec ça en commentaires.
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

            interfaceKitPhidget.addAttachListener(attachListener);
            interfaceKitPhidget.addDetachListener(detachListener);
            interfaceKitPhidget.addErrorListener(errorListener);
            interfaceKitPhidget.addInputChangeListener(inputChangeListener);
            interfaceKitPhidget.addOutputChangeListener(outputChangeListener);
            interfaceKitPhidget.addSensorChangeListener(sensorChangeListener);
            */

            interfaceKitPhidget.open(329107);
            interfaceKitPhidget.waitForAttachment();

            System.out.print("Initialising the blackbox...");

            BlackBox blackbox = new BlackBox(interfaceKitPhidget);

            System.out.println("  ... done ! ");

            /**
             * Code for roof.
             */
/*
            Roof roof = new Roof();
            if(roof.isOpen()){
                roof.actionateRoofMotor();
            }

            WeatherStation weatherStation = new WeatherStation(interfaceKitPhidget, 1);

            weatherStation.refreshWeather();
            weatherStation.refreshSunPhases();

            Weather weather = weatherStation.getWeather();

            System.out.println("Weather : " + weather + " ++ Température : " + weatherStation.refreshHeat());

            if(roof.isOpen()) roof.actionateRoofMotor();
            System.out.println("Ouvert ? : " + roof.isOpen());

            switch (weather) {
                case RAIN:
                    if(roof.isOpen()) roof.actionateRoofMotor();
                    break;

                case CLOUD:
                    if(weatherStation.refreshHeat() < 2) {
                        if (roof.isOpen()) roof.actionateRoofMotor();
                    } else {
                        if (!roof.isOpen()) roof.actionateRoofMotor();
                    }
                    break;

                case SNOW:
                    if(roof.isOpen()) roof.actionateRoofMotor();
                    break;

                case SUN:
                    if(weatherStation.refreshHeat() < 2){
                        if(roof.isOpen()) roof.actionateRoofMotor();
                    } else {
                        if(weatherStation.refreshHeat() > 28){
                            if(roof.isOpen()) roof.actionateRoofMotor();
                        } else {
                            if(!roof.isOpen()) roof.actionateRoofMotor();
                        }
                    }
                    break;

            }
            System.out.println("Ouvert ? : " + roof.isOpen());
*/


            /*Code for Automatic field*/
/*
            Roof roof = new Roof();
            if(roof.isOpen()){
                roof.actionateRoofMotor();
            }
            Field field = new Field(interfaceKitPhidget, 8, 7, 6);

            WeatherStation weatherStation = new WeatherStation(interfaceKitPhidget, 1);

            weatherStation.refreshWeather();
            weatherStation.refreshSunPhases();

            Heat heat = new Heat(interfaceKitPhidget, 1);
            if (roof.isOpen()) {
                switch (weatherStation.getWeather()) {
                    case RAIN:
                        field.setWatering(false);
                        if ((heat.refreshHeat()) < 5 )
                            field.setHeating(true);
                        break;
                    case SUN:
                        if ((heat.refreshHeat()) > 20) {
                            field.setWatering(true);
                        } else {
                            field.setWatering(false);
                            if ((heat.refreshHeat()) < 5){
                                field.setHeating(true);
                            }
                        }
                        break;
                    case CLOUD:
                        if ((heat.refreshHeat()) > 35) {
                            field.setWatering(true);
                        } else {
                            field.setWatering(false);
                            if ((heat.refreshHeat()) < 5){
                                field.setHeating(true);
                            }
                        }
                        break;
                    case SNOW:
                        field.setHeating(true);
                        break;
                    default:
                        break;
                }
            } else {
                switch (weatherStation.getWeather()) {
                    case RAIN:
                        if ((heat.refreshHeat()) > 30)
                            field.setWatering(true);
                        else
                            field.setWatering(false);
                        break;
                    case SUN:
                        if ((heat.refreshHeat()) > 30) {
                            field.setWatering(true);
                        } else {
                            field.setWatering(false);
                        }
                        break;
                    case CLOUD:
                        if ((heat.refreshHeat()) > 35) {
                            field.setWatering(true);
                        } else {
                            field.setWatering(false);
                        }
                        break;
                    default:
                        break;
                }
                if(heat.refreshHeat() < 5) field.setHeating(true);
            }
            System.out.println("Etat chauffage : " + field.isHeating() + "   Etat arrosage : " + field.isWatering());*/

            Thread.sleep(60000000);
        } catch(Exception e) {
            System.out.println(e + e.getLocalizedMessage());
        }

    }




}
