package com.example.gecko.smartstadium.events;

public class GetAthleticIdEvent {

    private String id;

    public GetAthleticIdEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
