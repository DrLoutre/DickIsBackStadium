package com.example.gecko.smartstadium.events;

import com.example.gecko.smartstadium.classes.Match;
import com.example.gecko.smartstadium.classes.custom.MatchNotEnded;

import java.util.ArrayList;

public class MatchsEvent {

    private ArrayList<MatchNotEnded> matchs;

    public MatchsEvent(ArrayList<MatchNotEnded> matchs) {
        this.matchs = matchs;
    }

    public ArrayList<MatchNotEnded> getMatchs() {
        return matchs;
    }

    public void setMatchs(ArrayList<MatchNotEnded> matchs) {
        this.matchs = matchs;
    }
}
