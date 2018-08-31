package com.example.nortonwei.skycar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return new WishListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder wishListViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class WishListViewHolder extends RecyclerView.ViewHolder {

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
