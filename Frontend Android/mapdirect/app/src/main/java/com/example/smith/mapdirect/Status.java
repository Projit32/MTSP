package com.example.smith.mapdirect;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("name")
    String LName;
    @SerializedName("lat")
    Double lat;
    @SerializedName("lng")
    Double lng;
    @SerializedName("status")
    String status;

    public Status(String name, Double lat, Double lng, String status) {
        LName = name;
        this.lat = lat;
        this.lng = lng;
        this.status = status;

    }


    public String getLName() {
        return LName;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getStatus() {
        return status;
    }

}
