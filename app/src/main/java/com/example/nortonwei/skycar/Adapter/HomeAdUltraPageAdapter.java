package com.example.nortonwei.skycar.Adapter;

import android.content.Context;
import android.databinding.adapters.ToolbarBindingAdapter;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nortonwei.skycar.R;

import java.util.ArrayList;

public class HomeAdUltraPageAdapter extends PagerAdapter {
    ArrayList<String> adImageUrlList = new ArrayList<>();
    Context context;

    public HomeAdUltraPageAdapter(ArrayList<String> adImageUrlList, Context context) {
        this.adImageUrlList = adImageUrlList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return adImageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.listitem_ad_banner, null);
        ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.imageView);
        Glide.with(context).load(adImageUrlList.get(position)).into(imageView);
        container.addView(relativeLayout);

        relativeLayout.setOnClickListener(view -> {
            Toast.makeText(context, "Clicked: " + position, Toast.LENGTH_SHORT).show();
        });

        return relativeLayout;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
