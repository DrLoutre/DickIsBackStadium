package com.example.gecko.smartstadium.classes.custom;

import com.example.gecko.smartstadium.classes.Seat;
import com.example.gecko.smartstadium.classes.Tribune;

import java.util.ArrayList;

public class SeatsByTribune {

    private Tribune tribune;
    private ArrayList<Seat> seats;

    public Tribune getTribune() {
        return tribune;
    }

    public void setTribune(Tribune tribune) {
        this.tribune = tribune;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }
}
