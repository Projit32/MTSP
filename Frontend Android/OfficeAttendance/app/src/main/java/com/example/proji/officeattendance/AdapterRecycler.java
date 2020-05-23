package com.example.proji.officeattendance;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
    FinalTask x;


    public AdapterRecycler(ArrayList<OfficeModel> officeModels,  FinalTask context) {
        this.officeModels = officeModels;
        x=context;
    }

    @NonNull
    @Override
    public AdapterRecycler.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.labourdetails, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecycler.viewholder holder,final int position) {
       holder.officename.setText(officeModels.get(position).getNames());
        final int pos=position;
       holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               for(int i=0;i<x.labourModels.size();i++)
               {
                   x.labourModels.get(i).setStation(officeModels.get(pos).getNames());
               }
               Toast.makeText(x, "Station Selected : "+officeModels.get(pos).getNames(), Toast.LENGTH_SHORT).show();
               if(x.Upload.getVisibility()==View.GONE)
               {
                   x.Upload.setVisibility(View.VISIBLE);
               }
           }
       });


    }

    @Override

    public int getItemCount() {
        return officeModels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView officename;
        ConstraintLayout constraintLayout;

        public viewholder(View itemView) {
            super(itemView);
            officename = itemView.findViewById(R.id.Name);
            constraintLayout=itemView.findViewById(R.id.layout);
        }
    }
}
