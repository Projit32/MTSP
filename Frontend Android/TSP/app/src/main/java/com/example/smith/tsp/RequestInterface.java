package com.example.smith.tsp;

import com.example.smith.tsp.models.DistanceResponse;
import com.example.smith.tsp.models.LocDist;
import com.example.smith.tsp.models.Location;
import com.example.smith.tsp.models.ResponseJson;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;



public interface RequestInterface {
    @Headers("Content-Type: application/json")
    @POST("TSP%20APP/AddLatLong.php")
    public Call<ResponseJson> SendJson(@Body ArrayList<Location> loc);

    @GET("maps/api/distancematrix/json")
    Call<DistanceResponse> getDistanceInfo(
            @QueryMap Map<String, String> parameters
    );
    @Headers("Content-Type: application/json")
    @POST("TSP%20APP/AddDistances.php")
    Call<ResponseJson> SendJson(@Body LocDist lc);

    @FormUrlEncoded
    @POST("TSP%20APP/createstation.php")
    Call<ResponseJson> CreateTables(@Field("station")String name);

}
