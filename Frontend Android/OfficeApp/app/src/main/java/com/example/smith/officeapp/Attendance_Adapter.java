package com.example.smith.officeapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Attendance_Adapter extends RecyclerView.Adapter<Attendance_Adapter.AttendanceViewHolder> {
    ArrayList<AttendanceModel> attendance;
     Context context;
     String baseUrl;

    public Attendance_Adapter(Context context, ArrayList<AttendanceModel> attendanceModels,String baseUrl) {
        attendance=attendanceModels;
        this.context=context;
        this.baseUrl=baseUrl;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.attendance,parent,false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, final int position) {
        holder.labourname.setText(attendance.get(position).getLabourName());
        holder.routeassign.setText(attendance.get(position).getRoute());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,Displaydetails.class);
                intent.putExtra("attendancedetails",attendance.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return attendance.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder
{
    TextView labourname,routeassign;
    RelativeLayout relativeLayout;

    public AttendanceViewHolder(View itemView) {
        super(itemView);
        labourname=itemView.findViewById(R.id.labourname);
        routeassign=itemView.findViewById(R.id.routename);
        relativeLayout=itemView.findViewById(R.id.attendancerecycler);
    }
}
}
