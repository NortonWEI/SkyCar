package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nortonwei.skycar.Adapter.FrequentPassengerListAdapter;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.example.nortonwei.skycar.Model.Contact;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FrequentPassengerActivity extends AppCompatActivity {
    private static final int REFRESH_REQUEST_CODE = 8001;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    private ProgressBar progressBar;
    private ListView contactListView;
    FrequentPassengerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequent_passenger);

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
        PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.refresh_view_frame);
        contactListView = (ListView) findViewById(R.id.contact_ListView);
        Button addFrequentPassengerButton = (Button) findViewById(R.id.add_frequent_passenger_button);
        addFrequentPassengerButton.setOnClickListener(view -> {
            Intent intent = AddFrequentPassengerActivity.makeIntent(FrequentPassengerActivity.this);
            startActivityForResult(intent, REFRESH_REQUEST_CODE);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        ptrFrameLayout.setEnabledNextPtrAtOnce(false);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                setUpContactListView();

                ptrFrameLayout.refreshComplete();
            }
        });

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.layout_frequent_passenger_header, null);
        contactListView.addHeaderView(header);

        adapter = new FrequentPassengerListAdapter(this, contactsList);

        contactListView.setAdapter(adapter);
        setUpContactListView();
    }

    private void setUpContactListView() {
        contactsList.clear();
        adapter.notifyDataSetChanged();

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

        Call<JsonObject> responseBodyCall = service.getContact(share.getString("token", ""));
        responseBodyCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int status = response.body().get("status").getAsInt();
                String msg = response.body().get("msg").getAsString();

                if (status == HttpApiService.STATUS_OK) {
                    JsonArray data = response.body().get("data").getAsJsonArray();

                    for (JsonElement element: data) {
                        JsonObject contact = element.getAsJsonObject();
                        contactsList.add(new Contact(contact.get("id").getAsString(), contact.get("name").getAsString(), contact.get("mobile").getAsString()));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(FrequentPassengerActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FrequentPassengerActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REFRESH_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    setUpContactListView();
                }
                break;
            default:
                break;
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, FrequentPassengerActivity.class);
    }
}
