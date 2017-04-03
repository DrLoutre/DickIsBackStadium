package com.example.gecko.smartstadium.events;

import com.example.gecko.smartstadium.classes.Athletic;

public class LoginEvent {

    private Athletic athletic;

    public LoginEvent(Athletic athletic) {
        this.athletic = athletic;
    }

    public Athletic getAthletic() {
        return athletic;
    }

    public void setAthletic(Athletic athletic) {
        this.athletic = athletic;
    }
}
