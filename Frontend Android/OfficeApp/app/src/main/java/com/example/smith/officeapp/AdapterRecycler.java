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

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.viewholder> {

    ArrayList<OfficeModel> officeModels = new ArrayList<>();
    String baseUrl;
    Context x;


    public AdapterRecycler(ArrayList<OfficeModel> officeModels, String Url, Context context) {
        this.officeModels = officeModels;
        baseUrl=Url;
        x=context;
    }

    @NonNull
    @Override
    public AdapterRecycler.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.officename, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecycler.viewholder holder,final int position) {
        holder.officename.setText("Office Name :"+officeModels.get(position).getNames());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestInterface requestInterface=new RetrofitUtility(baseUrl).getRetrofitInterface();
                Call<ArrayList<AttendanceModel>> call=requestInterface.getAttendance(officeModels.get(position).getNames());
                call.enqueue(new Callback<ArrayList<AttendanceModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AttendanceModel>> call, Response<ArrayList<AttendanceModel>> response) {
                        Intent intent= new Intent(x,AttendanceDetails.class);
                        ArrayList<AttendanceModel> attendance=response.body();
                        intent.putExtra("AttendanceList",attendance);
                        intent.putExtra("url",baseUrl);
                        x.startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<ArrayList<AttendanceModel>> call, Throwable t) {
                        Toast.makeText(x, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override

    public int getItemCount() {
        return officeModels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView officename;
        RelativeLayout relativeLayout;


        public viewholder(View itemView) {
            super(itemView);
            officename = itemView.findViewById(R.id.offname);
            relativeLayout=itemView.findViewById(R.id.officerecycler);
        }
    }
}
