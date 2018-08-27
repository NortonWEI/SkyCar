package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jmf.addsubutils.AddSubUtils;

import me.shaohui.bottomdialog.BottomDialog;

public class JoinCompanionBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_companion_bus);

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
        HomeActivity.setUpActionBar(this, getString(R.string.join_bus));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        Button continueButton = (Button) findViewById(R.id.continue_button);
        Button frequentContactButton = (Button) findViewById(R.id.frequent_contact_button);
        EditText passengerNumberEditText = (EditText) findViewById(R.id.passenger_number_editText);
        EditText luggageNumberEditText = (EditText) findViewById(R.id.luggage_number_editText);

        continueButton.setOnClickListener(view -> {
            Intent intent = ConfirmCompanionBusActivity.makeIntent(this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        frequentContactButton.setOnClickListener(view -> {
            Intent intent = FrequentPassengerActivity.makeIntent(this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        passengerNumberEditText.setOnClickListener(view -> {
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
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, JoinCompanionBusActivity.class);
    }
}
