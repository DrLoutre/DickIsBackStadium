package com.example.gecko.smartstadium.events;

import com.example.gecko.smartstadium.classes.Refreshment;

import java.util.ArrayList;

public class RefreshmentsEvent {

    private ArrayList<Refreshment> refreshments;

    public RefreshmentsEvent(ArrayList<Refreshment> refreshment) {
        this.refreshments = refreshment;
    }

    public ArrayList<Refreshment> getRefreshment() {
        return refreshments;
    }

    public void setRefreshment(ArrayList<Refreshment> refreshment) {
        this.refreshments = refreshment;
    }
}
