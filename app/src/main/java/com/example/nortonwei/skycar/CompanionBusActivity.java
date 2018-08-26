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

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CompanionBusActivity extends AppCompatActivity {
    private String[] acceptableRangeOptions = {"+-15分钟", "+-30分钟"};
    private int acceptableRangeChoice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companion_bus);

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
        HomeActivity.setUpActionBar(this, getString(R.string.companion_bus));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        EditText whereToStartEditText = (EditText) findViewById(R.id.where_to_start_editText);
        EditText whereToGoEditText = (EditText) findViewById(R.id.where_to_go_editText);
        EditText departTimeEditText = (EditText) findViewById(R.id.depart_time_editText);
        EditText acceptableRangeEditText = (EditText) findViewById(R.id.acceptable_range_editText);
        EditText expectedTimeEditText = (EditText) findViewById(R.id.expected_time_editText);
        Button commonAddressStartButton = (Button) findViewById(R.id.common_address_start_button);
        Button commonAddressGoButton = (Button) findViewById(R.id.common_address_go_button);
        Button nextStepButton = (Button) findViewById(R.id.next_step_button);

        departTimeEditText.setOnClickListener(view -> {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            endDate.set(2050,11,31);

            TimePickerView selectedTime = new TimePickerBuilder(CompanionBusActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    departTimeEditText.setText(date.toString());
                }
            })
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .setLabel("年","月","日","时","分","秒")
                    .setCancelText(getString(R.string.cancel))
                    .setSubmitText(getString(R.string.confirm))
                    .setCancelColor(getResources().getColor(R.color.themeRed))
                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                    .setTitleText(getString(R.string.depart_time))
                    .setRangDate(startDate, endDate)
                    .isCenterLabel(true)
                    .build();
            selectedTime.show();
        });

        acceptableRangeEditText.setOnClickListener(view -> {
            ArrayList acceptableRangeOptionsArrayList = new ArrayList(Arrays.asList(acceptableRangeOptions));

            OptionsPickerView rangeSelectPicker = new OptionsPickerBuilder(CompanionBusActivity.this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                    acceptableRangeChoice = options1;
                    acceptableRangeEditText.setText(acceptableRangeOptions[options1]);
                }
            })
                    .setCancelText(getString(R.string.cancel))
                    .setSubmitText(getString(R.string.confirm))
                    .setCancelColor(getResources().getColor(R.color.themeRed))
                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                    .setTitleText(getString(R.string.acceptable_time_range))
                    .setSelectOptions(acceptableRangeChoice)
                    .build();
            rangeSelectPicker.setPicker(acceptableRangeOptionsArrayList);
            rangeSelectPicker.show();
        });

        expectedTimeEditText.setOnClickListener(view -> {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            endDate.set(2050,11,31);

            TimePickerView selectedTime = new TimePickerBuilder(CompanionBusActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    expectedTimeEditText.setText(date.toString());
                }
            })
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .setLabel("年","月","日","时","分","秒")
                    .setCancelText(getString(R.string.cancel))
                    .setSubmitText(getString(R.string.confirm))
                    .setCancelColor(getResources().getColor(R.color.themeRed))
                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                    .setTitleText(getString(R.string.expected_arrive_time))
                    .setRangDate(startDate, endDate)
                    .isCenterLabel(true)
                    .build();
            selectedTime.show();
        });

        commonAddressStartButton.setOnClickListener(view -> {
            Intent intent = FrequentAddressActivity.makeIntent(this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        commonAddressGoButton.setOnClickListener(view -> {
            Intent intent = FrequentAddressActivity.makeIntent(this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        nextStepButton.setOnClickListener(view -> {
            Intent intent = CompanionBusResultActivity.makeIntent(this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CompanionBusActivity.class);
    }
}
