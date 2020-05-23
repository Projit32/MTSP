package com.example.proji.officeattendance;

import com.google.gson.annotations.SerializedName;


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
