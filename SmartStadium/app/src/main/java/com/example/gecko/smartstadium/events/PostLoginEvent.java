package com.example.gecko.smartstadium.events;

import com.example.gecko.smartstadium.classes.Credentials;

public class PostLoginEvent {

    private Credentials credentials;

    public PostLoginEvent(Credentials credentials) {

    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
