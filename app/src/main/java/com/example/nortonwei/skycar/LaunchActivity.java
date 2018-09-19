package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences share = getSharedPreferences("Login",
                Context.MODE_PRIVATE);

//        SharedPreferences.Editor editor = share.edit();
//        editor.putBoolean("isLogin", false);
//        editor.commit();

        if (share.getBoolean("isLogin", false)) {
            Intent intent = HomeActivity.makeIntent(LaunchActivity.this);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_launch);
        setupUIComponents();
    }

    private void setupUIComponents() {
        Button loginButton = findViewById(R.id.login_button);
        Button signUpButton = findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(view -> {
            Intent intent = SignUpActivity.makeIntent(LaunchActivity.this);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            Intent intent = LoginActivity.makeIntent(LaunchActivity.this);
            startActivity(intent);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, LaunchActivity.class);
    }
}
