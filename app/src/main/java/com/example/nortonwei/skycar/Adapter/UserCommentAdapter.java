package com.example.nortonwei.skycar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.Model.UserComment;
import com.example.nortonwei.skycar.R;

import java.util.List;

import carbon.widget.RecyclerView;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.UserCommentViewHolder> {
    private Context context;
    private List<UserComment> userCommentList;

    public UserCommentAdapter(Context context, List<UserComment> userCommentList) {
        this.context = context;
        this.userCommentList = userCommentList;
    }

    @NonNull
    @Override
    public UserCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_comment_list_layout, null);
        return new UserCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommentViewHolder userCommentViewHolder, int i) {
//        UserComment userComment = userCommentList.get(i);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class UserCommentViewHolder extends RecyclerView.ViewHolder {

        public UserCommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}