package com.example.gecko.smartstadium.activities;

import com.example.gecko.smartstadium.utils.ClearancesEnum;

/**
 * Created by Quentin Jacquemotte on 04-04-17.
 * Singleton that will hold the personnal information of the user
 */

public class CredentialSingletion {
    private static final CredentialSingletion ourInstance = new CredentialSingletion();
    private int id;
    private String name;
    private ClearancesEnum clearance;

    private CredentialSingletion() {
        id = 6717;
        name = "Quentin";
        clearance = ClearancesEnum.Public;

    }

    public static CredentialSingletion getInstance() {
        return ourInstance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClearancesEnum getClearance() {
        return clearance;
    }

    public void setClearance(ClearancesEnum clearance) {
        this.clearance = clearance;
    }
}