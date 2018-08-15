package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.widget.Button;

public class ConfirmTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_trip);

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
        HomeActivity.setUpActionBar(this, getString(R.string.confirm_trip));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        Button onlinePayButton = (Button) findViewById(R.id.online_pay_button);
        SpannableString onlinePayButtonText = new SpannableString("¥123" + "\n" + getString(R.string.online_pay_hint));
//        onlinePayButtonText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        onlinePayButtonText.setSpan(new ForegroundColorSpan(Color.GRAY), 5, onlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        onlinePayButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, onlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        onlinePayButton.setText(onlinePayButtonText);

        Button offlinePayButton = (Button) findViewById(R.id.offline_pay_button);
        SpannableString offlinePayButtonText = new SpannableString("$12" + "\n" + getString(R.string.offline_pay_hint));
//        offlinePayButtonText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        offlinePayButtonText.setSpan(new ForegroundColorSpan(Color.GRAY), 5, offlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        offlinePayButtonText.setSpan(new RelativeSizeSpan(0.8f), 4, offlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        offlinePayButton.setText(offlinePayButtonText);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ConfirmTripActivity.class);
    }
}
