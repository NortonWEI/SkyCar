package com.example.nortonwei.skycar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nortonwei.skycar.R;

import java.util.ArrayList;

public class CharteredTravelAdapter extends RecyclerView.Adapter<CharteredTravelAdapter.CardViewHolder> {

    private ArrayList<String> dataList;
    private Context context;

    public CharteredTravelAdapter(ArrayList<String> dataList, Context context) {
        super();
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public CharteredTravelAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v =  inflater.inflate(R.layout.layout_image_card_item, parent,false);
        CardViewHolder cardViewHolder = new CardViewHolder(v);
        cardViewHolder.setIsRecyclable(true);

        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int i) {
//        int resId = context.getResources().getIdentifier("img_"+i, "drawable", context.getPackageName());
//        if(resId!=0) {
//            cardViewHolder.itemImg.setImageResource(resId);
//        }
        if (i%2 == 0) {
            cardViewHolder.itemImg.setImageResource(R.drawable.main_banner);
        } else {
            cardViewHolder.itemImg.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_menu_report_image));
        }
//        if(mOnItemActionListener!=null){
        cardViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据
//                mOnItemActionListener.onItemClickListener(v,cardViewHolder.getPosition());
                Toast.makeText(context, "-单击-"+cardViewHolder.getPosition(), Toast.LENGTH_SHORT).show();

            }
        });
//        }
    }

    @Override
    public int getItemCount() {
//        return dataList.size();
        return 12;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImg;

        public CardViewHolder(View itemView) {
            super(itemView);
            itemImg = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }
}
