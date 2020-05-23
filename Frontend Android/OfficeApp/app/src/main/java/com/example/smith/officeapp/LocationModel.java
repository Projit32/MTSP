package com.example.smith.officeapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LocationModel implements Parcelable {

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

    protected LocationModel(Parcel in) {
        Routename = in.readString();
        Route = in.readString();
        lname = in.readString();
        currloc = in.readInt();
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Routename);
        parcel.writeString(Route);
        parcel.writeString(lname);
        parcel.writeInt(currloc);
    }
}
