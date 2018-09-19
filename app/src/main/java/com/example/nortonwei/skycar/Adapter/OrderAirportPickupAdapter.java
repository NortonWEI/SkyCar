package com.example.nortonwei.skycar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.R;

public class OrderAirportPickupAdapter extends RecyclerView.Adapter<OrderAirportPickupAdapter.AirportPickupViewHolder> {
    Context context;

    public OrderAirportPickupAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AirportPickupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_bus_list, null);

        return new AirportPickupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AirportPickupViewHolder airportPickupViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class AirportPickupViewHolder extends RecyclerView.ViewHolder {

        public AirportPickupViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
