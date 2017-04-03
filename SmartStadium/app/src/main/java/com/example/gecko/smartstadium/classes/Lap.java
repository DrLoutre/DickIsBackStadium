package com.example.gecko.smartstadium.classes;

public class Lap {

    private int ID;
    private String temp;
    private int temp_ms;
    private int id_race;

    public Lap(){

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getTemp_ms() {
        return temp_ms;
    }

    public void setTemp_ms(int temp_ms) {
        this.temp_ms = temp_ms;
    }

    public int getId_race() {
        return id_race;
    }

    public void setId_race(int id_race) {
        this.id_race = id_race;
    }
}
