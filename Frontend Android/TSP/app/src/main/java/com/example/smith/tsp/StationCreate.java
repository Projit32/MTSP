package com.example.smith.tsp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smith.tsp.models.ResponseJson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationCreate extends AppCompatActivity {
    EditText StationName;
    Button submit;
    String BaseURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_create);
        StationName= findViewById(R.id.Station_name);
        BaseURL=getIntent().getExtras().getString("url1");
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StationName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(StationCreate.this, "Enter Station Name...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    upload(StationName.getText().toString().toLowerCase());
                }
            }
        });
    }
    public void upload(final String Stationname)
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestInterface requestInterface= retrofit.create(RequestInterface.class);

        Call<ResponseJson> call = requestInterface.CreateTables(Stationname);
        call.enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(Call<ResponseJson> call, Response<ResponseJson> response) {
                try {
                    ResponseJson responseJson = response.body();
                    // Toast.makeText(StationCreate.this,responseJson.getMessage() , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StationCreate.this, MapActivity.class);
                    intent.putExtra("url1", BaseURL);
                    intent.putExtra("station", Stationname);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    Toast.makeText(StationCreate.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseJson> call, Throwable t) {
                Toast.makeText(StationCreate.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
