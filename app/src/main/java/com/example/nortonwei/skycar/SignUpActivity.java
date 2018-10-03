package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.nortonwei.skycar.Customization.LoginUtils;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    private int countryChoice = 0;
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<String> countryCodeList = new ArrayList<>();

    private ProgressBar progressBar;
    EditText countryRegionEditText;
    EditText phoneNumberEditText;
    EditText countryRegionHintEditText;
    EditText countryCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpActionBar();
        setUpHyperLink();
        setUpUIComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_up_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case R.id.login_menu_button:
                Intent intent = LoginActivity.makeIntent(SignUpActivity.this);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionBar() {
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.carbon_black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpHyperLink() {
        TextView conditionTermTextView = (TextView) findViewById(R.id.condition_term_textView);
        conditionTermTextView.setText(getClickableSpan());
        conditionTermTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private SpannableString getClickableSpan() {
        SpannableString spannableString = new SpannableString(getString(R.string.condition_term));

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                    Toast.makeText(MainActivity.this,"使用条款",Toast.LENGTH_SHORT).show();
            }
        }, 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeRed)), 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                    Toast.makeText(MainActivity.this,"隐私政策",Toast.LENGTH_SHORT).show();
            }
        }, 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeRed)), 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void setUpCountryList() {
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
                        countryRegionEditText.setText(countryList.get(countryChoice));
                        countryCodeEditText.setText(countryCodeList.get(countryChoice) + "  ");

                        countryRegionEditText.setOnClickListener(view -> {
                            OptionsPickerView countrySelectPicker = new OptionsPickerBuilder(SignUpActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                                    countryChoice = options1;
                                    countryRegionEditText.setText(countryList.get(options1));
                                    countryCodeEditText.setText(countryCodeList.get(options1) + "  ");
                                }
                            })
                                    .setCancelText(getString(R.string.cancel))
                                    .setSubmitText(getString(R.string.confirm))
                                    .setCancelColor(getResources().getColor(R.color.themeRed))
                                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                                    .setTitleText(getString(R.string.choose_number_region))
                                    .setSelectOptions(countryChoice)
                                    .build();
                            countrySelectPicker.setPicker(countryList);
                            countrySelectPicker.show();
                        });
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpUIComponents() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        countryRegionEditText = (EditText) findViewById(R.id.country_region_editText);
        phoneNumberEditText = (EditText) findViewById(R.id.sign_up_phone_editText);
        countryRegionHintEditText = (EditText) findViewById(R.id.country_region_hint_editText);
        countryCodeEditText = (EditText) findViewById(R.id.country_code_editText);

        countryRegionHintEditText.setText(getString(R.string.country_region) + "  ");
        phoneNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        setUpCountryList();

        Button nextButton = (Button) findViewById(R.id.next_auth_code_button);

        nextButton.setOnClickListener(view -> {
            if (phoneNumberEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "请您填写电话号", Toast.LENGTH_SHORT).show();
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HttpApiService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                HttpApiService service = retrofit.create(HttpApiService.class);

                Call<JsonObject> responseBodyCall = service.sendVerificationCode(countryCodeEditText.getText().toString().trim(), phoneNumberEditText.getText().toString(), 1);
                responseBodyCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        int status = response.body().get("status").getAsInt();
                        String msg = response.body().get("msg").getAsString();

                        if (status == HttpApiService.STATUS_OK) {
                            Intent intent = AuthCodeActivity.makeIntent(SignUpActivity.this);
                            intent.putExtra("countryCode", countryCodeEditText.getText().toString().trim());
                            intent.putExtra("mobile", phoneNumberEditText.getText().toString());
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        } else {
                            Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
        return new Intent(context, SignUpActivity.class);
    }
}
