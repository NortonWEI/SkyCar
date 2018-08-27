package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.crowdfire.cfalertdialog.CFAlertDialog;

import me.shaohui.bottomdialog.BottomDialog;

public class ConfirmCompanionBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_companion_bus);

        setUpActionBar();
        setUIComponent();
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
        HomeActivity.setUpActionBar(this, getString(R.string.confirm_bus));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUIComponent() {
        Button payButton = (Button) findViewById(R.id.pay_button);
        payButton.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    Button confirmButton = (Button) v.findViewById(R.id.online_payment_confirm_button);
                    ImageButton closeButton = (ImageButton) v.findViewById(R.id.online_payment_close_imageButton);

                    CheckBox skycarCheckBox = (CheckBox) v.findViewById(R.id.skycar_checkBox);
                    CheckBox wechatPayCheckBox = (CheckBox) v.findViewById(R.id.wechat_pay_checkBox);
                    CheckBox alipayCheckBox = (CheckBox) v.findViewById(R.id.alipay_checkBox);

                    ImageView skycarImageView = (ImageView) v.findViewById(R.id.skycar_imageView);
                    ImageView wechatPayImageView = (ImageView) v.findViewById(R.id.wechat_pay_imageView);
                    ImageView alipayImageView = (ImageView) v.findViewById(R.id.alipay_imageView);

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!skycarCheckBox.isChecked()) {
                                Intent intent = OnlinePaymentSuccessActivity.makeIntent(ConfirmCompanionBusActivity.this);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                bottomDialog.dismiss();
                            } else {
                                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(ConfirmCompanionBusActivity.this)
                                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                        .setHeaderView(R.layout.layout_payment_success_dialog_header)
                                        .setTitle(getString(R.string.confirm_skycar_pay))
                                        .setTextGravity(Gravity.CENTER_HORIZONTAL)
                                        .setMessage("立即支付5.8元\n" + getString(R.string.skycar_protect_you))
                                        .addButton(getString(R.string.confirm), getResources().getColor(R.color.themeRed), getResources().getColor(R.color.white), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                            dialog.dismiss();
                                            Intent intent = OnlinePaymentSuccessActivity.makeIntent(ConfirmCompanionBusActivity.this);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                            bottomDialog.dismiss();
                                        }).addButton(getString(R.string.cancel), getResources().getColor(R.color.carbon_grey_400), getResources().getColor(R.color.white), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                            dialog.dismiss();
                                        });

                                builder.show();
                            }
                        }
                    });

                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });

                    skycarCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (skycarCheckBox.isChecked()) {
                                skycarImageView.setImageResource(R.drawable.skycar_selected);
                                wechatPayImageView.setImageResource(R.drawable.wechat_unselected);
                                alipayImageView.setImageResource(R.drawable.alipay_unselected);
                                wechatPayCheckBox.setChecked(false);
                                alipayCheckBox.setChecked(false);
                            } else {
                                skycarImageView.setImageResource(R.drawable.skycar_unselected);
                            }
                        }
                    });

                    wechatPayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (wechatPayCheckBox.isChecked()) {
                                skycarImageView.setImageResource(R.drawable.skycar_unselected);
                                wechatPayImageView.setImageResource(R.drawable.wechat_selected);
                                alipayImageView.setImageResource(R.drawable.alipay_unselected);
                                skycarCheckBox.setChecked(false);
                                alipayCheckBox.setChecked(false);
                            } else {
                                wechatPayImageView.setImageResource(R.drawable.wechat_unselected);
                            }
                        }
                    });

                    alipayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (alipayCheckBox.isChecked()) {
                                skycarImageView.setImageResource(R.drawable.skycar_unselected);
                                wechatPayImageView.setImageResource(R.drawable.wechat_unselected);
                                alipayImageView.setImageResource(R.drawable.alipay_selected);
                                skycarCheckBox.setChecked(false);
                                wechatPayCheckBox.setChecked(false);
                            } else {
                                alipayImageView.setImageResource(R.drawable.alipay_unselected);
                            }
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_online_payment)
                    .show();
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ConfirmCompanionBusActivity.class);
    }
}
