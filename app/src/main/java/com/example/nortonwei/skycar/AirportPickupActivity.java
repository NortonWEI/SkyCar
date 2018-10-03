package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.nortonwei.skycar.Adapter.CarTypeUltraPagerAdapter;
import com.example.nortonwei.skycar.Customization.LoginUtils;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.example.nortonwei.skycar.Model.Car;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tmall.ultraviewpager.UltraViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AirportPickupActivity extends AppCompatActivity {
    private int airportChoice = 0;
    private int carTypeChoice = 0;
    private ProgressBar progressBar;
    private EditText chooseAirportEditText;
    private EditText arriveEditText;
    private EditText useCarTimeEditText;
    private TextView carTypeTextView;
    private TextView rmbPriceTextView;
    private TextView audPriceTextView;
    private LinearLayout linearLayout;
    private View carTypeView;
    private UltraViewPager ultraViewPager;
    private ArrayList<String> airportIdList = new ArrayList<>();
    private ArrayList<String> airportList = new ArrayList<>();
    private ArrayList<Car> carList = new ArrayList<>();
    private boolean isReserveButtonClicked = false;
    private Calendar selectedDate = Calendar.getInstance();
    private int serviceOption = 0; // 0: pickup, 1: dropoff
    private int cancelValue = 0; //0: cannot cancel, 1: half-cancel, 2: free-cancel
    private String cancelTime = "";
    private float distance = 0.00f;
    private float placardPrice = 0.00f;
    private float childSeatPrice = 0.00f;

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button pickUpButton = (Button) findViewById(R.id.pick_up_button);
        Button dropOffButton = (Button) findViewById(R.id.drop_off_button);
//        Button roundTripButton = (Button) findViewById(R.id.round_trip_button);
        Button continueReserveButton = (Button) findViewById(R.id.continue_reserve_button);

        pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_selected), null,null);

        linearLayout = (LinearLayout) findViewById(R.id.pickup_dropoff_linear);
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View useCarInfoView = vi.inflate(R.layout.layout_use_car_info, linearLayout, false);
        chooseAirportEditText = (EditText) useCarInfoView.findViewById(R.id.choose_airport_editText);
        TextView arriveTextView = (TextView) useCarInfoView.findViewById(R.id.arrive_textView);
        arriveEditText = (EditText) useCarInfoView.findViewById(R.id.arrive_editText);
        useCarTimeEditText = (EditText) useCarInfoView.findViewById(R.id.use_car_time_editText);
        linearLayout.addView(useCarInfoView,1);

        SearchView searchView = (SearchView) useCarInfoView.findViewById(R.id.pickup_dropoff_searchView);

        carTypeView = vi.inflate(R.layout.layout_choose_car_type, linearLayout, false);
        ultraViewPager = (UltraViewPager) carTypeView.findViewById(R.id.car_type_ultraViewPager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultraViewPager.setMultiScreen(0.5f);
        ultraViewPager.setItemRatio(1.0f);
        ultraViewPager.setRatio(2.0f);
        ultraViewPager.setMaxHeight(500);
        ultraViewPager.setAutoMeasureHeight(true);

        PagerAdapter carTypeAdapter = new CarTypeUltraPagerAdapter(true, AirportPickupActivity.this, carList);
        ultraViewPager.setAdapter(carTypeAdapter);

        carTypeTextView = (TextView) carTypeView.findViewById(R.id.car_type_textView);
        rmbPriceTextView = (TextView) carTypeView.findViewById(R.id.rmb_price);
        audPriceTextView = (TextView) carTypeView.findViewById(R.id.aud_price);

        pickUpButton.setOnClickListener(view -> {
            if (pickUpButton.getCompoundDrawables()[1].getConstantState().equals(getResources().getDrawable(R.drawable.pickup_unselected).getConstantState())) {
                pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_selected), null,null);
                dropOffButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.dropoff_unselected), null,null);
//                roundTripButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.round_unselected), null,null);

                if (isReserveButtonClicked) {
                    linearLayout.removeView(useCarInfoView);
                    linearLayout.removeView(carTypeView);
                    arriveTextView.setText(getString(R.string.arrive));
                    arriveEditText.setHint(R.string.please_input_arrive);
                    useCarTimeEditText.setHint(R.string.use_car_time);
                    linearLayout.addView(useCarInfoView,1);
                    linearLayout.addView(carTypeView,2);
                } else {
                    linearLayout.removeView(useCarInfoView);
                    arriveTextView.setText(getString(R.string.arrive));
                    arriveEditText.setHint(R.string.please_input_arrive);
                    useCarTimeEditText.setHint(R.string.use_car_time);
                    linearLayout.addView(useCarInfoView,1);
                }
            }
            serviceOption = 0;
        });

        dropOffButton.setOnClickListener(view -> {
            if (dropOffButton.getCompoundDrawables()[1].getConstantState().equals(getResources().getDrawable(R.drawable.dropoff_unselected).getConstantState())) {
                pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_unselected), null,null);
                dropOffButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.dropoff_selected), null,null);
