package com.example.proji.officeattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText LabourId;
    TextView LabourCount;
    Button Submit;
    String BaseURL="http://YOUR_DOMAIN_NAME/";
    RecyclerView recyclerView;
    ArrayList<LabourModel> labourModels= new ArrayList<>();
    RequestInterface requestInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        requestInterface=new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RequestInterface.class);
        recyclerView=findViewById(R.id.RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LabourId= findViewById(R.id.RollNumber);
        LabourCount=findViewById(R.id.PresentCount);
        LabourCount.setText("Present :0");
        Submit=findViewById(R.id.Proceed);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this,FinalTask.class);
                intent.putExtra("Present",labourModels);
                intent.putExtra("URL",BaseURL);
                startActivity(intent);
            }
        });
        LabourId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0)
                {
                    Check(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public ArrayList<LabourModel> getLabourModels() {
        return labourModels;
    }

    public void Check(String s)
    {
        Call<ArrayList<LabourModel>> call = requestInterface.GetLabours(s);


        call.enqueue(new Callback<ArrayList<LabourModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LabourModel>> call, Response<ArrayList<LabourModel>> response) {
                //Toast.makeText(MainActivity.this, "size "+response.body().size(), Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(new AttendaceShowAdapter(MainActivity.this,response.body(),false));

            }

            @Override
            public void onFailure(Call<ArrayList<LabourModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
