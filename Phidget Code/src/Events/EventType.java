package Events;

public enum EventType {

    BAR_EVENT("Event at Bar"),
    HEAT_EVENT("Event from Thermometer"),
    LIGHT_EVENT("Event of light"),
    PASSAGE_EVENT("Event of passage"),
    STAND_EVENT("Event in stage"),
    TURN_EVENT("Lap supp"),
    VIBRATION_EVENT("Event of vibration"),
    NEWMATCHPLAN_EVENT("Event of new match plannification"),
    DEMOPHASE_EVENT("Phase of Demo");

    private String evnt;

    EventType(String evnt) {
        this.evnt = evnt;
    }

    public String getEvnt() {
        return this.evnt;
    }
}
