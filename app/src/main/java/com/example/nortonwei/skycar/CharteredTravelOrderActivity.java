package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nortonwei.skycar.Customization.PaymentPopup;

import me.shaohui.bottomdialog.BottomDialog;

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
        String parentActivity = getIntent().getStringExtra("parentActivity");

        switch (parentActivity) {
            case "chartered":
                ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.title_constraintLayout);
                TextView shareFeeTextView = (TextView) findViewById(R.id.share_fee_textView);
                TextView shareFeeInfoTextView = (TextView) findViewById(R.id.share_fee_info_textView);
                constraintLayout.removeView(shareFeeTextView);
                constraintLayout.removeView(shareFeeInfoTextView);
                break;
            case "group":
                LinearLayout parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linearLayout);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.choose_car_type_linearLayout);
                parentLinearLayout.removeView(linearLayout);
                break;
            default:
                break;
        }

        TextView priceDetailTextView = (TextView) findViewById(R.id.price_detail_textView);
        priceDetailTextView.setPaintFlags(priceDetailTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        priceDetailTextView.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    ImageButton closeButton = (ImageButton) v.findViewById(R.id.cancel_button);

                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_price_detail)
                    .show();
        });

        Button payButton = (Button) findViewById(R.id.pay_button);
        payButton.setOnClickListener(view -> {
            PaymentPopup popup = new PaymentPopup(this, 198.0f);
            popup.setUp();
            popup.show();
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CharteredTravelOrderActivity.class);
    }
}
