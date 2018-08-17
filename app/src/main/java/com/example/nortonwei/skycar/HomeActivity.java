package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.layout_main_action_bar);
        setUpBottomNav();
    }

    static void setUpActionBar(AppCompatActivity activity, String title) {
        View view = activity.getLayoutInflater().inflate(R.layout.layout_main_action_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView textView = (TextView) view.findViewById(R.id.main_title);
        textView.setText(title);

        activity.getSupportActionBar().setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        activity.getSupportActionBar().setCustomView(view, params);
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpBottomNav() {
        BottomNavigationView mainBottomNavView = (BottomNavigationView) findViewById(R.id.main_bottom_nav_view);
        FrameLayout mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        HomeFragment homeFragment = new HomeFragment();
        BusFragment busFragment = new BusFragment();
        TripFragment tripFragment = new TripFragment();
        MyFragment myFragment = new MyFragment();

        setFragment(homeFragment);

        mainBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_skycar:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_bus:
                        setFragment(busFragment);
                        return true;
                    case R.id.nav_trip:
                        setFragment(tripFragment);
                        return true;
                    case R.id.nav_my:
                        setFragment(myFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }
}
