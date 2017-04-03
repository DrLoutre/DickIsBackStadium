package com.example.gecko.smartstadium.classes;

public class Spectator {

    private int id;
    private String lastName;
    private String firstName;
    private String tribuneNFC;
    private int id_Match;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTribuneNFC() {
        return tribuneNFC;
    }

    public void setTribuneNFC(String tribuneNFC) {
        this.tribuneNFC = tribuneNFC;
    }

    public int getId_Match() {
        return id_Match;
    }

    public void setId_Match(int id_Match) {
        this.id_Match = id_Match;
    }
}
