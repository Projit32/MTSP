package com.example.smith.tsp.models;

import com.google.gson.annotations.SerializedName;

public class LocDist {
    @SerializedName("from")
    String from;
    @SerializedName("to")
    String to;
    @SerializedName("dist")
    String dist;
    @SerializedName("station")
    String Tablename;



    public LocDist(String origin, String destination, String dist, String Table) {
        this.from = origin;
        this.to = destination;
        this.dist = dist;
        this.Tablename=Table;
    }

    public String getOrigin() {
        return from;
    }

    public String getDestination() {
        return to;
    }

    public String getDist() {
        return dist;
    }

    public String getTablename() {
        return Tablename;
    }
}
