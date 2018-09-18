package com.example.nortonwei.skycar;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nortonwei.skycar.Adapter.OrderViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripFragment extends Fragment {


    public TripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_trip, container, false);
        HomeActivity.setUpActionBar((AppCompatActivity)getActivity(), getString(R.string.my_trip));

        TabLayout tabLayout = (TabLayout) fragmentView.findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) fragmentView.findViewById(R.id.viewPager);
        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new OrderAirportPickupFragment(), getString(R.string.airport_pick_up));
        adapter.addFragment(new OrderReserveCarFragment(), getString(R.string.reserve_car));
        adapter.addFragment(new OrderCompanionBusFragment(), getString(R.string.companion_bus));
        adapter.addFragment(new OrderCharteredTravelFragment(), getString(R.string.chartered_travel));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return fragmentView;
    }

}
