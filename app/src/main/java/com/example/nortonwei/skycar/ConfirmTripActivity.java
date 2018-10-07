package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.nortonwei.skycar.Customization.PaymentPopup;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jmf.addsubutils.AddSubUtils;

import java.util.ArrayList;

import me.shaohui.bottomdialog.BottomDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ConfirmTripActivity extends AppCompatActivity {
    private static final int CONTACT_REQUEST_CODE = 7001;
    private String depart = "";
    private String arrive = "";
    private int serviceOption = 0;
    private int adultNum = 0;
    private int sevenPlusChildNum = 0;
    private int sevenMinusChildNum = 0;
    private int bigLuggageNum = 0;
    private int smallLuggageNum = 0;
    private ProgressBar progressBar;
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<String> countryCodeList = new ArrayList<>();
    private float currentPrice = 0f;

    EditText contactEditText;
    EditText phoneEditText;
    Spinner phoneCodeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_trip);

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
        HomeActivity.setUpActionBar(this, getString(R.string.confirm_trip));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        LinearLayout layout = (LinearLayout) findViewById(R.id.content_linearLayout);
        Button onlinePayButton = (Button) findViewById(R.id.online_pay_button);
//        SpannableString onlinePayButtonText = new SpannableString("¥123" + "\n" + getString(R.string.online_pay_hint));
//        onlinePayButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, onlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        onlinePayButton.setText(onlinePayButtonText);

