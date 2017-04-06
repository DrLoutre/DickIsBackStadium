package Models.BlackBox;

import Events.Event;
import Events.EventType;
import Events.PassageEvent;
import Events.VibrationEvent;
import Models.In.*;
import Models.Out.Field;
import Models.Out.Lighting;
import Models.Out.Roof;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import org.json.JSONException;

import java.util.LinkedList;

/**
 * Created by bri_e on 10-03-17.
 */
public class BlackBox {
    private static int INDEX_LIGHT_SENSOR =         0;
    private static int INDEX_TEMPERATURE_SENSOR =   1;
    private static int INDEX_PRECISION_IR_SENSOR =  3;
    private static int SEATS_NUMBER =               4;
    private static int INDEX_R =                    5;
    private static int INDEX_VIBRATION_SENSOR =     4;
    private static int INDEX_SLIDER_1 =             6;
    private static int INDEX_SLIDER_2 =             7;
    private static int INDEX_G =                    6;
    private static int INDEX_B =                    7;

    //BlackBox List
    private LinkedList<Event> eventList;

    //The Cases which will be filled with serie of events in order to trigger reactions
    private GoalCase goalCase;

    //Phidgets Object that react on events or cases.
    private InterfaceKitPhidget interfaceKitPhidget;
    private Goal     goal;
    private Bar      barSouth;
    private Bar      barNorth;
    private WeatherStation weather;
    private Light    light;
    private Lighting lighting;
    private Stand    stdSouth;
    private Stand    stdNorth;
    private LapCalculator lapCntr;
    private Field    field;
    private Roof     roof;

    public BlackBox(InterfaceKitPhidget interfaceKitPhidget){
        eventList = new LinkedList<>();
        this.interfaceKitPhidget = interfaceKitPhidget;
        try {
            goal     = new Goal(interfaceKitPhidget,    INDEX_PRECISION_IR_SENSOR,  INDEX_VIBRATION_SENSOR,this);
            barNorth = new Bar(interfaceKitPhidget,     INDEX_SLIDER_1,                                    this);
            barSouth = new Bar(interfaceKitPhidget,     INDEX_SLIDER_2,                                    this);
            weather  = new WeatherStation(interfaceKitPhidget,    INDEX_TEMPERATURE_SENSOR,                this);
            light    = new Light(interfaceKitPhidget,   INDEX_LIGHT_SENSOR,                                this);
            lighting = new Lighting(interfaceKitPhidget);
            stdNorth = new Stand(interfaceKitPhidget, "Northen Stand",         SEATS_NUMBER,         this);
            stdSouth = new Stand(interfaceKitPhidget, "Southen Stand",         SEATS_NUMBER,         this);
            lapCntr  = new LapCalculator(this);
            field    = new Field(interfaceKitPhidget,   INDEX_R,        INDEX_B,        INDEX_G);
            roof     = new Roof();


        } catch(PhidgetException e) {
            System.out.println("Error while loading phidgets objects : " + e);
        }
    }

