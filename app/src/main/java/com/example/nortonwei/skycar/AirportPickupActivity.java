package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.nortonwei.skycar.Adapter.CarTypeUltraPagerAdapter;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AirportPickupActivity extends AppCompatActivity {
    final String[] airportList = {"墨尔本机场", "悉尼机场", "阿德莱德机场", "堪培拉机场", "布里斯班机场", "凯恩斯机场"};
    int airportChoice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiport_pickup);

        setUpActionBar();
        setUpUIComponents();
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

    private void setUpUIComponents() {
        Button pickUpButton = (Button) findViewById(R.id.pick_up_button);
        Button dropOffButton = (Button) findViewById(R.id.drop_off_button);
//        Button roundTripButton = (Button) findViewById(R.id.round_trip_button);
        Button continueReserveButton = (Button) findViewById(R.id.continue_reserve_button);

        pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_selected), null,null);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.pickup_dropoff_linear);
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View useCarInfoView = vi.inflate(R.layout.layout_use_car_info, linearLayout, false);
        EditText chooseAirportEditText = (EditText) useCarInfoView.findViewById(R.id.choose_airport_editText);
        TextView arriveTextView = (TextView) useCarInfoView.findViewById(R.id.arrive_textView);
        EditText arriveEditText = (EditText) useCarInfoView.findViewById(R.id.arrive_editText);
        EditText useCarTimeEditText = (EditText) useCarInfoView.findViewById(R.id.use_car_time_editText);
        linearLayout.addView(useCarInfoView,1);

        SearchView searchView = (SearchView) useCarInfoView.findViewById(R.id.pickup_dropoff_searchView);

        View carTypeView = vi.inflate(R.layout.layout_choose_car_type, linearLayout, false);
        linearLayout.addView(carTypeView,2);

        UltraViewPager ultraViewPager = (UltraViewPager) carTypeView.findViewById(R.id.car_type_ultraViewPager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        PagerAdapter adapter = new CarTypeUltraPagerAdapter(true);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMultiScreen(0.5f);
        ultraViewPager.setItemRatio(1.0f);
        ultraViewPager.setRatio(2.0f);
        ultraViewPager.setMaxHeight(800);
        ultraViewPager.setAutoMeasureHeight(true);
//        UltraViewPager.Orientation gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;

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

        chooseAirportEditText.setOnClickListener(view -> {
            ArrayList airportArrayList = new ArrayList(Arrays.asList(airportList));

            OptionsPickerView countrySelectPicker = new OptionsPickerBuilder(AirportPickupActivity.this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                    airportChoice = options1;
                    chooseAirportEditText.setText(airportList[options1]);
                }
            })
                    .setCancelText(getString(R.string.cancel))
                    .setSubmitText(getString(R.string.confirm))
                    .setCancelColor(getResources().getColor(R.color.themeRed))
                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                    .setTitleText(getString(R.string.please_choose_airport))
                    .setSelectOptions(airportChoice)
                    .build();
            countrySelectPicker.setPicker(airportArrayList);
            countrySelectPicker.show();
        });

        useCarTimeEditText.setOnClickListener((View view) -> {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            endDate.set(2050,11,31);

            TimePickerView selectedTime = new TimePickerBuilder(AirportPickupActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    useCarTimeEditText.setText(date.toString());
                }
            })
                    .setType(new boolean[]{true, true, true, true, true, false})
                    .setLabel("年","月","日","时","分","秒")
                    .setCancelText(getString(R.string.cancel))
                    .setSubmitText(getString(R.string.confirm))
                    .setCancelColor(getResources().getColor(R.color.themeRed))
                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                    .setTitleText(getString(R.string.choose_use_car_time))
                    .setRangDate(startDate, endDate)
                    .isCenterLabel(true)
                    .build();
            selectedTime.show();
        });

        continueReserveButton.setOnClickListener(view -> {
            Intent intent = ConfirmTripActivity.makeIntent(AirportPickupActivity.this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AirportPickupActivity.class);
    }
}
