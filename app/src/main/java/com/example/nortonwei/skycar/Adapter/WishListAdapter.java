package com.example.nortonwei.skycar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.nortonwei.skycar.CharteredTravelDetailActivity;
import com.example.nortonwei.skycar.CharteredTravelListActivity;
import com.example.nortonwei.skycar.R;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {
    private Context context;
//    private List<UserComment> userCommentList;

    public WishListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_wish_list_item, null);
        view.setOnClickListener(v -> {
            Intent intent = CharteredTravelDetailActivity.makeIntent(context);
            context.startActivity(intent);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        return new WishListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder wishListViewHolder, int i) {
        if (context.getClass().getSimpleName().equals("WishListActivity")) {
            wishListViewHolder.collectionButton.setVisibility(View.INVISIBLE);
        }

        wishListViewHolder.collectionButton.setOnClickListener(view -> {
            if (wishListViewHolder.collectionButton.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.collection_unselected).getConstantState())) {
                wishListViewHolder.collectionButton.setImageResource(R.drawable.collection_selected);
            } else if (wishListViewHolder.collectionButton.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.collection_selected).getConstantState())) {
                wishListViewHolder.collectionButton.setImageResource(R.drawable.collection_unselected);
            }
        });


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class WishListViewHolder extends RecyclerView.ViewHolder {
        ImageButton collectionButton;

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            collectionButton = (ImageButton) itemView.findViewById(R.id.collection_imageButton);
        }
    }
}
