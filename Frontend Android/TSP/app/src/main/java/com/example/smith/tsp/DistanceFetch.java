package com.example.smith.tsp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smith.tsp.models.DistanceResponse;
import com.example.smith.tsp.models.Element;
import com.example.smith.tsp.models.LocDist;
import com.example.smith.tsp.models.Location;
import com.example.smith.tsp.models.ResponseJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DistanceFetch extends AppCompatActivity {
    private TextView tvTravelInfo;
    private ArrayList<Location> locations=new ArrayList<>();
    String baseUrl;
    String xname,yname,Station;
    int crt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_fetch);

        baseUrl=getIntent().getExtras().getString("url3");
        Station=getIntent().getExtras().getString("station");
        tvTravelInfo = findViewById(R.id.tv_travel_info);
        tvTravelInfo.setText("Click to start calculating distances");
       tvTravelInfo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {

                   locations = (ArrayList<Location>) getIntent().getSerializableExtra("position");
                   getDistanceInfo();

               }
               catch(Exception e){
                   Toast.makeText(DistanceFetch.this, e.toString(), Toast.LENGTH_SHORT).show();
               }
           }
       });



    }



    private android.location.Location getCurrentLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) return null;

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        android.location.Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return location;
    }




    private void getDistanceInfo() {
        // http://maps.googleapis.com/maps/api/distancematrix/json?destinations=Atlanta,GA|New+York,NY&origins=Orlando,FL&units=imperial

        for (final Location l : locations) {
            for ( final Location m : locations) {
                if (!(l.getLnmae().equalsIgnoreCase(m.getLnmae()))) {


                    Map<String, String> mapQuery = new HashMap<>();
                    mapQuery.put("units", "metric");
                    mapQuery.put("mode", "walking");
                    mapQuery.put("origins", l.getLlat() + "," + l.getLlng());
                    mapQuery.put("destinations", m.getLlat() + "," + m.getLlng());
                    mapQuery.put("key", "YOUR_API_KEY");


                    RequestInterface client = RestUtil.getInstance().getRetrofit().create(RequestInterface.class);

                    Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
                    call.enqueue(new Callback<DistanceResponse>() {
                        @Override
                        public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                            if (response.body() != null &&
                                    response.body().getRows() != null &&
                                    response.body().getRows().size() > 0 &&
                                    response.body().getRows().get(0) != null &&
                                    response.body().getRows().get(0).getElements() != null &&
                                    response.body().getRows().get(0).getElements().size() > 0 &&
                                    response.body().getRows().get(0).getElements().get(0) != null &&
                                    response.body().getRows().get(0).getElements().get(0).getDistance() != null &&
                                    response.body().getRows().get(0).getElements().get(0).getDuration() != null) {

                                crt++;
                                xname=l.getLnmae();
                                yname=m.getLnmae();

                                Element element = response.body().getRows().get(0).getElements().get(0);
                               // Toast.makeText(DistanceFetch.this, xname+"-->"+yname+" : "+element.getDistance().getText(), Toast.LENGTH_SHORT).show();
                                tvTravelInfo.setText(xname+"->"+yname+" : "+element.getDistance().getText());

                                if(crt%100 ==0)
                                {
                                    tvTravelInfo.setText("Preparing next batch, please wait......");
                                    try {
                                        Thread.sleep(2500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                String dist=element.getDistance().getText().replace("km","").trim();
                                showTravelDistance(xname, yname, dist);



                            } else {
                                Toast.makeText(DistanceFetch.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<DistanceResponse> call, Throwable t) {
                            Toast.makeText(DistanceFetch.this,t.getMessage() , Toast.LENGTH_SHORT).show();
                            tvTravelInfo.setText("Unable to fetch API :"+t.getMessage());
                        }
                    });

                }
            }

        }
    }

    private void showTravelDistance(final String From, final String To, final String Distance) {

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LocDist lc = new LocDist(From, To, Distance,Station);
            RequestInterface requestInterface = retrofit.create(RequestInterface.class);
            Call<ResponseJson> call2 = requestInterface.SendJson(lc);
            call2.enqueue(new Callback<ResponseJson>() {
                @Override
                public void onResponse(Call<ResponseJson> call, Response<ResponseJson> response) {

                    //tvTravelInfo.setText(response.body().getMessage());
                    tvTravelInfo.setText("All data Submitted :"+ Integer.toString(crt));

                }

                @Override
                public void onFailure(Call<ResponseJson> call, Throwable t) {
                    Toast.makeText(DistanceFetch.this, "Internal Server Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }



    }



}
