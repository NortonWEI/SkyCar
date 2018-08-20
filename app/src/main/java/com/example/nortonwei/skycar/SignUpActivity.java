package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.ArrayList;
import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity {
    int countryChoice = 0;
    final String[] countryList = {"澳大利亚", "新西兰", "中国", "中国香港", "中国澳门", "中国台湾", "蒙古", "朝鲜", "韩国", "日本", "菲律宾", "越南", "老挝", "柬埔寨", "缅甸", "泰国", "马来西亚", "文莱", "新加坡", "印度尼西亚", "东帝汶"};
    final String[] countryCodeList = {"+61", "+64", "+86", "+852", "+853", "+886", "+976", "+850", "+82", "+81", "+63", "+84", "+856", "+855", "+95", "+66", "+60", "+673", "+65", "+62", "+670"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpActionBar();
        setUpHyperLink();
        setUpEditText();
        setUpButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_up_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case R.id.login_menu_button:
                Intent intent = LoginActivity.makeIntent(SignUpActivity.this);
                startActivity(intent);
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

    private void setUpHyperLink() {
        TextView conditionTermTextView = (TextView) findViewById(R.id.condition_term_textView);
        conditionTermTextView.setText(getClickableSpan());
        conditionTermTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private SpannableString getClickableSpan() {
        SpannableString spannableString = new SpannableString(getString(R.string.condition_term));

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                    Toast.makeText(MainActivity.this,"使用条款",Toast.LENGTH_SHORT).show();
            }
        }, 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                    Toast.makeText(MainActivity.this,"隐私政策",Toast.LENGTH_SHORT).show();
            }
        }, 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void setUpEditText() {
        EditText countryRegionEditText = (EditText) findViewById(R.id.country_region_editText);
        EditText phoneNumberEditText = (EditText) findViewById(R.id.sign_up_phone_editText);
        EditText countryRegionHintEditText = (EditText) findViewById(R.id.country_region_hint_editText);
        EditText countryCodeEditText = (EditText) findViewById(R.id.country_code_editText);

        countryRegionEditText.setCursorVisible(false);
        countryRegionEditText.setText(countryList[countryChoice]);
        countryRegionHintEditText.setText(getString(R.string.country_region) + "  ");
        countryCodeEditText.setText(countryCodeList[countryChoice] + "  ");

        countryRegionEditText.setOnClickListener(view -> {
            ArrayList countryArrayList = new ArrayList(Arrays.asList(countryList));

            OptionsPickerView countrySelectPicker = new OptionsPickerBuilder(SignUpActivity.this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                    countryChoice = options1;
                    countryRegionEditText.setText(countryList[options1]);
                    countryCodeEditText.setText(countryCodeList[options1] + "  ");
                }
            })
                    .setCancelText(getString(R.string.cancel))
                    .setSubmitText(getString(R.string.confirm))
                    .setCancelColor(getResources().getColor(R.color.themeRed))
                    .setSubmitColor(getResources().getColor(R.color.themeRed))
                    .setTitleText(getString(R.string.choose_number_region))
                    .setSelectOptions(countryChoice)
                    .build();
            countrySelectPicker.setPicker(countryArrayList);
            countrySelectPicker.show();
        });

        phoneNumberEditText.setOnClickListener(view -> {

        });
    }

    private void setUpButton() {
        Button nextButton = (Button) findViewById(R.id.next_auth_code_button);

        nextButton.setOnClickListener(view -> {
            Intent intent = AuthCodeActivity.makeIntent(SignUpActivity.this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }
}
