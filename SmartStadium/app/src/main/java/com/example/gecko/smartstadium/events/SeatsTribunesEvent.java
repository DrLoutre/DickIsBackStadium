package com.example.gecko.smartstadium.events;

import com.example.gecko.smartstadium.classes.Tribune;
import com.example.gecko.smartstadium.classes.custom.SeatsByTribune;

import java.util.ArrayList;

public class SeatsTribunesEvent {

    private ArrayList<SeatsByTribune> seatsByTribunes;

    public SeatsTribunesEvent(ArrayList<SeatsByTribune> seatsByTribunes) {
        this.seatsByTribunes = seatsByTribunes;
    }

    public ArrayList<SeatsByTribune> getSeatsByTribunes() {
        return seatsByTribunes;
    }

    public void setSeatsByTribunes(ArrayList<SeatsByTribune> seatsByTribunes) {
        this.seatsByTribunes = seatsByTribunes;
    }
}
