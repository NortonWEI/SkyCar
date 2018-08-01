package com.example.nortonwei.skycar;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.nortonwei.skycar.Adapter.UserCommentAdapter;
import com.example.nortonwei.skycar.Model.UserComment;

import java.util.ArrayList;
import java.util.List;


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
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        setUpButton(fragmentView);
//        setUpRecyclerView(fragmentView);
        LinearLayout linearLayout = (LinearLayout) fragmentView.findViewById(R.id.test_linear);

        for (int i=0; i<10; i++) {
            LayoutInflater vi = (LayoutInflater) fragmentView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.user_comment_list_layout, null);

            linearLayout.addView(v);
        }

        return fragmentView;
    }

    private void setUpButton(View fragmentView) {
        Button inquireFlightButton = fragmentView.findViewById(R.id.inquire_flight_button);
        SpannableString inquireFlightButtonText = new SpannableString(getString(R.string.inquire_flight) + "\n" + getString(R.string.inquire_flight_sub));
        inquireFlightButtonText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inquireFlightButtonText.setSpan(new ForegroundColorSpan(Color.GRAY), 5, inquireFlightButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inquireFlightButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, inquireFlightButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inquireFlightButton.setText(inquireFlightButtonText);

        Button translateLicenceButton = fragmentView.findViewById(R.id.translate_licence_button);
        SpannableString translateLicenceButtonText = new SpannableString(getString(R.string.translate_licence) + "\n" + getString(R.string.translate_licence_sub));
        translateLicenceButtonText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        translateLicenceButtonText.setSpan(new ForegroundColorSpan(Color.GRAY), 5, translateLicenceButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        translateLicenceButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, translateLicenceButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        translateLicenceButton.setText(translateLicenceButtonText);
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