//        Button offlinePayButton = (Button) findViewById(R.id.offline_pay_button);
//        SpannableString offlinePayButtonText = new SpannableString("$12" + "\n" + getString(R.string.offline_pay_hint));
//        offlinePayButtonText.setSpan(new RelativeSizeSpan(0.8f), 4, offlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        offlinePayButton.setText(offlinePayButtonText);
        Switch shareCarSwitch = (Switch) findViewById(R.id.car_share_switch);
        Switch placardSwitch = (Switch) findViewById(R.id.placard_switch);
        CheckBox checkBox = (CheckBox) findViewById(R.id.hint_checkBox);

        TextView timeTextView = (TextView) findViewById(R.id.time_textView);
        TextView departTextView = (TextView) findViewById(R.id.depart_textView);
        TextView arriveTextView = (TextView) findViewById(R.id.arrive_textView);
        TextView carTypeTextView = (TextView) findViewById(R.id.car_type_textView);
        TextView cancelTextView = (TextView) findViewById(R.id.cancel_textView);
        ImageView cancelHintImageView = (ImageView) findViewById(R.id.cancel_hint_imageView);
        ImageButton switchImageButton = (ImageButton) findViewById(R.id.switch_imageButton);

        EditText peopleNumberEditText = (EditText) findViewById(R.id.people_number_editText);
        EditText luggageNumberEditText = (EditText) findViewById(R.id.luggage_number_editText);

        contactEditText = (EditText) findViewById(R.id.contact_person_editText);
        phoneEditText = (EditText) findViewById(R.id.phone_number_editText);
        EditText standbyPhoneEditText = (EditText) findViewById(R.id.standby_phone_number_editText);
        EditText flightNumberEditText = (EditText) findViewById(R.id.flight_number_editText);

        CardView placardCardView = (CardView) findViewById(R.id.placard_cardView);
        TextView placardPriceTextView = (TextView) findViewById(R.id.placard_price_textView);

        if (getIntent().hasExtra("time") &&
                getIntent().hasExtra("distance") &&
                getIntent().hasExtra("basePrice") &&
                getIntent().hasExtra("serviceOption") &&
                getIntent().hasExtra("depart") &&
                getIntent().hasExtra("arrive") &&
                getIntent().hasExtra("carName") &&
                getIntent().hasExtra("carDesc") &&
                getIntent().hasExtra("cancelValue") &&
                getIntent().hasExtra("cancelTime")) {
            timeTextView.setText(getIntent().getStringExtra("time"));
            serviceOption = getIntent().getIntExtra("serviceOption", 0);
            depart = getIntent().getStringExtra("depart");
            arrive = getIntent().getStringExtra("arrive");
            departTextView.setText(depart);
            arriveTextView.setText(arrive + "（大约" + getIntent().getFloatExtra("distance", 0) + "km）");
            carTypeTextView.setText(getIntent().getStringExtra("carName") + "（" + getIntent().getStringExtra("carDesc") + "）");

            if (serviceOption == 1) {
                layout.removeView(placardCardView);
            }

            String content;
            SpannableString string;
            switch (getIntent().getIntExtra("cancelValue", 0)) {
                case 0: //cannot cancel
                    cancelHintImageView.setImageResource(R.drawable.noncancelable);
                    content = "司机接单后不可免费取消，具体请看 取消规则";
                    string = new SpannableString(content);
                    string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeRed)), content.length()-4, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    cancelTextView.setText(string);
                    break;
                case 1: //half cancel
                    cancelHintImageView.setImageResource(R.drawable.cancelabel);
                    content = "北京时间" + getIntent().getStringExtra("cancelTime") + "前 可半价取消";
                    string = new SpannableString(content);
                    string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeGreen)), content.length()-5, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    cancelTextView.setText(string);
                    break;
                case 2: //free cancel
                    cancelHintImageView.setImageResource(R.drawable.cancelabel);
                    content = "北京时间" + getIntent().getStringExtra("cancelTime") + "前 可免费取消";
                    string = new SpannableString(content);
                    string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeGreen)), content.length()-5, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    cancelTextView.setText(string);
                    break;
                default:
                    break;
            }
            currentPrice = getIntent().getFloatExtra("basePrice", 0);
            onlinePayButton.setText("¥" + currentPrice);
        }

        if (getIntent().hasExtra("placardPrice")) {
            placardPriceTextView.setText("¥" + getIntent().getFloatExtra("placardPrice",0) + "/次");
        }

        placardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getIntent().hasExtra("placardPrice")) {
                    if (isChecked) {
                        currentPrice += getIntent().getFloatExtra("placardPrice",0);
                    } else {
                        currentPrice -= getIntent().getFloatExtra("placardPrice",0);
                    }
                    onlinePayButton.setText("¥" + currentPrice);
                }
            }
        });

        String checkBoxContent = "下单代表您同意接送机用户使用协议和隐私协议";
        SpannableString checkBoxString = new SpannableString(checkBoxContent);
        checkBoxString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeRed)), checkBoxContent.length()-11, checkBoxContent.length()-5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBoxString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeRed)), checkBoxContent.length()-4, checkBoxContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBox.setText(checkBoxString);

        switchImageButton.setOnClickListener(view -> {
            String tempString = arrive;
            arrive = depart;
            depart = tempString;
            departTextView.setText(depart);
            arriveTextView.setText(arrive + "（大约" + getIntent().getFloatExtra("distance", 0) + "km）");

            if (serviceOption == 0) {
                serviceOption = 1;
                layout.removeView(placardCardView);

                if (placardSwitch.isChecked()) {
                    currentPrice -= getIntent().getFloatExtra("placardPrice",0);
                }
            } else if (serviceOption == 1) {
                serviceOption = 0;
                layout.addView(placardCardView, layout.getChildCount()-2);

                if (placardSwitch.isChecked()) {
                    currentPrice += getIntent().getFloatExtra("placardPrice",0);
                }
            }
        });

        peopleNumberEditText.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    Button confirmButton = (Button) v.findViewById(R.id.people_number_confirm_button);
                    Button cancelButton = (Button) v.findViewById(R.id.people_number_cancel_button);

                    AddSubUtils adultAddSubUtils = (AddSubUtils) v.findViewById(R.id.adult_add_sub);
                    adultAddSubUtils.setBuyMax(30)
                            .setBuyMin(0)
                            .setCurrentNumber(adultNum);

                    AddSubUtils sevenPlusAddSubUtils = (AddSubUtils) v.findViewById(R.id.seven_plus_child_add_sub);
                    sevenPlusAddSubUtils.setBuyMax(30)
                            .setBuyMin(0)
                            .setCurrentNumber(sevenPlusChildNum);

                    AddSubUtils sevenMinusAddSubUtils = (AddSubUtils) v.findViewById(R.id.seven_minus_child_add_sub);
                    sevenMinusAddSubUtils.setBuyMax(30)
                            .setBuyMin(0)
                            .setCurrentNumber(sevenMinusChildNum);

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adultNum = adultAddSubUtils.getNumber();
                            sevenPlusChildNum = sevenPlusAddSubUtils.getNumber();
                            sevenMinusChildNum = sevenMinusAddSubUtils.getNumber();
                            peopleNumberEditText.setText("成人" + adultNum + "，儿童" + (sevenPlusChildNum+sevenMinusChildNum));

                            if (getIntent().hasExtra("childSeatPrice")) {
                                currentPrice += (getIntent().getFloatExtra("childSeatPrice", 0) * (sevenPlusChildNum+sevenMinusChildNum));
                                onlinePayButton.setText("¥" + currentPrice);
                            }

                            bottomDialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_choose_people_number)
                    .show();
        });

        luggageNumberEditText.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    Button confirmButton = (Button) v.findViewById(R.id.luggage_number_confirm_button);
                    Button cancelButton = (Button) v.findViewById(R.id.luggage_number_cancel_button);

                    AddSubUtils bigLuggageAddSubUtils = (AddSubUtils) v.findViewById(R.id.big_luggage_add_sub);
                    bigLuggageAddSubUtils.setBuyMax(30)
                            .setBuyMin(0)
                            .setCurrentNumber(bigLuggageNum);

                    AddSubUtils smallLuggageAddSubUtils = (AddSubUtils) v.findViewById(R.id.small_luggage_add_sub);
                    smallLuggageAddSubUtils.setBuyMax(30)
                            .setBuyMin(0)
                            .setCurrentNumber(smallLuggageNum);

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bigLuggageNum = bigLuggageAddSubUtils.getNumber();
                            smallLuggageNum = smallLuggageAddSubUtils.getNumber();
                            luggageNumberEditText.setText("大行李" + bigLuggageNum + "，小行李" + smallLuggageNum);

                            bottomDialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_choose_luggage_number)
                    .show();
        });

        Button frequentPassengerButton = (Button) findViewById(R.id.frequent_passenger_button);
        frequentPassengerButton.setOnClickListener(view -> {
            Intent intent = FrequentPassengerActivity.makeIntent(this);
            startActivityForResult(intent, CONTACT_REQUEST_CODE);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        onlinePayButton.setOnClickListener(view -> {
            if (peopleNumberEditText.getText().toString().isEmpty() ||
                    luggageNumberEditText.getText().toString().isEmpty() ||
                    contactEditText.getText().toString().isEmpty() ||
                    phoneEditText.getText().toString().isEmpty() ||
                    flightNumberEditText.getText().toString().isEmpty()) {
                Toast.makeText(ConfirmTripActivity.this, "请填写您的行程信息", Toast.LENGTH_SHORT).show();
            } else if (adultNum == 0) {
                Toast.makeText(ConfirmTripActivity.this, "乘车成人数需至少为1", Toast.LENGTH_SHORT).show();
            } else if (!checkBox.isChecked()) {
                Toast.makeText(ConfirmTripActivity.this, "请您先同意接送机用户使用协议和隐私协议", Toast.LENGTH_SHORT).show();
            } else {
                PaymentPopup popup = new PaymentPopup(this, currentPrice);
                popup.setUp();
                popup.show();
            }
        });

//        offlinePayButton.setOnClickListener(view -> {
//            Intent intent = OfflinePaymentSuccessActivity.makeIntent(this);
//            startActivity(intent);
//            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//        });
        setUpCountryCodeList();
    }

    private void setUpCountryCodeList() {
        countryList.clear();
        countryCodeList.clear();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpApiService.BASE_URL)
                .client(HttpApiService.client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApiService service = retrofit.create(HttpApiService.class);

        Call<JsonObject> responseBodyCall = service.getCountryList();
        responseBodyCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int status = response.body().get("status").getAsInt();
                String msg = response.body().get("msg").getAsString();

                if (status == HttpApiService.STATUS_OK) {
                    JsonArray data = response.body().get("data").getAsJsonArray();

                    for (JsonElement element: data) {
                        countryList.add(element.getAsJsonObject().get("name").getAsString());
                        countryCodeList.add(element.getAsJsonObject().get("val").getAsString());
                    }
                    if (!countryList.isEmpty() && !countryCodeList.isEmpty()) {
                        phoneCodeSpinner = (Spinner) findViewById(R.id.phone_country_code_spinner);
                        ArrayAdapter<String> phoneCodeAdapter = new ArrayAdapter<String>(ConfirmTripActivity.this, android.R.layout.simple_spinner_item, countryCodeList);
                        phoneCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        phoneCodeSpinner.setAdapter(phoneCodeAdapter);

                        Spinner standbyPhoneCodeSpinner = (Spinner) findViewById(R.id.standby_phone_country_code_spinner);
                        ArrayAdapter<String> standbyPhoneCodeAdapter = new ArrayAdapter<String>(ConfirmTripActivity.this, android.R.layout.simple_spinner_item, countryCodeList);
                        standbyPhoneCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        standbyPhoneCodeSpinner.setAdapter(standbyPhoneCodeAdapter);
                    }
                } else {
                    Toast.makeText(ConfirmTripActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ConfirmTripActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CONTACT_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    contactEditText.setText(data.getStringExtra("name"));
                    String[] mobileString = data.getStringExtra("mobile").split(" ");
                    if (mobileString.length >= 2) {
                        for (int i=0; i<countryCodeList.size(); i++) {
                            if (countryCodeList.get(i).contains(mobileString[0])) {
                                phoneCodeSpinner.setSelection(i);
                            }
                        }
                        phoneEditText.setText(mobileString[1]);
                    } else {
                        phoneEditText.setText(data.getStringExtra("mobile"));
                    }
                }
                break;
            default:
                break;
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ConfirmTripActivity.class);
    }
}
