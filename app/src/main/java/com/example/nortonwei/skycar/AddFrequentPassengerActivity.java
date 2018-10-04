package com.example.nortonwei.skycar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nortonwei.skycar.Customization.LoginUtils;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddFrequentPassengerActivity extends AppCompatActivity {
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<String> countryCodeList = new ArrayList<>();
    private ProgressBar progressBar;
    private Spinner phoneCodeSpinner;
    private EditText nameEditText;
    private EditText phoneNumberEditText;

    private static final int PICK_CONTACT = 8001;
    private static final int CONTACT_REQUESTS_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_frequent_passenger);

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
        HomeActivity.setUpActionBar(this, getString(R.string.passenger));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        phoneCodeSpinner = (Spinner) findViewById(R.id.country_code_spinner);
        nameEditText = (EditText) findViewById(R.id.name_editText);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number_editText);

        setUpCountryCodeList();

        Button confirmButton = (Button) findViewById(R.id.add_frequent_passenger_confirm_button);
        confirmButton.setOnClickListener(view -> {
            if (nameEditText.getText().toString().isEmpty() || phoneNumberEditText.getText().toString().isEmpty()) {
                Toast.makeText(AddFrequentPassengerActivity.this, "请填写姓名及手机号", Toast.LENGTH_SHORT).show();
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HttpApiService.BASE_URL)
                        .client(HttpApiService.client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                HttpApiService service = retrofit.create(HttpApiService.class);

                SharedPreferences share = getSharedPreferences("Login",
                        Context.MODE_PRIVATE);

                Log.d("token", share.getString("token", ""));

                Call<JsonObject> responseBodyCall = service.addContact(share.getString("token", ""),
                        nameEditText.getText().toString(),
                        phoneCodeSpinner.getSelectedItem().toString() + " " + phoneNumberEditText.getText().toString());
                responseBodyCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        int status = response.body().get("status").getAsInt();
                        String msg = response.body().get("msg").getAsString();

                        if (status == HttpApiService.STATUS_OK) {
                            finish();
                        } else if (status == HttpApiService.STATUS_LOGOUT) {
                            LoginUtils.logout(AddFrequentPassengerActivity.this);
                            Toast.makeText(AddFrequentPassengerActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddFrequentPassengerActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddFrequentPassengerActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button addressBookButton = (Button) findViewById(R.id.address_book_button);
        addressBookButton.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(AddFrequentPassengerActivity.this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddFrequentPassengerActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        CONTACT_REQUESTS_CODE);
            } else {
                Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
    }

    private void setUpCountryCodeList() {
        countryList.clear();
        countryCodeList.clear();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpApiService.BASE_URL)
                .client(HttpApiService.client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApiService service = retrofit.create(HttpApiService.class);

        Call<JsonObject> responseBodyCall = service.getCountryList();
        responseBodyCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int status = response.body().get("status").getAsInt();
                String msg = response.body().get("msg").getAsString();

                if (status == HttpApiService.STATUS_OK) {
                    JsonArray data = response.body().get("data").getAsJsonArray();

                    for (JsonElement element: data) {
                        countryList.add(element.getAsJsonObject().get("name").getAsString());
                        countryCodeList.add(element.getAsJsonObject().get("val").getAsString());
                    }
                    if (!countryList.isEmpty() && !countryCodeList.isEmpty()) {
                        ArrayAdapter<String> phoneCodeAdapter = new ArrayAdapter<String>(AddFrequentPassengerActivity.this, android.R.layout.simple_spinner_item, countryCodeList);
                        phoneCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        phoneCodeSpinner.setAdapter(phoneCodeAdapter);
                    }
                } else {
                    Toast.makeText(AddFrequentPassengerActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddFrequentPassengerActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == RESULT_OK && data != null) {
                    Uri contactData = data.getData();
                    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String phoneNumber = "";

                        if ( hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            phones.close();
                        }

                        nameEditText.setText(name);
                        phoneNumberEditText.setText(phoneNumber);
                    }
                    cursor.close();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONTACT_REQUESTS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            } else {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddFrequentPassengerActivity.class);
    }
}
