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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nortonwei.skycar.Adapter.HomeAdUltraPageAdapter;
import com.example.nortonwei.skycar.Customization.LoginUtils;
import com.example.nortonwei.skycar.HTTPClient.HttpApiService;
import com.example.nortonwei.skycar.Model.UserComment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private ArrayList<String> adImageUrlList = new ArrayList<>();
    private ArrayList<String> adClickUrlList = new ArrayList<>();
    private ArrayList<UserComment> userCommentList = new ArrayList<>();
    private boolean isAdFinishLoad = false;
    private boolean isCommentFinishLoad = false;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        HomeActivity.setUpActionBar((AppCompatActivity)getActivity(), getString(R.string.skycar));
        setUpUIComponents(fragmentView);

        return fragmentView;
    }

    private void setUpUIComponents(View fragmentView) {
        PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) fragmentView.findViewById(R.id.refresh_view_frame);
        ptrFrameLayout.setEnabledNextPtrAtOnce(false);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                setAdData(fragmentView);
                setUpCommentView(fragmentView);

                ptrFrameLayout.refreshComplete();
            }
        });

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

        setAdData(fragmentView);
        setUpCommentView(fragmentView);
    }

    private void setAdData(View fragmentView) {
        UltraViewPager ultraViewPager = (UltraViewPager) fragmentView.findViewById(R.id.ad_ultraViewPager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        PagerAdapter adapter = new HomeAdUltraPageAdapter(adImageUrlList, adClickUrlList, getContext());
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

        isAdFinishLoad = false;
        adImageUrlList.clear();
        adClickUrlList.clear();
        ultraViewPager.refresh();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApiService service = retrofit.create(HttpApiService.class);

        Call<JsonObject> responseBodyCall = service.getIndexData();
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int status = response.body().get("status").getAsInt();

                if (status == HttpApiService.STATUS_OK) {
                    JsonObject data = (JsonObject) response.body().get("data");
                    JsonArray adList = data.get("adsList").getAsJsonArray();

                    for (JsonElement element: adList) {
                        JsonObject ad = element.getAsJsonObject();
                        adImageUrlList.add(ad.get("img").getAsString());
                        adClickUrlList.add(ad.get("url").getAsString());
                    }
                    ultraViewPager.refresh();

                } else if (status == HttpApiService.STATUS_LOGOUT) {
                    LoginUtils.logout(getContext());
                } else {
                    Toast.makeText(getContext(), "服务器繁忙，加载主页内容失败！", Toast.LENGTH_SHORT).show();
                }
                isAdFinishLoad = true;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "网络错误，加载主页内容失败！", Toast.LENGTH_SHORT).show();
                isAdFinishLoad = true;
            }
        });
    }

    private void setUpCommentView(View fragmentView) {
        isCommentFinishLoad = false;
        userCommentList.clear();

        LinearLayout linearLayout = (LinearLayout) fragmentView.findViewById(R.id.user_comment_linear);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApiService service = retrofit.create(HttpApiService.class);

        SharedPreferences share = getContext().getSharedPreferences("Login",
                Context.MODE_PRIVATE);

        Call<JsonObject> responseBodyCall = service.getUserComment(share.getString("token",""));
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int status = response.body().get("status").getAsInt();

                if (status == HttpApiService.STATUS_OK) {
                    linearLayout.removeViews(2, linearLayout.getChildCount()-2);

                    JsonObject data = response.body().get("data").getAsJsonObject();
                    JsonArray commentList = data.get("commentList").getAsJsonArray();

                    for (JsonElement element: commentList) {
                        JsonObject comment = element.getAsJsonObject();
                        UserComment commentModel = new UserComment(comment.get("id").getAsInt(),
                                comment.get("nickname").getAsString(),
                                comment.get("headimg").getAsString(),
                                comment.get("star").getAsInt(),
                                comment.get("comment").getAsString(),
                                comment.get("create_time").getAsString());
                        userCommentList.add(commentModel);
                    }

                    for (int i=0; i<userCommentList.size(); i++) {
                        LayoutInflater vi = (LayoutInflater) fragmentView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = vi.inflate(R.layout.layout_user_comment_list, null);
                        CircleImageView userIconImageView = (CircleImageView) v.findViewById(R.id.user_icon_imageView);
                        TextView usernameTextView = (TextView) v.findViewById(R.id.name_textView);
                        TextView createTimeTextView = (TextView) v.findViewById(R.id.time_textView);
                        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
                        TextView commentTextView = (TextView) v.findViewById(R.id.comment_textView);

                        Glide.with(getContext()).load(userCommentList.get(i).getHeadImg()).into(userIconImageView);
                        usernameTextView.setText(userCommentList.get(i).getNickname());
                        createTimeTextView.setText(userCommentList.get(i).getCreateTime());
                        ratingBar.setRating(userCommentList.get(i).getStar());
                        commentTextView.setText(userCommentList.get(i).getComment());

                        linearLayout.addView(v);
                    }
                } else if (status == HttpApiService.STATUS_LOGOUT) {
                    LoginUtils.logout(getContext());
                } else {
                    Toast.makeText(getContext(), "网络错误，加载主页内容失败！", Toast.LENGTH_SHORT).show();
                }
                isCommentFinishLoad = true;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                isCommentFinishLoad = true;
            }
        });
    }
}
