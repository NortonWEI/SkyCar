package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class CharteredTravelOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartered_travel_order);

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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionBar() {
        HomeActivity.setUpActionBar(this, getString(R.string.confirm_order));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.title_constraintLayout);
        TextView shareFeeTextView = (TextView) findViewById(R.id.share_fee_textView);
        TextView shareFeeInfoTextView = (TextView) findViewById(R.id.share_fee_info_textView);
        layout.removeView(shareFeeTextView);
        layout.removeView(shareFeeInfoTextView);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CharteredTravelOrderActivity.class);
    }
}
