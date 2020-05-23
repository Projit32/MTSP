package com.example.smith.mapdirect;

import com.google.gson.annotations.SerializedName;


public class LocationModel {
@SerializedName("Name")
    String Routename;
@SerializedName("Route")
    String Route;
@SerializedName("Lname")
    String lname;
@SerializedName("Current")
    int currloc;

    public LocationModel(String routename, String route, String lname, int currloc) {
        Routename = routename;
        Route = route;
        this.lname = lname;
        this.currloc = currloc;
    }

    public String getRoutename() {
        return Routename;
    }

    public String getRoute() {
        return Route;
    }

    public String getLname() {
        return lname;
    }

    public int getCurrloc() {
        return currloc;
    }
}
