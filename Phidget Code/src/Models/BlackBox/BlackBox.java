package Models.BlackBox;

import Events.Event;
import Events.EventType;
import Events.PassageEvent;
import Events.VibrationEvent;
import Models.In.Bar;
import Models.In.Goal;
import Models.In.Heat;
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

    //BlackBox List
    private LinkedList<Event> eventList;

    //The Cases which will be filled with serie of events in order to trigger reactions
    private GoalCase goalCase;

    //Phidgets Object that react on events or cases.
    private InterfaceKitPhidget interfaceKitPhidget;
    private Goal    goal;
    private Bar     barSouth;
    private Bar     barNorth;
    private Heat    heat;


    public BlackBox(InterfaceKitPhidget interfaceKitPhidget){
        eventList = new LinkedList<>();
        this.interfaceKitPhidget = interfaceKitPhidget;
        try {
            goal     = new Goal(interfaceKitPhidget,INDEX_PRECISION_IR_SENSOR,INDEX_VIBRATION_SENSOR, this);
            barNorth = new Bar(interfaceKitPhidget, INDEX_SLIDER_1, this);
            barSouth = new Bar(interfaceKitPhidget, INDEX_SLIDER_2, this);
            heat     = new Heat(interfaceKitPhidget, INDEX_TEMPERATURE_SENSOR, this);

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
                    log += "change in Bar population : BSouth = " + barSouth.getAffluence() + " and BNorth = " + barNorth.getAffluence();
                } catch(PhidgetException e) {
                    System.out.println("Error while updating bars : " + e);
                }
                System.out.println(log);
                break;
            case HEAT_EVENT:
                try {
                    heat.refreshHeat();
                } catch (PhidgetException e) {
                    System.out.println("Error while updating heat : " + e);
                }
                log += "change in Temperature : " + heat.getHeat();
                System.out.println(log);
                //TODO : Here call the API in order to decide if 1) it is time to water the field 2) what to do with the roof.
                break;
            case LIGHT_EVENT:
                log += "change in brightness : + ";
                break;
            case PASSAGE_EVENT:
                goalCase = new GoalCase((PassageEvent) event);
                log += "passage between the goal poles";
                break;
            case STAND_EVENT:
                log += "change in the Stand";
                break;
            case TURN_EVENT:
                log += "new turn or player";
                break;
            case VIBRATION_EVENT:
                if ((goalCase.hasGoalHappened((VibrationEvent) event)) && (System.currentTimeMillis() - goal.getLastGoal()) > 8000){
                    goal.incrementGoal(System.currentTimeMillis());
                }
                log += "vibration of the goal structure";
                break;
            default:
                log = "Error : Event non recognized !";
                break;
        }
        if (eventList.size() > 50) eventList.removeLast();
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
