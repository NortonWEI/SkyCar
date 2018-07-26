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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

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
        EditText countryRegionEditText = (EditText) findViewById(R.id.input_nickname_editText);
        EditText phoneNumberEditText = (EditText) findViewById(R.id.set_email_editText);

        countryRegionEditText.setText(getString(R.string.country_region) + "  ");
        countryRegionEditText.setCursorVisible(false);
        phoneNumberEditText.setText("           +86  ");

        countryRegionEditText.setOnClickListener(view -> {

        });

        phoneNumberEditText.setOnClickListener(view -> {
            phoneNumberEditText.setSelection(16);
        });

        phoneNumberEditText.setOnKeyListener((view, keyCode, keyEvent) -> {

            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (phoneNumberEditText.getText().length() <= 16) {
                    phoneNumberEditText.setText("           +86   ");
                    phoneNumberEditText.setSelection(17);
                }
            }
            return false;
        });

    }

    private void setUpButton() {
        Button nextButton = (Button) findViewById(R.id.next_auth_code_button);

        nextButton.setOnClickListener(view -> {
            Intent intent = AuthCodeActivity.makeIntent(SignUpActivity.this);
            startActivity(intent);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }
}
