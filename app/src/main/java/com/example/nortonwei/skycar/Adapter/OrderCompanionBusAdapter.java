package com.example.nortonwei.skycar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.R;

public class OrderCompanionBusAdapter extends RecyclerView.Adapter<OrderCompanionBusAdapter.CompanionBusViewHolder> {
    Context context;

    public OrderCompanionBusAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CompanionBusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_bus_list, null);

        return new CompanionBusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanionBusViewHolder companionBusViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CompanionBusViewHolder extends RecyclerView.ViewHolder {

        public CompanionBusViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
