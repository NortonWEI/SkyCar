package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jmf.addsubutils.AddSubUtils;

import me.shaohui.bottomdialog.BottomDialog;


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
        onlinePayButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, onlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        onlinePayButton.setText(onlinePayButtonText);

        Button offlinePayButton = (Button) findViewById(R.id.offline_pay_button);
        SpannableString offlinePayButtonText = new SpannableString("$12" + "\n" + getString(R.string.offline_pay_hint));
        offlinePayButtonText.setSpan(new RelativeSizeSpan(0.8f), 4, offlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        offlinePayButton.setText(offlinePayButtonText);

        EditText peopleNumberEditText = (EditText) findViewById(R.id.people_number_editText);
        EditText luggageNumberEditText = (EditText) findViewById(R.id.luggage_number_editText);

        peopleNumberEditText.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                        @Override
                        public void bindView(View v) {
                            Button confirmButton = (Button) v.findViewById(R.id.people_number_confirm_button);
                            Button cancelButton = (Button) v.findViewById(R.id.people_number_cancel_button);

                            AddSubUtils adultAddSubUtils = (AddSubUtils) v.findViewById(R.id.adult_add_sub);
                            adultAddSubUtils.setBuyMax(30)
                                    .setBuyMin(0)
                                    .setCurrentNumber(0);

                            AddSubUtils sevenPlusAddSubUtils = (AddSubUtils) v.findViewById(R.id.seven_plus_child_add_sub);
                            sevenPlusAddSubUtils.setBuyMax(30)
                                    .setBuyMin(0)
                                    .setCurrentNumber(0);

                            AddSubUtils sevenMinusAddSubUtils = (AddSubUtils) v.findViewById(R.id.seven_minus_child_add_sub);
                            sevenMinusAddSubUtils.setBuyMax(30)
                                    .setBuyMin(0)
                                    .setCurrentNumber(0);

                            confirmButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bottomDialog.dismiss();
                                }
                            });

                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bottomDialog.dismiss();
                                }
                            });
                        }
                    })
                    .setLayoutRes(R.layout.layout_choose_people_number)
                    .show();
        });

        luggageNumberEditText.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    Button confirmButton = (Button) v.findViewById(R.id.luggage_number_confirm_button);
                    Button cancelButton = (Button) v.findViewById(R.id.luggage_number_cancel_button);

                    AddSubUtils bigLuggageAddSubUtils = (AddSubUtils) v.findViewById(R.id.big_luggage_add_sub);
                    bigLuggageAddSubUtils.setBuyMax(30)
                            .setBuyMin(0)
                            .setCurrentNumber(0);

                    AddSubUtils smallLuggageAddSubUtils = (AddSubUtils) v.findViewById(R.id.small_luggage_add_sub);
                    smallLuggageAddSubUtils.setBuyMax(30)
                            .setBuyMin(0)
                            .setCurrentNumber(0);

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_choose_luggage_number)
                    .show();
        });

        Button frequentPassengerButton = (Button) findViewById(R.id.frequent_passenger_button);
        frequentPassengerButton.setOnClickListener(view -> {
            Intent intent = FrequentPassengerActivity.makeIntent(this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        onlinePayButton.setOnClickListener(view -> {
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
                            bottomDialog.dismiss();
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
        return new Intent(context, ConfirmTripActivity.class);
    }
}
