package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

public class AirportPickupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiport_pickup);

        setUpActionBar();
        setUpButton();
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionBar() {
        HomeActivity.setUpActionBar(this, getString(R.string.airport_pick_up));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpButton() {
        Button pickUpButton = (Button) findViewById(R.id.pick_up_button);
        Button dropOffButton = (Button) findViewById(R.id.drop_off_button);
        Button roundTripButton = (Button) findViewById(R.id.round_trip_button);

        pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_selected), null,null);

        pickUpButton.setOnClickListener(view -> {
            if (pickUpButton.getCompoundDrawables()[1].getConstantState().equals(getResources().getDrawable(R.drawable.pickup_unselected).getConstantState())) {
                pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_selected), null,null);
                dropOffButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.dropoff_unselected), null,null);
                roundTripButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.round_unselected), null,null);
            }
        });

        dropOffButton.setOnClickListener(view -> {
            if (dropOffButton.getCompoundDrawables()[1].getConstantState().equals(getResources().getDrawable(R.drawable.dropoff_unselected).getConstantState())) {
                pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_unselected), null,null);
                dropOffButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.dropoff_selected), null,null);
                roundTripButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.round_unselected), null,null);
            }
        });

        roundTripButton.setOnClickListener(view -> {
            if (roundTripButton.getCompoundDrawables()[1].getConstantState().equals(getResources().getDrawable(R.drawable.round_unselected).getConstantState())) {
                pickUpButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.pickup_unselected), null,null);
                dropOffButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.dropoff_unselected), null,null);
                roundTripButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.round_selected), null,null);
            }
        });
    }

    public static Intent makeIntent(Context context) {
            return new Intent(context, AirportPickupActivity.class);
    }
}