    public void processElement(Event event) throws PhidgetException{


        int barNorthAffluence = barNorth.getAffluence();
        int barSouthAffluence = barSouth.getAffluence();
        double temperature = weather.getHeat();
        String wt = weather.getWeather().toString();
        boolean day = weather.isDay();
        int brightness = light.getIntensityStep();
        boolean[] northStd = stdNorth.getSeats();
        boolean[] southStd = stdSouth.getSeats();

        //String log = "Proceed event : ";
        eventList.add(event);
        switch (event.getType()) {
            case BAR_EVENT:
                try {
                    //log += "\nchange in Bar population : BSouth = " + barSouth.getAffluence() + " and BNorth = " + barNorth.getAffluence();
                    barNorthAffluence = barNorth.getAffluence();
                    barSouthAffluence = barSouth.getAffluence();
                } catch(PhidgetException e) {
                    System.out.println("Error while updating bars : " + e);
                }
                break;
            case HEAT_EVENT:
                try {
                    weather.refreshHeat();
                } catch (PhidgetException e) {
                    System.out.println("Error while updating heat : " + e);
                }
                //log += "\nchange in Temperature : " + weather.getHeat();
                try {
                    weather.refreshWeather();
                    weather.refreshSunPhases();
                } catch(JSONException e) {
                    System.out.println("Error while accessing Weather API");
                }
                //log += "\n       meteo statut : " + weather.getWeather();
                wt = weather.getWeather().toString();
                day = weather.isDay();
                temperature = weather.getHeat();
                Weather actualWeather = weather.getWeather();
                if(weather.getHeat() <= 5){
                    field.setHeating(true);
                } else {
                    field.setHeating(false);
                }

                if(actualWeather.equals(Weather.SNOW) || actualWeather.equals(Weather.RAIN)) {
                    if(roof.isOpen()) {
                        try {
                            roof.actionateRoofMotor();
                        } catch(PhidgetException e) {
                            System.out.println("Error while closing the roof.");
                        }
                    }

                }
                if(actualWeather.equals(Weather.RAIN)||actualWeather.equals(Weather.SUN)){
                    if(weather.getHeat() < 0) {
                        if(roof.isOpen()) {
                            try{
                                roof.actionateRoofMotor();
                            } catch(PhidgetException e) {
                                System.out.println("Error while closing the roof");
                            }
                        }
                    } else {
                        if(!roof.isOpen()) {
                            try{
                                roof.actionateRoofMotor();
                            } catch(PhidgetException e) {
                                System.out.println("Error while opening the roof");
                            }
                        }
                    }
                }

                if (weather.isDay()) {
                    if (weather.equals(Weather.CLOUD) || weather.equals(Weather.SUN))
                    field.setWatering(false);
                    //log += "\n       day";
                } else {
                    if(weather.getHeat() > 15) {
                        field.setWatering(true);
                    }
                    //log += "\n       night";
                }

                break;
            case LIGHT_EVENT:
                try {
                    light.refreshLight();
                    lighting.updatePower(light.getIntensityStep());
                } catch(PhidgetException e) {
                    System.out.println("Error while updating light and lighting : " + e);
                }
                //log += "\nchange in brightness : + " + light.getIntensityStep();
                brightness = light.getIntensityStep();
                break;
            case PASSAGE_EVENT:
                goalCase = new GoalCase((PassageEvent) event);
                //log += "\npassage between the goal poles";
                break;
            case STAND_EVENT:
                /*log += "\nStands Actual State \n: ";
                for(int i = 0; i < stdNorth.getNumberOfSeats() ; i++) {
                    log += "std north : ";
                    if(stdNorth.getSeats()[i]) log+= "taken";
                    else log += "free";
                    log += "\n";
                }
                for(int i = 0; i < stdSouth.getNumberOfSeats() ; i++) {
                    log += "std south : ";
                    if(stdNorth.getSeats()[i]) log+= "taken";
                    else log += "free";
                    log += "\n";
                }*/
                //log += "\nthere was a change in the Stand";
                northStd = stdNorth.getSeats();
                southStd = stdSouth.getSeats();
                break;
            case TURN_EVENT:
                //log += "\nnew turn or player";
                break;
            case VIBRATION_EVENT:
                if ((goalCase.hasGoalHappened((VibrationEvent) event)) && (System.currentTimeMillis() - goal.getLastGoal()) > 8000){
                    goal.incrementGoal(System.currentTimeMillis());
                }
                //log += "\nvibration of the goal structure";
                break;
            default:
                //log = "Error : Event non recognized !";
                break;
        }
        if (eventList.size() > 50) eventList.removeLast();
        //System.out.println(log);

        System.out.print(printLog(barNorthAffluence, barSouthAffluence, temperature, wt, day, brightness, northStd, southStd));
    }

    public LinkedList<Event> getLast(EventType type){
        LinkedList<Event> temporaryList = (LinkedList<Event>)eventList.clone();
        //System.out.println("before filter " + temporaryList);
        temporaryList.removeIf(p -> !p.getType().equals(type));
        //System.out.println("after filter " + temporaryList);
        return temporaryList;
    }

    public boolean noEvent(EventType type){
        return this.getLast(type).isEmpty();
    }

    private StringBuilder printLog(int barNorthAffluence, int barSouthAffluence, double heat, String weather, boolean day, int brightness, boolean[] northStd, boolean[] southStd) {
        StringBuilder string = new StringBuilder(400);
        string
                .append('\r')
                .append(" %nBar Nord "    + barNorthAffluence)
                .append(" %nBar Sud "     + barSouthAffluence)
                .append(String.format(" %nTempérature %02f", heat))
                .append(" %nWeather "     + weather)
                .append(" %nDe jour ? "   + day)
                .append(" %nLuminosité "  + brightness)
                .append(" %nTribune Nord "+ northStd)
                .append(" %nTribune Sud " + southStd);

        return string;
    }


}
