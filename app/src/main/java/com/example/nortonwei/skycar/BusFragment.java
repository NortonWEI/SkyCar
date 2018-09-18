package com.example.nortonwei.skycar;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusFragment extends Fragment {

    public BusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_bus, container, false);
        HomeActivity.setUpActionBar((AppCompatActivity)getActivity(), getString(R.string.scheduled_bus));
        setUpSpinner(fragmentView);
        setUpBusListView(fragmentView);

        return fragmentView;
    }

    private void setUpSpinner(View fragmentView) {
        ArrayList<String> locationList = new ArrayList<>();
        locationList.add("Melbourne");
        locationList.add("Sydney");

        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.bus_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragmentView.getContext(), android.R.layout.simple_spinner_item, locationList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpBusListView(View fragmentView) {
        LinearLayout linearLayout = (LinearLayout) fragmentView.findViewById(R.id.bus_linear);

        for (int i=0; i<10; i++) {
            LayoutInflater vi = (LayoutInflater) fragmentView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.layout_bus_list, null);
            Button joinButton = (Button) v.findViewById(R.id.join_button);

            joinButton.setOnClickListener(view -> {
                Intent intent = CompanionBusOrderDetailActivity.makeIntent(getContext());
                startActivity(intent);  
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            });

            if (i == 9) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0,0,0,20);
                v.setLayoutParams(params);
            }

            linearLayout.addView(v);
        }
    }

}
