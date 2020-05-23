package com.example.smith.tsp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smith.tsp.models.Location;
import com.example.smith.tsp.models.ResponseJson;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Show_locations extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<LatLng> latLngs= new ArrayList<>();
    ArrayList<Location> loc= new ArrayList<>();
    ArrayList<String> addresses=new ArrayList<>();
    Button pro;
    String baseUrl,station;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_locations);
        pro=findViewById(R.id.proceed);
        Bundle bundle=getIntent().getExtras();
        baseUrl=bundle.getString("url2");
        station=bundle.getString("station");
        addresses=bundle.getStringArrayList("mAddresses");

        for(int i =0; i < addresses.size();i++)
        {
            addresses.set(i,addresses.get(i).replaceAll("[^a-zA-Z0-9]", " "));
        }

        rv=findViewById(R.id.rv1);
        try {
            latLngs = bundle.getParcelableArrayList("places");
            if (latLngs.size() > 0) {
                rv.setLayoutManager(new LinearLayoutManager(this));
                rv.setAdapter(new Adapter_Rec(this, latLngs,addresses));
            } else
                Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();

            pro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goTo();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Up(View view)
    {

        loc.clear();
        int i=0;
        for(LatLng l : latLngs)
        {
            Location location = new Location(addresses.get(i),
                    Double.toString(l.latitude),Double.toString(l.longitude),station);
            loc.add(location);
            i++;
        }
        try {
            //Toast.makeText(this, Integer.toString(loc.size()), Toast.LENGTH_SHORT).show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final RequestInterface requestInterface = retrofit.create(RequestInterface.class);
            Call<ResponseJson> call = requestInterface.SendJson(loc);
            call.enqueue(new Callback<ResponseJson>() {
                @Override
                public void onResponse(Call<ResponseJson> call, Response<ResponseJson> response) {
                    Toast.makeText(Show_locations.this, response.body().getMessage()+" "+
                            response.body().getCode(), Toast.LENGTH_SHORT).show();
                    pro.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<ResponseJson> call, Throwable t) {
                    Toast.makeText(Show_locations.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    public void goTo()
    {
        Intent intent = new Intent(Show_locations.this,DistanceFetch.class);
        intent.putExtra("url3",baseUrl);
        intent.putExtra("position",loc);
        intent.putExtra("station",station);
        startActivity(intent);


    }
}
