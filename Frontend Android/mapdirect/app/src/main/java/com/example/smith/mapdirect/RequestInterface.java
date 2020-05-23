package com.example.smith.mapdirect;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequestInterface {

    @Headers("Content-Type: application/json")
    @POST("TSP%20APP/updatestatus.php")
    public Call<ResponseMessage> UpdateStatus(@Body Status status);


    @FormUrlEncoded
    @POST("TSP%20APP/routeFetch.php")
    Call<LocationModel> getLocation(@Field("username")String Username, @Field("password")String Password);



    @FormUrlEncoded
    @POST("TSP%20APP/UpdateCurrent.php")
    Call<ResponseMessage> UpdateCounter(@Field("name")String name, @Field("count") String count );




}
