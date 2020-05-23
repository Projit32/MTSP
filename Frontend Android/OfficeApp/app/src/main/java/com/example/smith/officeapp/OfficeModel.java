package com.example.smith.officeapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class OfficeModel {
    @SerializedName("name")
    String names;

    public OfficeModel(String names) {
        this.names = names;
    }

    public String getNames() {
        return names;
    }
}
