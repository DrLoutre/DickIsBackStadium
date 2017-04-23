package com.example.gecko.smartstadium.classes;

import com.google.gson.annotations.SerializedName;

public class Refreshment {

    @SerializedName("id")
    private int Id;
    @SerializedName("attendance")
    private float Attendance;
    @SerializedName("localisation")
    private String Localisation;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public float getAttendance() {
        return Attendance;
    }

    public void setAttendance(float attendance) {
        Attendance = attendance;
    }

    public String getLocalisation() {
        return Localisation;
    }

    public void setLocalisation(String localisation) {
        Localisation = localisation;
    }
}
