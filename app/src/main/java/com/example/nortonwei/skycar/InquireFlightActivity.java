package com.example.nortonwei.skycar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.nortonwei.skycar.Customization.LoginUtils;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;

import me.shaohui.bottomdialog.BottomDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InquireFlightActivity extends AppCompatActivity {
    Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire_flight);

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
        HomeActivity.setUpActionBar(this, getString(R.string.inquire_flight));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        EditText flightNumberEditText = (EditText) findViewById(R.id.flight_number_editText);
        EditText takeoffTimeEditText = (EditText) findViewById(R.id.takeoff_time_editText);
        Button inquireButton = (Button) findViewById(R.id.inquire_button);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        takeoffTimeEditText.setOnClickListener(view -> {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            endDate.set(2050,11,31);

            flightNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(v.getId() == R.id.flight_number_editText && !hasFocus) {

                        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    }
                }
            });

            TimePickerView selectedTime = new TimePickerBuilder(InquireFlightActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    selectedDate = calendar;

                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String month = String.format("%02d", calendar.get(Calendar.MONTH)+1);
                    String dayOfMonth = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));

                    takeoffTimeEditText.setText(year + "-" + month + "-" + dayOfMonth);
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("年","月","日","时","分","秒")
                    .setCancelText(getString(R.string.cancel))
                    .setSubmitText(getString(R.string.confirm))
                    .setDate(selectedDate)
                    .setCancelColor(getResources().getColor(R.color.themeRed))
                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                    .setTitleText(getString(R.string.depart_time))
                    .setRangDate(startDate, endDate)
                    .isCenterLabel(true)
                    .build();
            selectedTime.show();
        });

        inquireButton.setOnClickListener(view -> {
            if (flightNumberEditText.getText().toString().isEmpty() || takeoffTimeEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "请您填写航班号及起飞时间", Toast.LENGTH_SHORT).show();
            }
//            else if (!flightNumberEditText.getText().toString().matches("^([^\\W_]{2}[a-z]?)?(\\d{4})[a-z]?$")) {
//                Toast.makeText(this, "请您输入有效的航班号", Toast.LENGTH_SHORT).show();
//            }
            else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HttpApiService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                HttpApiService service = retrofit.create(HttpApiService.class);

                SharedPreferences share = getSharedPreferences("Login",
                        Context.MODE_PRIVATE);

                Log.d("token", share.getString("token", ""));

                Call<JsonObject> responseBodyCall = service.inquireFlight(share.getString("token", ""),
                        flightNumberEditText.getText().toString(),
                        takeoffTimeEditText.getText().toString());

                responseBodyCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        int status = response.body().get("status").getAsInt();
                        String msg = response.body().get("msg").getAsString();

                        if (status == HttpApiService.STATUS_OK) {
                            JsonArray data = response.body().get("data").getAsJsonArray();
                            String flightNo = "";
                            String onTimeRate = "";
                            String depCity = "";
                            String arrCity = "";
                            String depTerminal = "";
                            String arrTerminal = "";
                            String depDate = "";
                            String arrDate = "";
                            String depTime = "";
                            String arrTime = "";

                            for (JsonElement element: data) {
                                JsonObject object = element.getAsJsonObject();
                                flightNo = object.get("flightNo").getAsString();
                                onTimeRate = object.get("rate").getAsString();
                                depCity = object.get("depCity").getAsString();
                                arrCity = object.get("arrCity").getAsString();
                                depTerminal = object.get("depTerminal").getAsString();
                                arrTerminal = object.get("arrTerminal").getAsString();
                                depDate = object.get("depDate").getAsString();
                                arrDate = object.get("arrDate").getAsString();
                                depTime = object.get("depTime").getAsString();
                                arrTime = object.get("arrTime").getAsString();
                            }

                            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

                            String finalFlightNo = flightNo;
                            String finalDepTime = depTime;
                            String finalArrTime = arrTime;
                            String finalDepCity = depCity;
                            String finalDepTerminal = depTerminal;
                            String finalArrCity = arrCity;
                            String finalArrTerminal = arrTerminal;
                            String finalDepDate = depDate;
                            String finalArrDate = arrDate;
                            String finalOnTimeRate = onTimeRate;
                            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                                @Override
                                public void bindView(View v) {
                                    ImageButton closeButton = (ImageButton) v.findViewById(R.id.cancel_button);
                                    TextView flightNumberTextView = (TextView) v.findViewById(R.id.flight_number_textView);
                                    TextView dateTextView = (TextView) v.findViewById(R.id.date_textView);
                                    TextView airlineTextView = (TextView) v.findViewById(R.id.airline_textView);
                                    TextView departTimeTextView = (TextView) v.findViewById(R.id.depart_time_textView);
                                    TextView arriveTimeTextView = (TextView) v.findViewById(R.id.arrive_time_textView);
                                    TextView departAirportTextView = (TextView) v.findViewById(R.id.depart_airport_textView);
                                    TextView arriveAirportTextView = (TextView) v.findViewById(R.id.arrive_airport_textView);
                                    TextView departDateTextView = (TextView) v.findViewById(R.id.depart_date_textView);
                                    TextView arriveDateTextView = (TextView) v.findViewById(R.id.arrive_date_textView);

                                    closeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bottomDialog.dismiss();
                                        }
                                    });

                                    String[] weeks = getResources().getStringArray(R.array.chinese_week_string_array);

                                    flightNumberTextView.setText(flightNumberEditText.getText());
                                    dateTextView.setText((selectedDate.get(Calendar.MONTH)+1) + "月" +
                                            selectedDate.get(Calendar.DAY_OF_MONTH) + "日 " +
                                    "周" + weeks[selectedDate.get(Calendar.DAY_OF_WEEK)-1]);
                                    airlineTextView.setText(finalFlightNo + " 历史准点率：" + finalOnTimeRate + "%");
                                    departTimeTextView.setText(finalDepTime);
                                    arriveTimeTextView.setText(finalArrTime);
                                    if (finalDepTerminal.isEmpty()) {
                                        departAirportTextView.setText(finalDepCity);
                                    } else {
                                        departAirportTextView.setText(finalDepCity + " 航站楼" + finalDepTerminal);
                                    }

                                    if (finalArrTerminal.isEmpty()) {
                                        arriveAirportTextView.setText(finalArrCity);
                                    } else {
                                        arriveAirportTextView.setText(finalArrCity + " 航站楼" + finalArrTerminal);
                                    }

                                    departDateTextView.setText(finalDepDate);
                                    arriveDateTextView.setText(finalArrDate);
                                }
                            })
                                    .setLayoutRes(R.layout.layout_inquire_flight_result)
                                    .show();
                        } else if (status == HttpApiService.STATUS_LOGOUT) {
                            LoginUtils.logout(InquireFlightActivity.this);
                            Toast.makeText(InquireFlightActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(InquireFlightActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(InquireFlightActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                });
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
        return new Intent(context, InquireFlightActivity.class);
    }
}
