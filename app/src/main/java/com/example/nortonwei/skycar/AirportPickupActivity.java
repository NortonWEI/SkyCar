package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.nortonwei.skycar.Adapter.UltraPagerAdapter;
import com.tmall.ultraviewpager.UltraViewPager;

public class AirportPickupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiport_pickup);

        setUpActionBar();
        setUpButton();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionBar() {
        HomeActivity.setUpActionBar(this, getString(R.string.airport_pick_up));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpButton() {
        Button pickUpButton = (Button) findViewById(R.id.pick_up_button);
        Button dropOffButton = (Button) findViewById(R.id.drop_off_button);
//        Button roundTripButton = (Button) findViewById(R.id.round_trip_button);

        pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_selected), null,null);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.pickup_dropoff_linear);
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View useCarInfoView = vi.inflate(R.layout.use_car_info_layout, linearLayout, false);
        TextView arriveTextView = (TextView) useCarInfoView.findViewById(R.id.arrive_textView);
        EditText arriveEditText = (EditText) useCarInfoView.findViewById(R.id.arrive_editText);
        EditText useCarTimeEditText = (EditText) useCarInfoView.findViewById(R.id.use_car_time_editText);
        linearLayout.addView(useCarInfoView,1);

        SearchView searchView = (SearchView) useCarInfoView.findViewById(R.id.pickup_dropoff_searchView);

        View carTypeView = vi.inflate(R.layout.choose_car_type_layout, linearLayout, false);
        linearLayout.addView(carTypeView,2);

        UltraViewPager ultraViewPager = (UltraViewPager) carTypeView.findViewById(R.id.car_type_ultraViewPager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        PagerAdapter adapter = new UltraPagerAdapter(true);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMultiScreen(0.5f);
        ultraViewPager.setItemRatio(1.0f);
        ultraViewPager.setRatio(2.0f);
        ultraViewPager.setMaxHeight(800);
        ultraViewPager.setAutoMeasureHeight(true);
        UltraViewPager.Orientation gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;

        pickUpButton.setOnClickListener(view -> {
            if (pickUpButton.getCompoundDrawables()[1].getConstantState().equals(getResources().getDrawable(R.drawable.pickup_unselected).getConstantState())) {
                pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_selected), null,null);
                dropOffButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.dropoff_unselected), null,null);
//                roundTripButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.round_unselected), null,null);

                linearLayout.removeView(useCarInfoView);
                linearLayout.removeView(carTypeView);
                arriveTextView.setText(getString(R.string.arrive));
                arriveEditText.setHint(R.string.please_input_arrive);
                useCarTimeEditText.setHint(R.string.use_car_time);
                linearLayout.addView(useCarInfoView,1);
                linearLayout.addView(carTypeView,2);
            }
        });

        dropOffButton.setOnClickListener(view -> {
            if (dropOffButton.getCompoundDrawables()[1].getConstantState().equals(getResources().getDrawable(R.drawable.dropoff_unselected).getConstantState())) {
                pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_unselected), null,null);
                dropOffButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.dropoff_selected), null,null);
//                roundTripButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.round_unselected), null,null);

                linearLayout.removeView(useCarInfoView);
                linearLayout.removeView(carTypeView);
                arriveTextView.setText(getString(R.string.depart));
                arriveEditText.setHint(R.string.please_input_depart);
                useCarTimeEditText.setHint(R.string.depart_time);
                linearLayout.addView(useCarInfoView,1);
                linearLayout.addView(carTypeView,2);
            }
        });

//        roundTripButton.setOnClickListener(view -> {
//            if (roundTripButton.getCompoundDrawables()[1].getConstantState().equals(getResources().getDrawable(R.drawable.round_unselected).getConstantState())) {
//                pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_unselected), null,null);
//                dropOffButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.dropoff_unselected), null,null);
//                roundTripButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.round_selected), null,null);
//
//                linearLayout.removeView(useCarInfoView);
//                linearLayout.removeView(carTypeView);
//            }
//        });
    }

    public static Intent makeIntent(Context context) {
            return new Intent(context, AirportPickupActivity.class);
    }
}
