package Models.BlackBox;

import Events.Event;
import Events.EventType;
import Events.PassageEvent;
import Events.VibrationEvent;
import Models.In.*;
import Models.Out.Field;
import Models.Out.Lighting;
import Models.Out.Roof;
import Modes.Mode;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

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
    private Goal           goal;
    private Bar            barSouth;
    private Bar            barNorth;
    private WeatherStation weather;
    private Light          light;
    private Lighting       lighting;
    private Stand          stdSouth;
    private Stand          stdNorth;
    private LapCalculator  lapCntr;
    private Field          field;
    private Roof           roof;
    private MatchPlanning  planning;
    private CommunicationListener communicationListener;
    private Mode curntMode;

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
            planning = new MatchPlanning();
            curntMode= new Mode();
            communicationListener = new CommunicationListener();



            final String begin1 =   "7 avr. 2017 20:00:00";
            final String end1 =     "7 avr. 2017 23:40:00";
            final SimpleDateFormat parser = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.FRENCH);

            Date b1 = null, e1 = null;

            try {
                b1 = parser.parse(begin1);
                e1 = parser.parse(end1);

            } catch(ParseException e) {
                System.out.println("Error while creating dates " +e);
            }


            System.out.println("Ajout des matchs");
            planning.addMatchToList(b1,e1);


            ArrayList<Match> tmp = planning.matchArrayList();
            for (Match e : tmp) {
                System.out.println("Match avant filtre : " + e.toString());
            }

            if (planning.areWeDuringAMatch()) {
                System.out.println("Nous sommes bien en match");
            }


        } catch(PhidgetException e) {
            System.out.println("Error while loading phidgets objects : " + e);
        }
    }

    public void processElement(Event event) throws PhidgetException{


        String log;


        boolean matchMode = curntMode.isMatch();

        if(!matchMode) {
            log = "Proceed event : ";
        } else {
            log = "Proceeding event in Match mode : ";
        }
        eventList.add(event);
        switch (event.getType()) {
            case BAR_EVENT:
                processBarEvent(log);
                break;
            case HEAT_EVENT:
                processHeatEvent(log);
                break;
            case LIGHT_EVENT:
                processLightEvent(log);
                break;
            case PASSAGE_EVENT:
                processGoalEvent(event,log);
                break;
            case STAND_EVENT:
                processStandEvent(log);
                break;
            case TURN_EVENT:
                log += "\nnew turn or player";
                break;
            case VIBRATION_EVENT:
                processGoalEvent(event,log);
                break;
            case NEWMATCHPLAN_EVENT:
                communicationListener.addReceivedMatch(planning);
                break;
            case DEMOPHASE_EVENT:
                curntMode = communicationListener.getNewDemoPhase();
                break;
            default:
                log = "Error : Event non recognized !";
                break;
        }
        if (eventList.size() > 50) eventList.removeLast();

        System.out.println(log);


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

    private String processBarEvent(String log){
        try {
            log += "\nchange in Bar population : BSouth = " + barSouth.getAffluence() + " and BNorth = " + barNorth.getAffluence();
        } catch (PhidgetException e) {
            log = "Error while updating bars : " + e;
        }
        return log;
    }

    private String processHeatEvent(String log){
        try {
            weather.refreshHeat();
        } catch (PhidgetException e) {
            log = "Error while updating heat : " + e;
        }
        log += "\nchange in Temperature : " + weather.getHeat();
        try {
            weather.refreshWeather();
            weather.refreshSunPhases();
        } catch (JSONException e) {
            log = "Error while accessing Weather API" + e;
        }
        log += "\n       meteo statut : " + weather.getWeather();
        Weather actualWeather = weather.getWeather();
        if (weather.getHeat() <= 5) {
            field.setHeating(true);
        } else {
            field.setHeating(false);
        }

        if (actualWeather.equals(Weather.SNOW) || actualWeather.equals(Weather.RAIN)) {
            if (roof.isOpen()) {
                try {
                    roof.actionateRoofMotor();
                } catch (PhidgetException e) {
                    log += "Error while closing the roof." + e;
                }
            }

        }
        if (actualWeather.equals(Weather.RAIN) || actualWeather.equals(Weather.SUN)) {
            if (weather.getHeat() < 0) {
                if (roof.isOpen()) {
                    try {
                        roof.actionateRoofMotor();
                    } catch (PhidgetException e) {
                        log += "Error while closing the roof" + e;
                    }
                }
            } else {
                if (!roof.isOpen()) {
                    try {
                        roof.actionateRoofMotor();
                    } catch (PhidgetException e) {
                        System.out.println("Error while opening the roof");
                    }
                }
            }
        }

        if (weather.isDay()) {
            if (actualWeather.equals(Weather.CLOUD) || actualWeather.equals(Weather.SUN))
                field.setWatering(false);
            log += "\n       we're at day";
        } else {
            if (weather.getHeat() > 15) {
                if (curntMode.isMatch()) {
                    log += "\nMatch Mode, Watering Ignored";
                } else {
                    field.setWatering(true);
                }
            }
            log += "\n       we're at night";
        }
        return log;
    }

    private String processLightEvent(String log){
        try {
            light.refreshLight();
            lighting.updatePower(light.getIntensityStep()); //Todo : pheraps create a match mode where there is more light during a match
        } catch (PhidgetException e) {
            log = "Error while updating light and lighting : " + e;
        }
        log += "\nchange in brightness : + " + light.getIntensityStep();
        return log;
    }

    private String processGoalEvent(Event event, String log){
        if (curntMode.isMatch()) {
            switch(event.getType()) {
                case VIBRATION_EVENT:
                    if ((goalCase.hasGoalHappened((VibrationEvent) event)) && (System.currentTimeMillis() - goal.getLastGoal()) > 8000) {
                        goal.incrementGoal(System.currentTimeMillis());
                    }
                    log += "\nvibration of the goal structure";
                case PASSAGE_EVENT:
                    goalCase = new GoalCase((PassageEvent) event);
                    log += "\npassage between the goal poles";
                    break;
            }
        } else {
            log += "\nno match ongoing, goal event ignored";
        }
        return log;
    }

    private String processStandEvent(String log){
        log += "\nStands Actual State \n: ";
        for (int i = 0; i < stdNorth.getNumberOfSeats(); i++) {
            log += "std north : ";
            if (stdNorth.getSeats()[i]) log += "taken";
            else log += "free";
            log += "\n";
        }
        for (int i = 0; i < stdSouth.getNumberOfSeats(); i++) {
            log += "std south : ";
            if (stdNorth.getSeats()[i]) log += "taken";
            else log += "free";
            log += "\n";
        }
        log += "\nthere was a change in the Stand";
        return log;
    }

    private String processTurnEvent(String log){

        return log;
    }

    private String processComEvent(String log){

        return log;
    }

}
