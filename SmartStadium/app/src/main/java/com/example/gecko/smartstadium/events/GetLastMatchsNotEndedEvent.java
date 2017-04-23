package com.example.gecko.smartstadium.events;

public class GetLastMatchsNotEndedEvent {

    private String id;

    public GetLastMatchsNotEndedEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
