package com.example.nortonwei.skycar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.R;

public class OrderReserveCarAdapter extends RecyclerView.Adapter<OrderReserveCarAdapter.ReserveCarViewHolder> {
    Context context;

    public OrderReserveCarAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ReserveCarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_bus_list, null);

        return new ReserveCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveCarViewHolder reserveCarViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ReserveCarViewHolder extends RecyclerView.ViewHolder {

        public ReserveCarViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
