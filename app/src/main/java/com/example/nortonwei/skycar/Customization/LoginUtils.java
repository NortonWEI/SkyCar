package com.example.nortonwei.skycar.Customization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.nortonwei.skycar.LaunchActivity;

public class LoginUtils {
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
