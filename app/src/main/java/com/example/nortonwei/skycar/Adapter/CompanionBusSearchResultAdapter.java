package com.example.nortonwei.skycar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nortonwei.skycar.CompanionBusOrderDetailActivity;
import com.example.nortonwei.skycar.CompanionBusResultActivity;
import com.example.nortonwei.skycar.Model.UserComment;
import com.example.nortonwei.skycar.R;

import java.util.List;


public class CompanionBusSearchResultAdapter extends RecyclerView.Adapter<CompanionBusSearchResultAdapter.CompanionBusSearchResultViewHolder> {
    private Context context;
//    private List<UserComment> userCommentList;

    public CompanionBusSearchResultAdapter(Context context, List<UserComment> userCommentList) {
        this.context = context;
//        this.userCommentList = userCommentList;
    }

    public CompanionBusSearchResultAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CompanionBusSearchResultAdapter.CompanionBusSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_bus_list, null);

        return new CompanionBusSearchResultAdapter.CompanionBusSearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanionBusSearchResultViewHolder companionBusSearchResultViewHolder, int i) {
        companionBusSearchResultViewHolder.joinButton.setOnClickListener(view -> {
            Intent intent = CompanionBusOrderDetailActivity.makeIntent(context);
            context.startActivity(intent);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CompanionBusSearchResultViewHolder extends RecyclerView.ViewHolder {
        Button joinButton;

        public CompanionBusSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            joinButton = (Button) itemView.findViewById(R.id.join_button);
        }
    }
}
