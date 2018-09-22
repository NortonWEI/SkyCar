package com.example.nortonwei.skycar.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.nortonwei.skycar.Customization.LoginUtils;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.example.nortonwei.skycar.LoginActivity;
import com.example.nortonwei.skycar.R;
import com.example.nortonwei.skycar.WechatLoginTransitionActivity;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private int errCode = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPI api = WXAPIFactory.createWXAPI(this, LoginUtils.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        errCode = baseResp.errCode;

        switch (errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (((SendAuth.Resp) baseResp).state.equals(LoginUtils.WECHAT_REQ_STATE)) {
                    String code = ((SendAuth.Resp) baseResp).code;
                    Log.d("code", code);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(HttpApiService.WECHAT_INFO_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    HttpApiService service = retrofit.create(HttpApiService.class);

                    String suffix = "/sns/oauth2/access_token?appid="
                            + LoginUtils.WECHAT_APP_ID
                            + "&secret=" + LoginUtils.WECHAT_APP_SECRET + "&code=" + code
                            + "&grant_type=authorization_code";

                    Call<JsonObject> responseBodyCall = service.obtainWechatAccessToken(suffix);
                    responseBodyCall.enqueue(new Callback<JsonObject>() {

                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            String accessToken = response.body().get("access_token").getAsString();
                            String openId = response.body().get("openid").getAsString();
                            String unionId = response.body().get("unionid").getAsString();

                            Intent intent = WechatLoginTransitionActivity.makeIntent(WXEntryActivity.this);
                            intent.putExtra("accessToken", accessToken);
                            intent.putExtra("openId", openId);
                            intent.putExtra("unionId", unionId);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(WXEntryActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "您已取消授权", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "您已拒绝授权", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                break;
        }

        if (errCode != BaseResp.ErrCode.ERR_OK) {
            finish();
        }
    }
}
