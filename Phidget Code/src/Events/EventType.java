package Events;

public enum EventType {

    BAR_EVENT("Event au Bar"),
    HEAT_EVENT("Event du Thermomètre"),
    LIGHT_EVENT("Event lumineux"),
    PASSAGE_EVENT("Event de passage"),
    STAND_EVENT("Event de tribune"),
    TURN_EVENT("Tour supplémentaire"),
    VIBRATION_EVENT("Event de vibration");

    private String evnt;

    EventType(String evnt) {
        this.evnt = evnt;
    }

    public String getEvnt() {
        return this.evnt;
    }
}
