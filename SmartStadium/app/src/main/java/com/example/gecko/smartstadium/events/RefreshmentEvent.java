package com.example.gecko.smartstadium.events;

import com.example.gecko.smartstadium.classes.Refreshment;

public class RefreshmentEvent {

    private Refreshment refreshment;

    public RefreshmentEvent(Refreshment refreshment) {
        this.refreshment = refreshment;
    }

    public Refreshment getRefreshment() {
        return refreshment;
    }

    public void setRefreshment(Refreshment refreshment) {
        this.refreshment = refreshment;
    }
}
