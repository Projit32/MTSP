package com.example.smith.officeapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class AttendanceDetails extends AppCompatActivity {
    ArrayList<AttendanceModel> attendanceModels;
    RecyclerView recyclerView;
    String baseUrl;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);
        context=this;
        attendanceModels=getIntent().getExtras().getParcelableArrayList("AttendanceList");
        //Toast.makeText(context, "Size :"+attendanceModels.size(), Toast.LENGTH_SHORT).show();
        baseUrl=getIntent().getExtras().getString("url");
        recyclerView=findViewById(R.id.detailsview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Attendance_Adapter(context,attendanceModels,baseUrl));
    }
}
