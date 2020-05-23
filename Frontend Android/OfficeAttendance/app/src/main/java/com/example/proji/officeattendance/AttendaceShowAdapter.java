package com.example.proji.officeattendance;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendaceShowAdapter extends RecyclerView.Adapter<AttendaceShowAdapter.LabourViewHolder>{
    MainActivity mainActivity;
    ArrayList<LabourModel> labourModels;
    boolean present;

    public AttendaceShowAdapter(MainActivity mainActivity, ArrayList<LabourModel> labourModels, boolean present) {
        this.mainActivity = mainActivity;
        this.labourModels = labourModels;
        this.present = present;
    }

    @NonNull
    @Override
    public LabourViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater lf = LayoutInflater.from(viewGroup.getContext());
        View view=lf.inflate(R.layout.labourdetails,viewGroup,false);
        return new LabourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabourViewHolder labourViewHolder, int i) {
        labourViewHolder.Name.setText(labourModels.get(i).getName());
        labourViewHolder.Id.setText(labourModels.get(i).getId());
        if(present)
        {
            labourViewHolder.constraintLayout.setBackgroundResource(R.drawable.backgroundpresent);
        }
        final int pos=i;
        labourViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.labourModels.add(labourModels.get(pos));
                mainActivity.LabourCount.setText("Present :"+mainActivity.labourModels.size());
                mainActivity.Submit.setVisibility(View.VISIBLE);
                mainActivity. recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
                mainActivity.recyclerView.setAdapter(new AttendaceShowAdapter(mainActivity, mainActivity.getLabourModels(),true));

            }
        });


    }

    @Override
    public int getItemCount() {
        return labourModels.size();
    }

    class LabourViewHolder extends RecyclerView.ViewHolder{

        TextView Name, Id;
        ConstraintLayout constraintLayout;
        public LabourViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Name);
            Id=itemView.findViewById(R.id.Id);
            constraintLayout=itemView.findViewById(R.id.layout);
        }
    }
}
