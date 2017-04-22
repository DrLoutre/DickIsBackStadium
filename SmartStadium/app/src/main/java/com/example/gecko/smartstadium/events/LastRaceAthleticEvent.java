package com.example.gecko.smartstadium.events;

import com.example.gecko.smartstadium.classes.Lap;

import java.util.ArrayList;

public class LastRaceAthleticEvent {

    private ArrayList<Lap> laps;

    public LastRaceAthleticEvent(ArrayList<Lap> laps) {
        this.laps = laps;
    }

    public ArrayList<Lap> getLaps() {
        return laps;
    }

    public void setLaps(ArrayList<Lap> laps) {
        this.laps = laps;
    }
}
