package Models.BlackBox;

import Events.Event;
import Events.EventType;
import Events.PassageEvent;
import Events.VibrationEvent;
import Models.In.*;
import Models.Out.Lighting;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

import java.util.LinkedList;

/**
 * Created by bri_e on 10-03-17.
 */
public class BlackBox {
    private static int INDEX_PRECISION_IR_SENSOR =  3;
    private static int INDEX_VIBRATION_SENSOR =     4;
    private static int INDEX_LIGHT_SENSOR =         0;
    private static int INDEX_TEMPERATURE_SENSOR =   1;
    private static int INDEX_SLIDER_1 =             6;
    private static int INDEX_SLIDER_2 =             7;
    private static int SEATS_NUMBER =               4;

    //BlackBox List
    private LinkedList<Event> eventList;

    //The Cases which will be filled with serie of events in order to trigger reactions
    private GoalCase goalCase;

    //Phidgets Object that react on events or cases.
    private InterfaceKitPhidget interfaceKitPhidget;
    private Goal     goal;
    private Bar      barSouth;
    private Bar      barNorth;
    private Heat     heat;
    private Light    light;
    private Lighting lighting;
    private Stand    stdSouth;
    private Stand    stdNorth;
    private LapCalculator lapCntr;



    public BlackBox(InterfaceKitPhidget interfaceKitPhidget){
        eventList = new LinkedList<>();
        this.interfaceKitPhidget = interfaceKitPhidget;
        try {
            goal     = new Goal(interfaceKitPhidget,    INDEX_PRECISION_IR_SENSOR,  INDEX_VIBRATION_SENSOR,this);
            barNorth = new Bar(interfaceKitPhidget,     INDEX_SLIDER_1,                                    this);
            barSouth = new Bar(interfaceKitPhidget,     INDEX_SLIDER_2,                                    this);
            heat     = new Heat(interfaceKitPhidget,    INDEX_TEMPERATURE_SENSOR,                          this);
            light    = new Light(interfaceKitPhidget,   INDEX_LIGHT_SENSOR,                                this);
            lighting = new Lighting(interfaceKitPhidget);
            stdNorth = new Stand(interfaceKitPhidget, "Northen Stand",         SEATS_NUMBER,          this);
            stdSouth = new Stand(interfaceKitPhidget, "Southen Stand",         SEATS_NUMBER,          this);
            lapCntr  = new LapCalculator(this);

        } catch(PhidgetException e) {
            System.out.println("Error while loading phidgets objects : " + e);
        }
    }

    public String processElement(Event event){
        String log = "Proceed event : ";
        eventList.add(event);
        switch (event.getType()) {
            case BAR_EVENT:
                try {
                    log += "\nchange in Bar population : BSouth = " + barSouth.getAffluence() + " and BNorth = " + barNorth.getAffluence();
                } catch(PhidgetException e) {
                    System.out.println("Error while updating bars : " + e);
                }
                break;
            case HEAT_EVENT:
                try {
                    heat.refreshHeat();
                } catch (PhidgetException e) {
                    System.out.println("Error while updating heat : " + e);
                }
                log += "\nchange in Temperature : " + heat.getHeat();
                //TODO : Here call the API in order to decide if 1) it is time to water the field 2) what to do with the roof.
                break;
            case LIGHT_EVENT:
                try {
                    light.refreshLight();
                    lighting.updatePower(light.getIntensityStep());
                } catch(PhidgetException e) {
                    System.out.println("Error while updating light and lighting : " + e);
                }
                log += "\nchange in brightness : + " + light.getIntensityStep();
                break;
            case PASSAGE_EVENT:
                goalCase = new GoalCase((PassageEvent) event);
                log += "\npassage between the goal poles";
                break;
            case STAND_EVENT:
                log += "\nStands Actual State : ";
                for(int i = 0; i > stdNorth.getNumberOfSeats() ; i++) {
                    log += "std north : ";
                    if(stdNorth.getSeats()[i]) log+= "taken";
                    else log += "free";
                    log += "\n";
                }
                for(int i = 0; i > stdSouth.getNumberOfSeats() ; i++) {
                    log += "std south : ";
                    if(stdNorth.getSeats()[i]) log+= "taken";
                    else log += "free";
                    log += "\n";
                }
                log += "\nthere was a change in the Stand";
                break;
            case TURN_EVENT:
                log += "\nnew turn or player";
                break;
            case VIBRATION_EVENT:
                if ((goalCase.hasGoalHappened((VibrationEvent) event)) && (System.currentTimeMillis() - goal.getLastGoal()) > 8000){
                    goal.incrementGoal(System.currentTimeMillis());
                }
                log += "\nvibration of the goal structure";
                break;
            default:
                log = "Error : Event non recognized !";
                break;
        }
        if (eventList.size() > 50) eventList.removeLast();
        System.out.println(log);
        return log;
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



}
