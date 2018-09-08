package com.example.nortonwei.skycar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.CharteredTravelDetailActivity;
import com.example.nortonwei.skycar.R;

public class JourneyRecommendationAdapter extends RecyclerView.Adapter<JourneyRecommendationAdapter.RecommendationViewHolder> {
    Context context;

    public JourneyRecommendationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_journey_recommendation_item, null);
        view.setOnClickListener(v -> {
//            Intent intent = CharteredTravelDetailActivity.makeIntent(context);
//            context.startActivity(intent);
//            Activity activity = (Activity) context;
//            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder recommendationViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class RecommendationViewHolder extends RecyclerView.ViewHolder {

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
