package com.example.nortonwei.skycar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.R;

public class OrderCharteredTravelAdapter extends RecyclerView.Adapter<OrderCharteredTravelAdapter.CharteredTravelViewHolder> {
    Context context;

    public OrderCharteredTravelAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CharteredTravelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_bus_list, null);

        return new CharteredTravelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharteredTravelViewHolder charteredTravelViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CharteredTravelViewHolder extends RecyclerView.ViewHolder {

        public CharteredTravelViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
