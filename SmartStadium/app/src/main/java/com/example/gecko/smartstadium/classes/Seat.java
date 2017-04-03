package com.example.gecko.smartstadium.classes;

public class Seat {

    private int id;
    private String tribuneNFC;
    private boolean occupied;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTribuneNFC() {
        return tribuneNFC;
    }

    public void setTribuneNFC(String tribuneNFC) {
        this.tribuneNFC = tribuneNFC;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
