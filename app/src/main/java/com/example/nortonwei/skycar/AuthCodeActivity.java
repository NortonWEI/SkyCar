package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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

import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_code);

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
//                NavUtils.navigateUpFromSameTask(this);
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

    private void setUpUIComponents() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button nextButton = (Button) findViewById(R.id.next_profile_button);
        Button resendButton = (Button) findViewById(R.id.resend_auth_code_button);
        EditText authCodeEditText = (EditText) findViewById(R.id.auth_code_editText);
        TextView sendNumberTextView = (TextView) findViewById(R.id.send_number_textView);

        String mobileNumber = getIntent().getStringExtra("countryCode") + " " +  getIntent().getStringExtra("mobile");
        String sendNumberText = getString(R.string.auth_code_sent_foo) + mobileNumber + getString(R.string.auth_code_sent_bar);
        Spannable spannable = new SpannableString(sendNumberText);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeRed)),
                getString(R.string.auth_code_sent_foo).length(),
                (getString(R.string.auth_code_sent_foo) + mobileNumber).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sendNumberTextView.setText(spannable, TextView.BufferType.SPANNABLE);

        nextButton.setOnClickListener(view -> {
            if (authCodeEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "请您填写验证码", Toast.LENGTH_SHORT).show();
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HttpApiService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                HttpApiService service = retrofit.create(HttpApiService.class);

                Call<JsonObject> responseBodyCall = service.verifyCode(getIntent().getStringExtra("countryCode"), getIntent().getStringExtra("mobile"), authCodeEditText.getText().toString());
                responseBodyCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        int status = response.body().get("status").getAsInt();
                        String msg = response.body().get("msg").getAsString();

                        if (status == HttpApiService.STATUS_OK) {
                            JsonObject data = response.body().get("data").getAsJsonObject();

                            SharedPreferences share = getSharedPreferences("Login",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = share.edit();
                            editor.putBoolean("isLogin", false);
                            editor.putString("mobile", getIntent().getStringExtra("mobile"));
                            editor.putString("token", data.get("token").getAsString());
                            editor.commit();

                            Intent intent = ProfileSetActivity.makeIntent(AuthCodeActivity.this);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        } else {
                            Toast.makeText(AuthCodeActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AuthCodeActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        resendButton.setOnClickListener(view -> {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.VISIBLE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HttpApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            HttpApiService service = retrofit.create(HttpApiService.class);

            Call<JsonObject> responseBodyCall = service.sendVerificationCode(getIntent().getStringExtra("countryCode"), getIntent().getStringExtra("mobile"), 1);
            responseBodyCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    int status = response.body().get("status").getAsInt();
                    String msg = response.body().get("msg").getAsString();

                    if (status == HttpApiService.STATUS_OK) {
                        Toast.makeText(AuthCodeActivity.this, "验证码已重新发送，请注意查收", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AuthCodeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AuthCodeActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AuthCodeActivity.class);
    }
}
