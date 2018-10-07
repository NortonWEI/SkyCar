package com.example.nortonwei.skycar.Customization;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.nortonwei.skycar.OnlinePaymentSuccessActivity;
import com.example.nortonwei.skycar.R;

import org.w3c.dom.Text;

import me.shaohui.bottomdialog.BottomDialog;

public class PaymentPopup {
    Context context;
    float price;
    BottomDialog bottomDialog;

    public PaymentPopup(Context context, float price) {
        this.context = context;
        this.price = price;
    }

    public void setUp() {
        FragmentActivity activity = (FragmentActivity) context;

        bottomDialog = BottomDialog.create(activity.getSupportFragmentManager());

        bottomDialog.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                TextView priceTextView = (TextView) v.findViewById(R.id.price_textView);
                Button confirmButton = (Button) v.findViewById(R.id.online_payment_confirm_button);
                ImageButton closeButton = (ImageButton) v.findViewById(R.id.online_payment_close_imageButton);

                CheckBox skycarCheckBox = (CheckBox) v.findViewById(R.id.skycar_checkBox);
                CheckBox wechatPayCheckBox = (CheckBox) v.findViewById(R.id.wechat_pay_checkBox);
                CheckBox alipayCheckBox = (CheckBox) v.findViewById(R.id.alipay_checkBox);

                ImageView skycarImageView = (ImageView) v.findViewById(R.id.skycar_imageView);
                ImageView wechatPayImageView = (ImageView) v.findViewById(R.id.wechat_pay_imageView);
                ImageView alipayImageView = (ImageView) v.findViewById(R.id.alipay_imageView);

                priceTextView.setText("¥" + price);

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!skycarCheckBox.isChecked()) {
                            Intent intent = OnlinePaymentSuccessActivity.makeIntent(context);
                            context.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            bottomDialog.dismiss();
                        } else {
                            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context)
                                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                    .setHeaderView(R.layout.layout_payment_success_dialog_header)
                                    .setTitle(context.getString(R.string.confirm_skycar_pay))
                                    .setTextGravity(Gravity.CENTER_HORIZONTAL)
                                    .setMessage("立即支付5.8元\n" + context.getString(R.string.skycar_protect_you))
                                    .addButton(context.getString(R.string.confirm), context.getResources().getColor(R.color.themeRed), context.getResources().getColor(R.color.white), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                        dialog.dismiss();
                                        Intent intent = OnlinePaymentSuccessActivity.makeIntent(context);
                                        context.startActivity(intent);
                                        activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                        bottomDialog.dismiss();
                                    }).addButton(context.getString(R.string.cancel), context.getResources().getColor(R.color.carbon_grey_400), context.getResources().getColor(R.color.white), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
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
                .setLayoutRes(R.layout.layout_online_payment);
    }

    public void show() {
        bottomDialog.show();
    }
}
