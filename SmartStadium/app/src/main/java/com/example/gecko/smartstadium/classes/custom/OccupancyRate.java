package com.example.gecko.smartstadium.classes.custom;

import com.google.gson.annotations.SerializedName;

public class OccupancyRate {

    @SerializedName("free")
    private double free;
    @SerializedName("occupied")
    private double occupied;

    public double getFree() {
        return free;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getOccupied() {
        return occupied;
    }

    public void setOccupied(double occupied) {
        this.occupied = occupied;
    }
}
