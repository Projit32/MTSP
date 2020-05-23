package com.example.smith.tsp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Location implements Parcelable{
    @SerializedName("station")
    String Tablename;
    @SerializedName("location")
    String Lnmae;
    @SerializedName("lat")
    String Llat;
    @SerializedName("lng")
    String Llng;

    public Location(String lnmae, String llat, String llng, String tablename) {
        Lnmae = lnmae;
        Llat = llat;
        Llng = llng;
        Tablename=tablename;
    }

    protected Location(Parcel in) {
        Lnmae = in.readString();
        Llat = in.readString();
        Llng = in.readString();
        Tablename= in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getLnmae() {
        return Lnmae;
    }

    public String getLlat() {
        return Llat;
    }

    public String getLlng() {
        return Llng;
    }

    public String getTablename() {
        return Tablename;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Lnmae);
        parcel.writeString(Llat);
        parcel.writeString(Llng);
        parcel.writeString(Tablename);
    }
}
