package com.example.gecko.smartstadium.classes;

import com.google.gson.annotations.SerializedName;

public class Tribune {

    @SerializedName("nfc")
    private String NFC;
    @SerializedName("places")
    private int places;
    @SerializedName("localisation")
    private String localisation;
    @SerializedName("description")
    private String description;

    public String getNFC() {
        return NFC;
    }

    public void setNFC(String NFC) {
        this.NFC = NFC;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
