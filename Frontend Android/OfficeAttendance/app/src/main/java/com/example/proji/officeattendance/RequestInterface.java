package com.example.proji.officeattendance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequestInterface {
    @FormUrlEncoded
    @POST("TSP%20APP/FetchLabour.php")
    public Call<ArrayList<LabourModel>> GetLabours(@Field("id")String ID);


    @Headers("Content-Type: application/json")
    @POST("TSP%20APP/AddAttendance.php")
    public Call<ResponseMessage> PutAttendance(@Body ArrayList<LabourModel> Present);

    @POST("TSP%20APP/OfficeDetails.php")
    public Call<ArrayList<OfficeModel>>  getOffices();

    @FormUrlEncoded
    @POST("TSP%20APP/TSP%20APP/TrigerScript.php")
    public Call<ResponseMessage>  TriggerMTSP(@Field("name")String Station);
}
