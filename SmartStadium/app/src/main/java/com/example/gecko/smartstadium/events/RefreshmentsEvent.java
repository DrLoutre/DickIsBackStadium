package com.example.gecko.smartstadium.events;

import com.example.gecko.smartstadium.classes.Refreshment;

import java.util.ArrayList;

public class RefreshmentsEvent {

    private ArrayList<Refreshment> refreshments;
    private boolean bestOne;

    public RefreshmentsEvent(ArrayList<Refreshment> refreshment, boolean bestOne) {
        this.refreshments = refreshment;
        this.bestOne = bestOne;
    }

    public ArrayList<Refreshment> getRefreshment() {
        return refreshments;
    }

    public void setRefreshment(ArrayList<Refreshment> refreshment) {
        this.refreshments = refreshment;
    }

    public boolean isBestOne() {
        return bestOne;
    }

    public void setBestOne(boolean bestOne) {
        this.bestOne = bestOne;
    }
}
