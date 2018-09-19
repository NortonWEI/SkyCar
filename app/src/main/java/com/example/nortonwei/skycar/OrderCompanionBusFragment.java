package com.example.nortonwei.skycar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.Adapter.OrderCompanionBusAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderCompanionBusFragment extends Fragment {


    public OrderCompanionBusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_order_companion_bus, container, false);
        RecyclerView recyclerView = fragment.findViewById(R.id.order_bus_recyclerView);
        OrderCompanionBusAdapter adapter = new OrderCompanionBusAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return fragment;
    }

}
