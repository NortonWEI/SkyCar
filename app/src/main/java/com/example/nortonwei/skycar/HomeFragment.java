package com.example.nortonwei.skycar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nortonwei.skycar.Adapter.HomeAdUltraPageAdapter;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ArrayList<String> adImageUrlList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        adImageUrlList.clear();

        HomeActivity.setUpActionBar((AppCompatActivity)getActivity(), getString(R.string.skycar));
        setUpButton(fragmentView);
        setUpCommentView(fragmentView);

        return fragmentView;
    }

    private void setUpButton(View fragmentView) {
        Button inquireFlightButton = (Button) fragmentView.findViewById(R.id.inquire_flight_button);
        SpannableString inquireFlightButtonText = new SpannableString(getString(R.string.inquire_flight) + "\n" + getString(R.string.inquire_flight_sub));
        inquireFlightButtonText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inquireFlightButtonText.setSpan(new ForegroundColorSpan(Color.GRAY), 5, inquireFlightButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inquireFlightButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, inquireFlightButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inquireFlightButton.setText(inquireFlightButtonText);

        Button translateLicenceButton = (Button) fragmentView.findViewById(R.id.translate_licence_button);
        SpannableString translateLicenceButtonText = new SpannableString(getString(R.string.translate_licence) + "\n" + getString(R.string.translate_licence_sub));
        translateLicenceButtonText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        translateLicenceButtonText.setSpan(new ForegroundColorSpan(Color.GRAY), 5, translateLicenceButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        translateLicenceButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, translateLicenceButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        translateLicenceButton.setText(translateLicenceButtonText);

        Button airportPickupButton = (Button) fragmentView.findViewById(R.id.ariport_pick_up_button);
        airportPickupButton.setOnClickListener(view -> {
            Intent intent = AirportPickupActivity.makeIntent(getContext());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        Button reserveCarButton = (Button) fragmentView.findViewById(R.id.reserve_car_button);
        reserveCarButton.setOnClickListener(view -> {
            Intent intent = ReserveCarActivity.makeIntent(getContext());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        Button companionBusButton = (Button) fragmentView.findViewById(R.id.companion_bus_button);
        companionBusButton.setOnClickListener(view -> {
            Intent intent = CompanionBusActivity.makeIntent(getContext());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        Button charteredTravelButton = (Button) fragmentView.findViewById(R.id.chartered_travel_button);
        charteredTravelButton.setOnClickListener(view -> {
            Intent intent = CharteredTravelActivity.makeIntent(getContext());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        inquireFlightButton.setOnClickListener(view -> {
            Intent intent = InquireFlightActivity.makeIntent(getContext());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        translateLicenceButton.setOnClickListener(view -> {
            Intent intent = LicenseTranslationActivity.makeIntent(getContext());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApiService service = retrofit.create(HttpApiService.class);

        Call<JsonObject> responseBodyCall = service.getIndexData();
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int status = Integer.parseInt(response.body().get("status").toString());

                if (status == HttpApiService.STATUS_OK) {
                    JsonObject data = (JsonObject) response.body().get("data");
                    JsonArray adList = data.get("adsList").getAsJsonArray();

                    for (JsonElement element: adList) {
                       JsonObject ad = element.getAsJsonObject();
                       adImageUrlList.add(ad.get("img").getAsString());
                    }

                    UltraViewPager ultraViewPager = (UltraViewPager) fragmentView.findViewById(R.id.ad_ultraViewPager);
                    ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                    PagerAdapter adapter = new HomeAdUltraPageAdapter(adImageUrlList, getContext());
                    ultraViewPager.setAdapter(adapter);
                    ultraViewPager.setAutoMeasureHeight(true);
                    ultraViewPager.initIndicator();
                    ultraViewPager.getIndicator()
                            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                            .setFocusColor(getResources().getColor(R.color.themeRed))
                            .setNormalColor(getResources().getColor(R.color.white))
                            .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                    ultraViewPager.getIndicator().build();

                    ultraViewPager.setInfiniteLoop(true);
                    ultraViewPager.setAutoScroll(2000);

                } else if (status == HttpApiService.STATUS_LOGOUT) {
                    SharedPreferences share = getContext().getSharedPreferences("Login",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putBoolean("isLogin", false);
                    editor.commit();

                    Intent intent = LaunchActivity.makeIntent(getContext());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "服务器繁忙，加载主页内容失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "网络错误，加载主页内容失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpCommentView(View fragmentView) {
        LinearLayout linearLayout = (LinearLayout) fragmentView.findViewById(R.id.user_comment_linear);

        for (int i=0; i<10; i++) {
            LayoutInflater vi = (LayoutInflater) fragmentView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.layout_user_comment_list, null);

            linearLayout.addView(v);
        }
    }
}
