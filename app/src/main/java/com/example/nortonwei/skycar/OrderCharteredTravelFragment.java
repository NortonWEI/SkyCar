package com.example.nortonwei.skycar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.Adapter.OrderCharteredTravelAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderCharteredTravelFragment extends Fragment {


    public OrderCharteredTravelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_order_chartered_travel, container, false);
        RecyclerView recyclerView = fragment.findViewById(R.id.order_chartered_recyclerView);
        OrderCharteredTravelAdapter adapter = new OrderCharteredTravelAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return fragment;
    }

}
