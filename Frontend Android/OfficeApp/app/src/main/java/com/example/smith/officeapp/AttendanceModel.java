package com.example.smith.officeapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AttendanceModel implements Parcelable {

    @SerializedName("id")
    String Labour_ID;
    @SerializedName("name")
    String LabourName;
    @SerializedName("routeassign")
    String Route;
    @SerializedName("lat")
    double lat;
    @SerializedName("lng")
    double lng;
    @SerializedName("status")
    String status;
    @SerializedName("Route")
    String Locations;
    @SerializedName("at")
    Integer current;

    public AttendanceModel(String labour_ID, String labourName, String route, double lat, double lng, String status, String locations, Integer current) {
        Labour_ID = labour_ID;
        LabourName = labourName;
        Route = route;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        Locations = locations;
        this.current = current;
    }

    protected AttendanceModel(Parcel in) {
        Labour_ID = in.readString();
        LabourName = in.readString();
        Route = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        status = in.readString();
        Locations = in.readString();
        if (in.readByte() == 0) {
            current = null;
        } else {
            current = in.readInt();
        }
    }

    public static final Creator<AttendanceModel> CREATOR = new Creator<AttendanceModel>() {
        @Override
        public AttendanceModel createFromParcel(Parcel in) {
            return new AttendanceModel(in);
        }

        @Override
        public AttendanceModel[] newArray(int size) {
            return new AttendanceModel[size];
        }
    };

    public String getLabour_ID() {
        return Labour_ID;
    }

    public String getLabourName() {
        return LabourName;
    }

    public String getRoute() {
        return Route;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getStatus() {
        return status;
    }

    public String getLocations() {
        return Locations;
    }

    public Integer getCurrent() {
        return current;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Labour_ID);
        parcel.writeString(LabourName);
        parcel.writeString(Route);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeString(status);
        parcel.writeString(Locations);
        if (current == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(current);
        }
    }
}
