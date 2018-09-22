package com.example.nortonwei.skycar.Customization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.nortonwei.skycar.LaunchActivity;

public class LoginUtils {
    public final static String WECHAT_APP_ID = "wxe1ce36a27b6dfb4f";
    public final static String WECHAT_APP_SECRET = "52c53a7be9e8d1d7b93fbf8a9f682afc";
    public final static String WECHAT_REQ_STATE = "skycar_android_wechat_login";

    private LoginUtils() {
    }

    public static void logout(Context context, Activity activity) {
        SharedPreferences share = context.getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean("isLogin", false);
        editor.commit();

        Intent intent = LaunchActivity.makeIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        activity.finish();
    }
}
