package com.example.proji.officeattendance;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FinalTask extends AppCompatActivity {
    Button Upload;
    RecyclerView recyclerView;
    String BaseUrl;
    TextView Result;
    ArrayList<LabourModel> labourModels= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_task);

        BaseUrl=getIntent().getExtras().getString("URL");
        labourModels=getIntent().getExtras().getParcelableArrayList("Present");


        Upload=findViewById(R.id.Upload);

        Result = findViewById(R.id.Result);
        recyclerView=findViewById(R.id.StationNames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<ArrayList<OfficeModel>>call = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RequestInterface.class)
                .getOffices();
        call.enqueue(new Callback<ArrayList<OfficeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<OfficeModel>> call, Response<ArrayList<OfficeModel>> response) {
                recyclerView.setAdapter(new AdapterRecycler(response.body(),FinalTask.this));
            }

            @Override
            public void onFailure(Call<ArrayList<OfficeModel>> call, Throwable t) {
                Toast.makeText(FinalTask.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(FinalTask.this, labourModels.get(0).station, Toast.LENGTH_SHORT).show();
                 Call<ResponseMessage> call=  new Retrofit.Builder()
                         .baseUrl(BaseUrl)
                         .addConverterFactory(GsonConverterFactory.create())
                         .build()
                         .create(RequestInterface.class).PutAttendance(labourModels);
              call.enqueue(new Callback<ResponseMessage>() {
                  @Override
                  public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                     // Toast.makeText(FinalTask.this, response.body().getMessages()+" "+response.body().getCode(), Toast.LENGTH_SHORT).show();
                      if(response.body().getCode()>0)
                      {
                          Upload.setVisibility(View.GONE);
                          recyclerView.setVisibility(View.GONE);
                          Result.setVisibility(View.VISIBLE);
                      }
                  }

                  @Override
                  public void onFailure(Call<ResponseMessage> call, Throwable t) {
                      Toast.makeText(FinalTask.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                  }
              });
            }
        });
    }
}
