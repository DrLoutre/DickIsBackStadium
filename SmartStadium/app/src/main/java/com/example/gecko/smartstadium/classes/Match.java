package com.example.gecko.smartstadium.classes;

import com.google.gson.annotations.SerializedName;

public class Match {

    @SerializedName("id")
    private int ID;
    @SerializedName("teamID1")
    private int id_team_1;
    @SerializedName("teamID2")
    private int id_team_2;
    @SerializedName("goals1")
    private int goals_1;
    @SerializedName("goals2")
    private int goals_2;
    @SerializedName("date")
    private String date;
    @SerializedName("ended")
    private boolean ended;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getId_team_1() {
        return id_team_1;
    }

    public void setId_team_1(int id_team_1) {
        this.id_team_1 = id_team_1;
    }

    public int getId_team_2() {
        return id_team_2;
    }

    public void setId_team_2(int id_team_2) {
        this.id_team_2 = id_team_2;
    }

    public int getGoals_1() {
        return goals_1;
    }

    public void setGoals_1(int goals_1) {
        this.goals_1 = goals_1;
    }

    public int getGoals_2() {
        return goals_2;
    }

    public void setGoals_2(int goals_2) {
        this.goals_2 = goals_2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }
}