//                roundTripButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.round_unselected), null,null);

                if (isReserveButtonClicked) {
                    linearLayout.removeView(useCarInfoView);
                    linearLayout.removeView(carTypeView);
                    arriveTextView.setText(getString(R.string.depart));
                    arriveEditText.setHint(R.string.please_input_depart);
                    useCarTimeEditText.setHint(R.string.depart_time);
                    linearLayout.addView(useCarInfoView, 1);
                    linearLayout.addView(carTypeView, 2);
                } else {
                    linearLayout.removeView(useCarInfoView);
                    arriveTextView.setText(getString(R.string.depart));
                    arriveEditText.setHint(R.string.please_input_depart);
                    useCarTimeEditText.setHint(R.string.depart_time);
                    linearLayout.addView(useCarInfoView, 1);
                }
            }
            serviceOption = 1;
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
            setUpAirportList();
        });

        arriveEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_SEARCH ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        if (!chooseAirportEditText.getText().toString().isEmpty() && !useCarTimeEditText.getText().toString().isEmpty()) {
                            setUpCarList();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        useCarTimeEditText.setOnClickListener((View view) -> {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            endDate.set(2050,11,31);

            TimePickerView selectedTime = new TimePickerBuilder(AirportPickupActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    selectedDate.setTime(date);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    useCarTimeEditText.setText(dateFormat.format(date));

                    if (!arriveEditText.getText().toString().isEmpty() && !chooseAirportEditText.getText().toString().isEmpty()) {
                        setUpCarList();
                    }
                }
            })
                    .setType(new boolean[]{true, true, true, true, true, false})
                    .setLabel("年","月","日","时","分","秒")
                    .setCancelText(getString(R.string.cancel))
                    .setSubmitText(getString(R.string.confirm))
                    .setCancelColor(getResources().getColor(R.color.themeRed))
                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                    .setTitleText(getString(R.string.choose_use_car_time))
                    .setDate(selectedDate)
                    .setRangDate(startDate, endDate)
                    .isCenterLabel(true)
                    .build();
            selectedTime.show();
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId() == R.id.pickup_dropoff_searchView && !hasFocus) {

                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });

        arriveEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId() == R.id.arrive_editText && !hasFocus) {

                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });

        continueReserveButton.setOnClickListener(view -> {
            if (chooseAirportEditText.getText().toString().isEmpty() || arriveEditText.getText().toString().isEmpty() || useCarTimeEditText.getText().toString().isEmpty()) {
                Toast.makeText(AirportPickupActivity.this, "请您填写所有信息", Toast.LENGTH_SHORT).show();
            } else {
                if (!isReserveButtonClicked) {
                    setUpCarList();
                } else {
                    Intent intent = ConfirmTripActivity.makeIntent(AirportPickupActivity.this);
                    intent.putExtra("time", useCarTimeEditText.getText().toString());
                    intent.putExtra("distance", distance);
                    intent.putExtra("basePrice", carList.get(carTypeChoice).getBasePrice());
                    intent.putExtra("placardPrice", placardPrice);
                    intent.putExtra("childSeatPrice", childSeatPrice);
                    intent.putExtra("serviceOption", serviceOption);
                    if (serviceOption == 0) {
                        intent.putExtra("depart", chooseAirportEditText.getText().toString());
                        intent.putExtra("arrive", arriveEditText.getText().toString());
                    } else if (serviceOption == 1) {
                        intent.putExtra("depart", arriveEditText.getText().toString());
                        intent.putExtra("arrive", chooseAirportEditText.getText().toString());
                    }
                    intent.putExtra("carName", carList.get(carTypeChoice).getName());
                    intent.putExtra("carDesc", carList.get(carTypeChoice).getDesc());
                    intent.putExtra("cancelValue", cancelValue);
                    intent.putExtra("cancelTime", cancelTime);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
            }
        });
    }

    private void setUpAirportList() {
        airportIdList.clear();
        airportList.clear();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpApiService.BASE_URL)
                .client(HttpApiService.client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApiService service = retrofit.create(HttpApiService.class);

        Call<JsonObject> responseBodyCall = service.getAirportList();
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int status = response.body().get("status").getAsInt();
                String msg = response.body().get("msg").getAsString();

                if (status == HttpApiService.STATUS_OK) {
                    JsonArray data = response.body().get("data").getAsJsonArray();

                    for (JsonElement element: data) {
                        airportIdList.add(element.getAsJsonObject().get("id").getAsString());
                        airportList.add(element.getAsJsonObject().get("name").getAsString());
                    }

                    if (!airportList.isEmpty() && !airportIdList.isEmpty()) {
                        chooseAirportEditText.setText(airportList.get(airportChoice));

                        OptionsPickerView countrySelectPicker = new OptionsPickerBuilder(AirportPickupActivity.this, new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                                airportChoice = options1;
                                chooseAirportEditText.setText(airportList.get(options1));

                                if (!arriveEditText.getText().toString().isEmpty() && !useCarTimeEditText.getText().toString().isEmpty()) {
                                    setUpCarList();
                                }
                            }
                        })
                                .setCancelText(getString(R.string.cancel))
                                .setSubmitText(getString(R.string.confirm))
                                .setCancelColor(getResources().getColor(R.color.themeRed))
                                .setSubmitColor(getResources().getColor(R.color.themeRed))
                                .setTitleText(getString(R.string.please_choose_airport))
                                .setSelectOptions(airportChoice)
                                .build();
                        countrySelectPicker.setPicker(airportList);
                        countrySelectPicker.show();
                    }
                } else {
                    Toast.makeText(AirportPickupActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AirportPickupActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpCarList() {
        carList.clear();
        ultraViewPager.refresh();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApiService service = retrofit.create(HttpApiService.class);

        SharedPreferences share = getSharedPreferences("Login",
                Context.MODE_PRIVATE);

        String address = arriveEditText.getText().toString();
        String[] addressList = {address};
        Call<JsonObject> responseBodyCall = service.checkCarType(share.getString("token", ""),
                useCarTimeEditText.getText().toString(),
                addressList,
                airportIdList.get(airportChoice));

        responseBodyCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int status = response.body().get("status").getAsInt();
                String msg = response.body().get("msg").getAsString();

                if (status == HttpApiService.STATUS_OK) {
                    JsonObject data = response.body().get("data").getAsJsonObject();
                    distance = data.get("distance").getAsFloat();
                    placardPrice = data.get("placards_price").getAsFloat();
                    childSeatPrice = data.get("children_price").getAsFloat();
                    int freeCancel = data.get("free_cancel").getAsInt();
                    int halfCancel = data.get("half_cancel").getAsInt();

                    if (freeCancel == 1) {
                        cancelValue = 2;
                    } else if (halfCancel == 1) {
                        cancelValue = 1;
                    } else {
                        cancelValue = 0;
                    }
                    cancelTime = data.get("cancel_time").getAsString();

                    JsonArray carArray = data.get("carList").getAsJsonArray();

                    for (JsonElement element: carArray) {
                        JsonObject car = element.getAsJsonObject();
                        String id = car.get("id").getAsString();
                        String name = car.get("name").getAsString();
                        String desc = car.get("descript").getAsString();
                        String imgUrl = car.get("img").getAsString();
                        float price = car.get("price").getAsFloat();
                        float audPrice = car.get("aud_price").getAsFloat();

                        carList.add(new Car(id, name, desc, imgUrl, price, audPrice));
                    }
                    ultraViewPager.refresh();

                    if (!carList.isEmpty()) {
                        ultraViewPager.setCurrentItem(0);
                        carTypeTextView.setText(carList.get(0).getName());
                        rmbPriceTextView.setText(getString(R.string.base_price) + carList.get(0).getBasePrice());
                        audPriceTextView.setText("AUD" + carList.get(0).getAudPrice());
                    }

                    if (isReserveButtonClicked) {
                        linearLayout.removeViewAt(2);
                    }

                    linearLayout.addView(carTypeView,2);

                    ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {}

                        @Override
                        public void onPageSelected(int i) {
                            carTypeTextView.setText(carList.get(i).getName());
                            rmbPriceTextView.setText(getString(R.string.base_price) + carList.get(i).getBasePrice());
                            audPriceTextView.setText("AUD" + carList.get(i).getAudPrice());
                            carTypeChoice = i;
                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {}
                    });

                    isReserveButtonClicked = true;
                } else if (status == HttpApiService.STATUS_LOGOUT) {
                    LoginUtils.logout(AirportPickupActivity.this);
                    Toast.makeText(AirportPickupActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AirportPickupActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AirportPickupActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AirportPickupActivity.class);
    }
}
