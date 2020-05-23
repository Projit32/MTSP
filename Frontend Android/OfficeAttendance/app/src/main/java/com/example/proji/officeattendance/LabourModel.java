package com.example.proji.officeattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LabourModel implements Parcelable {

    @SerializedName("Name")
    String Name;
    @SerializedName("ID")
    String Id;
    @SerializedName("stname")
    String station;

    public LabourModel(String name, String id, String station) {
        Name = name;
        Id = id;
        this.station = station;
    }

    protected LabourModel(Parcel in) {
        Name = in.readString();
        Id = in.readString();
        station = in.readString();
    }

    public static final Creator<LabourModel> CREATOR = new Creator<LabourModel>() {
        @Override
        public LabourModel createFromParcel(Parcel in) {
            return new LabourModel(in);
        }

        @Override
        public LabourModel[] newArray(int size) {
            return new LabourModel[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(Id);
        parcel.writeString(station);
    }
}
