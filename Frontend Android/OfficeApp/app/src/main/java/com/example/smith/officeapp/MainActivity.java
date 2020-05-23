package com.example.smith.officeapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    String BaseURL="http://YOUR_DOMAIN_NAME/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context=this;
        rv=findViewById(R.id.rview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Call<ArrayList<OfficeModel>> call=new RetrofitUtility(BaseURL).getRetrofitInterface().getOffices();
        call.enqueue(new Callback<ArrayList<OfficeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<OfficeModel>> call, Response<ArrayList<OfficeModel>> response) {
                //Toast.makeText(MainActivity.this, Integer.toString(response.body().size()), Toast.LENGTH_SHORT).show();
                rv.setAdapter(new AdapterRecycler(response.body(),BaseURL,context));
            }

            @Override
            public void onFailure(Call<ArrayList<OfficeModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
