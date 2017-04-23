package com.example.gecko.smartstadium.classes;

import com.google.gson.annotations.SerializedName;

public class Credentials {

    @SerializedName("id")
    private String id;
    @SerializedName("password")
    private String password;

    public Credentials(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
