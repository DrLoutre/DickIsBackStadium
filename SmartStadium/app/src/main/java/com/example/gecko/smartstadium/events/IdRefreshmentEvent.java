package com.example.gecko.smartstadium.events;

public class IdRefreshmentEvent {

    private int id;

    public IdRefreshmentEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
