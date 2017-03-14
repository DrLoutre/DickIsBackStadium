package Models;

import Events.Event;
import Events.EventType;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by bri_e on 10-03-17.
 */
public class BlackBox {

    private LinkedList<Event> eventList;



    public BlackBox(){
        eventList = new LinkedList<>();
    }

    public String processElement(Event event){
        String log = "Proceed event : ";
        eventList.addLast(event);
        switch (event.getType()) {
            case BAR_EVENT:
                log += "change in Bar population";
                break;
            case HEAT_EVENT:
                log += "change in Temperature";
                break;
            case LIGHT_EVENT:
                log += "change in brightness";
                break;
            case PASSAGE_EVENT:
                log += "passage between the goal poles";
                break;
            case STAND_EVENT:
                log += "change in the Stand";
                break;
            case TURN_EVENT:
                log += "new turn or player";
                break;
            case VIBRATION_EVENT:
                log += "vibration of the goal structure";
                break;
            default:
                log = "Error : Event non recognized !";
                break;
        }
        return log;
    }

    public Event getLast(EventType type){
        Event genericEvent = new Event(type);
        LinkedList<Event> temporaryList = (LinkedList<Event>)eventList.clone();
        LinkedList<Event> genericList = new LinkedList<>();
        genericList.add(genericEvent);
        temporaryList.retainAll(genericList);
        return temporaryList.removeLast();
    }


}
