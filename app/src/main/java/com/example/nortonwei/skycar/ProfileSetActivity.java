package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ProfileSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);

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

    private void setUpButton() {
        Button nextButton = (Button) findViewById(R.id.next_set_password_button);
        ImageButton maleButton = (ImageButton) findViewById(R.id.male_imageButton);
        ImageButton femaleButton = (ImageButton) findViewById(R.id.female_imageButton);

        nextButton.setOnClickListener(view -> {
            Intent intent = PasswordSetActivity.makeIntent(ProfileSetActivity.this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        maleButton.setOnClickListener(view -> {
            if (maleButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.male_unchecked).getConstantState())) {
                maleButton.setImageResource(R.drawable.male_selected);
                femaleButton.setImageResource(R.drawable.female_unchecked);
            } else if (maleButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.male_selected).getConstantState())) {
                maleButton.setImageResource(R.drawable.male_unchecked);
            }
        });

        femaleButton.setOnClickListener(view -> {
            if (femaleButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.female_unchecked).getConstantState())) {
                femaleButton.setImageResource(R.drawable.female_selected);
                maleButton.setImageResource(R.drawable.male_unchecked);
            } else if (femaleButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.female_selected).getConstantState())) {
                femaleButton.setImageResource(R.drawable.female_unchecked);
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ProfileSetActivity.class);
    }
}
