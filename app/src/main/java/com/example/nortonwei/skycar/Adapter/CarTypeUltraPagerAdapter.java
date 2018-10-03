package com.example.nortonwei.skycar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.nortonwei.skycar.Model.Car;
import com.example.nortonwei.skycar.R;

import java.util.ArrayList;

public class CarTypeUltraPagerAdapter extends PagerAdapter {
    private boolean isMultiScr;
    private Context context;
    private ArrayList<Car> carList = new ArrayList<>();

    public CarTypeUltraPagerAdapter(boolean isMultiScr, Context context, ArrayList<Car> carList) {
        this.isMultiScr = isMultiScr;
        this.context = context;
        this.carList = carList;
    }

    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.listitem_car_type, null);
        ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.car_type_imageView);
        Glide.with(context).load(carList.get(position).getImgUrl()).into(imageView);

        container.addView(relativeLayout);
//        linearLayout.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, container.getContext().getResources().getDisplayMetrics());
//        linearLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, container.getContext().getResources().getDisplayMetrics());
        return relativeLayout;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

