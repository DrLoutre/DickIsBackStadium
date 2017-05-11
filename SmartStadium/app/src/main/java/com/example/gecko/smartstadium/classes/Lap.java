package com.example.gecko.smartstadium.classes;

import com.google.gson.annotations.SerializedName;

public class Lap {

    @SerializedName("id")
    private int ID;
    @SerializedName("year")
    private int year;
    @SerializedName("month")
    private int month;
    @SerializedName("day")
    private int day;
    @SerializedName("tempHour")
    private int temp_hour;
    @SerializedName("tempMin")
    private int temp_min;
    @SerializedName("tempSec")
    private int temp_sec;
    @SerializedName("tempMs")
    private int temp_ms;
    @SerializedName("isBeginning")
    private boolean isBeginning;
    @SerializedName("idRace")
    private int id_race;

    public Lap(){

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTemp_hour() {
        return temp_hour;
    }

    public void setTemp_hour(int temp_hour) {
        this.temp_hour = temp_hour;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(int temp_min) {
        this.temp_min = temp_min;
    }

    public int getTemp_sec() {
        return temp_sec;
    }

    public void setTemp_sec(int temp_sec) {
        this.temp_sec = temp_sec;
    }

    public int getTemp_ms() {
        return temp_ms;
    }

    public void setTemp_ms(int temp_ms) {
        this.temp_ms = temp_ms;
    }

    public boolean isBeginning() {
        return isBeginning;
    }

    public void setBeginning(boolean beginning) {
        isBeginning = beginning;
    }

    public int getId_race() {
        return id_race;
    }

    public void setId_race(int id_race) {
        this.id_race = id_race;
    }
}
