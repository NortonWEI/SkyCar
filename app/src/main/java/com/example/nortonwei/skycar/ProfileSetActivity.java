package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
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
import android.widget.Toast;

import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileSetActivity extends AppCompatActivity {
    private int sex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);

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
        Button nextButton = (Button) findViewById(R.id.next_set_password_button);
        ImageButton maleButton = (ImageButton) findViewById(R.id.male_imageButton);
        ImageButton femaleButton = (ImageButton) findViewById(R.id.female_imageButton);
        EditText nickNameEditText = (EditText) findViewById(R.id.set_nickname_editText);
        EditText emailEditText = (EditText) findViewById(R.id.set_email_editText);

        nextButton.setOnClickListener(view -> {
            if (nickNameEditText.getText().toString().isEmpty() || emailEditText.getText().toString().isEmpty() || sex == 0) {
                Toast.makeText(this, "请您填写并选择所有信息", Toast.LENGTH_SHORT).show();
            } else if (!emailEditText.getText().toString().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")) {
                Toast.makeText(this, "请您填写有效的邮箱地址", Toast.LENGTH_SHORT).show();
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HttpApiService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                HttpApiService service = retrofit.create(HttpApiService.class);

                SharedPreferences share = getSharedPreferences("Login",
                        Context.MODE_PRIVATE);

                Call<JsonObject> responseBodyCall = service.setProfile(share.getString("token", ""),
                        nickNameEditText.getText().toString(),
                        sex,
                        emailEditText.getText().toString());
                responseBodyCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        int status = response.body().get("status").getAsInt();
                        String msg = response.body().get("msg").getAsString();

                        if (status == HttpApiService.STATUS_OK) {
                            Intent intent = PasswordSetActivity.makeIntent(ProfileSetActivity.this);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        } else {
                            Toast.makeText(ProfileSetActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Log.d("msg", msg);
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileSetActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        maleButton.setOnClickListener(view -> {
            if (maleButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.male_unchecked).getConstantState())) {
                maleButton.setImageResource(R.drawable.male_selected);
                femaleButton.setImageResource(R.drawable.female_unchecked);
                sex = 1;
            } else if (maleButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.male_selected).getConstantState())) {
                maleButton.setImageResource(R.drawable.male_unchecked);
                sex = 0;
            }
        });

        femaleButton.setOnClickListener(view -> {
            if (femaleButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.female_unchecked).getConstantState())) {
                femaleButton.setImageResource(R.drawable.female_selected);
                maleButton.setImageResource(R.drawable.male_unchecked);
                sex = 2;
            } else if (femaleButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.female_selected).getConstantState())) {
                femaleButton.setImageResource(R.drawable.female_unchecked);
                sex = 0;
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
        return new Intent(context, ProfileSetActivity.class);
    }
}
