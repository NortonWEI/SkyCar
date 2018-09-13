package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.nortonwei.skycar.Customization.PaymentPopup;

public class LicenseTranslationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_translation);

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
        HomeActivity.setUpActionBar(this, getString(R.string.translate_licence));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        Button payButton = (Button) findViewById(R.id.pay_button);
        Button uploadFrontButton = (Button) findViewById(R.id.upload_front_button);
        Button uploadBackButton = (Button) findViewById(R.id.upload_back_button);

        payButton.setOnClickListener(view -> {
            PaymentPopup popup = new PaymentPopup(this);
            popup.setUp();
            popup.show();
        });


    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, LicenseTranslationActivity.class);
    }
}
