package com.example.gecko.smartstadium.classes;

import com.google.gson.annotations.SerializedName;

public class Race {

    @SerializedName("id")
    private int id;
    @SerializedName("nfc")
    private String NFC;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNFC() {
        return NFC;
    }

    public void setNFC(String NFC) {
        this.NFC = NFC;
    }
}
