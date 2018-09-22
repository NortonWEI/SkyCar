package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WechatLoginTransitionActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_login_transition);

        progressBar = findViewById(R.id.progressBar);

        if (getIntent().hasExtra("accessToken") && getIntent().hasExtra("openId") && getIntent().hasExtra("unionId")) {
            doWechatLogin(getIntent().getStringExtra("accessToken"), getIntent().getStringExtra("openId"), getIntent().getStringExtra("unionId"));
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    private void doWechatLogin(String accessToken, String openId, String unionId) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpApiService.WECHAT_INFO_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApiService service = retrofit.create(HttpApiService.class);

        String suffix = "/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;

        Call<JsonObject> responseBodyCall = service.obtainWechatUserInfo(suffix);
        responseBodyCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String nickname = response.body().get("nickname").getAsString();
                String headImgUrl = response.body().get("headimgurl").getAsString();
                int sex = response.body().get("sex").getAsInt();
//                String country = response.body().get("country").getAsString();
//                String province = response.body().get("province").getAsString();
//                String city = response.body().get("city").getAsString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HttpApiService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                HttpApiService service = retrofit.create(HttpApiService.class);

                Call<JsonObject> responseBodyCall = service.createWechatLogin(unionId, nickname, headImgUrl, sex);
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
                            editor.putBoolean("isLogin", true);
                            editor.putString("mobile", "");
                            editor.putString("token", data.get("token").toString());
                            editor.commit();

                            Intent intent = HomeActivity.makeIntent(WechatLoginTransitionActivity.this);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(WechatLoginTransitionActivity.this, msg, Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(WechatLoginTransitionActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(WechatLoginTransitionActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, WechatLoginTransitionActivity.class);
    }
}
