package Events;

/**
 * Created by bri_e on 13-03-17.
 */
public class Event {

    private EventType eventType;
    private double time;

    public Event(){}

    public Event(double evenTime, EventType evnt){
        time = evenTime;
        eventType = evnt;
    }

    public Event(EventType event){
        eventType = event;
    }

    public double getTime(){
        return time;
    }

    public EventType getType(){
        return eventType;
    }

}
