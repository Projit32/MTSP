package com.example.smith.tsp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Adapter_Rec extends RecyclerView.Adapter<Adapter_Rec.viewholder>{

    Show_locations x;
    ArrayList<LatLng> coord;
    ArrayList<String> mAddresses;
    public Adapter_Rec(Show_locations x, ArrayList<LatLng> y,ArrayList<String> z)
    {
        this.x=x;
        coord=y;
        mAddresses=z;
    }
    @NonNull
    @Override
    public Adapter_Rec.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lf=LayoutInflater.from(parent.getContext());
                View view=lf.inflate(R.layout.latilongi,parent,false);
                return new viewholder(view);//fill this after creating show_locations acitivity
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Rec.viewholder holder, int position) {

            holder.lat.setText("LAT: "+Double.toString(coord.get(position).latitude));
            holder.lon.setText("LNG: "+Double.toString(coord.get(position).longitude));
            holder.ads.setText("ADDRESS: "+mAddresses.get(position));
    }

    @Override
    public int getItemCount() {
        return coord.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView lat,lon,ads;


        public viewholder(View itemView) {
            super(itemView);
            lat=itemView.findViewById((R.id.lat));
            lon=itemView.findViewById((R.id.lon));
            ads=itemView.findViewById(R.id.ads);
        }
    }
}