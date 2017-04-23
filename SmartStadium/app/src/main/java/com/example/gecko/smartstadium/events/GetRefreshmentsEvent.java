package com.example.gecko.smartstadium.events;

public class GetRefreshmentsEvent {

    private boolean bestOne = false;

    public GetRefreshmentsEvent(boolean bestOne) {
        this.bestOne = bestOne;
    }

    public boolean isBestOne() {
        return bestOne;
    }

    public void setBestOne(boolean bestOne) {
        this.bestOne = bestOne;
    }
}
