package com.example.nortonwei.skycar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.nortonwei.skycar.Adapter.CarTypeUltraPagerAdapter;
import com.example.nortonwei.skycar.Customization.PaymentPopup;
import com.example.nortonwei.skycar.Model.Car;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReserveCarActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private boolean locationPermissionGranted = false;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean isFillInfoFinished = false;

    private RelativeLayout relativeLayout;
    private LayoutInflater inflater;
    private View fillInfoView;
    private View continueReserveView;
    private RelativeLayout.LayoutParams params;

    private ArrayList<Car> carList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_car);

        setUpActionBar();
        setUpUIComponents();

        if (isServiceOk()) {
            getLocationPermission();
        }
    }

    @Override
    public void finish() {
        if (isFillInfoFinished) {
            isFillInfoFinished = false;
            continueReserveView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.exit_to_right));
            fillInfoView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_left));

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ViewGroup)continueReserveView.getParent()).removeView(continueReserveView);
                    relativeLayout.addView(fillInfoView, params);
                }
            }, 300);
        } else {
            super.finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
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
        HomeActivity.setUpActionBar(this, getString(R.string.reserve_car));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        relativeLayout = (RelativeLayout) findViewById(R.id.reserve_car_relative);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fillInfoView = inflater.inflate(R.layout.layout_fill_reserve_car_info, relativeLayout,false);
        continueReserveView = inflater.inflate(R.layout.layout_continue_reserve_car, relativeLayout, false);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        relativeLayout.addView(fillInfoView, params);

        EditText timeEditText = fillInfoView.findViewById(R.id.depart_time_editText);
        timeEditText.setOnClickListener(view -> {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            endDate.set(2050,11,31);

            TimePickerView selectedTime = new TimePickerBuilder(ReserveCarActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    timeEditText.setText(date.toString());
                }
            })
                    .setType(new boolean[]{true, true, true, true, true, false})
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

        Button nextStepButton = (Button) fillInfoView.findViewById(R.id.reserve_next_button);
        nextStepButton.setOnClickListener(view -> {
            isFillInfoFinished = true;
            fillInfoView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.exit_to_left));
            continueReserveView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_right));

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ViewGroup)fillInfoView.getParent()).removeView(fillInfoView);
                    relativeLayout.addView(continueReserveView, params);
                }
            }, 300);

            UltraViewPager ultraViewPager = (UltraViewPager) continueReserveView.findViewById(R.id.car_type_ultraViewPager);
            ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
            PagerAdapter adapter = new CarTypeUltraPagerAdapter(true, ReserveCarActivity.this, carList);
            ultraViewPager.setAdapter(adapter);
            ultraViewPager.setMultiScreen(0.5f);
            ultraViewPager.setItemRatio(1.0f);
            ultraViewPager.setRatio(2.0f);
            ultraViewPager.setMaxHeight(800);
            ultraViewPager.setAutoMeasureHeight(true);
        });

        Button onlinePayButton = (Button) continueReserveView.findViewById(R.id.online_pay_button);
        SpannableString onlinePayButtonText = new SpannableString("¥123" + "\n" + getString(R.string.online_pay_hint));
        onlinePayButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, onlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        onlinePayButton.setText(onlinePayButtonText);

        Button offlinePayButton = (Button) continueReserveView.findViewById(R.id.offline_pay_button);
        SpannableString offlinePayButtonText = new SpannableString("$12" + "\n" + getString(R.string.offline_pay_hint));
        offlinePayButtonText.setSpan(new RelativeSizeSpan(0.8f), 4, offlinePayButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        offlinePayButton.setText(offlinePayButtonText);

        onlinePayButton.setOnClickListener(view -> {
            PaymentPopup popup = new PaymentPopup(this);
            popup.setUp();
            popup.show();
        });

        offlinePayButton.setOnClickListener(view -> {
            Intent intent = OfflinePaymentSuccessActivity.makeIntent(this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.reserve_car_map);
        mapFragment.getMapAsync(ReserveCarActivity.this);
    }

    private boolean isServiceOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ReserveCarActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Toast.makeText(ReserveCarActivity.this, "Google Play Services is working", Toast.LENGTH_SHORT).show();
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            GoogleApiAvailability.getInstance().getErrorDialog(ReserveCarActivity.this, available, ERROR_DIALOG_REQUEST).show();
        } else {
            Toast.makeText(ReserveCarActivity.this, "You can't make requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission() {
        String[] permissionList = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                setUpMap();
            } else {
                ActivityCompat.requestPermissions(this, permissionList, LOCATION_PERMISSION_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissionList, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionGranted = false;
                            return;
                        }
                    }
                    locationPermissionGranted = true;
                    setUpMap();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (locationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Location currentLocation = (Location) task.getResult();
                    if (currentLocation != null) {
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                    }
                } else {
                    Toast.makeText(ReserveCarActivity.this, "Unable to get your current location", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ReserveCarActivity.class);
    }
}
