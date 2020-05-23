package com.example.smith.officeapp;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("TSP%20APP/OfficeDetails.php")
    public Call<ArrayList<OfficeModel>>  getOffices();

    @FormUrlEncoded
    @POST("TSP%20APP/FetchAttendance.php")
    public Call<ArrayList<AttendanceModel>> getAttendance(@Field("stname") String name);


}
