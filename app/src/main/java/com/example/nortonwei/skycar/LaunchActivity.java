package com.example.nortonwei.skycar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
