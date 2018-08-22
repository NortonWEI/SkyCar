package com.example.nortonwei.skycar;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

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
    }

    private void setUpCommentView(View fragmentView) {
        LinearLayout linearLayout = (LinearLayout) fragmentView.findViewById(R.id.user_comment_linear);

        for (int i=0; i<10; i++) {
            LayoutInflater vi = (LayoutInflater) fragmentView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.layout_user_comment_list, null);

            linearLayout.addView(v);
        }
    }

//    private void setUpRecyclerView(View fragmentView) {
//        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.user_comment_recyclerView);
//        List<UserComment> userCommentList = new ArrayList<>();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
//
//        UserCommentAdapter adapter = new UserCommentAdapter(this.getContext(), userCommentList);
//        recyclerView.setAdapter(adapter);
//    }

}
