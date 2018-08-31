package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.nortonwei.skycar.Adapter.ListDropDownAdapter;
import com.example.nortonwei.skycar.Adapter.WishListAdapter;
import com.yyydjk.library.DropDownMenu;


public class WishListActivity extends AppCompatActivity {
    private String[] menuHeaders = {"城市", "路线", "天数", "关键字"};
    private String cities[] = {"不限", "霍巴特", "凯恩斯", "堪培拉", "布里斯班", "奥克兰", "阿德莱德"};
    private String routes[] = {"不限", "小众", "经典"};
    private String days[] = {"不限", "1天", "2天", "3天", "4-6天", "7天及以上"};
    private String themes[] = {"不限", "丰收果园", "沙滩海浪", "野餐BBQ", "国家公园"};

    private ListDropDownAdapter cityAdapter;
    private ListDropDownAdapter routeAdapter;
    private ListDropDownAdapter dayAdapter;
    private ListDropDownAdapter themeAdapter;

    private List<View> popupViews = new ArrayList<>();
    private DropDownMenu dropDownMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

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
        HomeActivity.setUpActionBar(this, getString(R.string.my_wish_list));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        dropDownMenu = (DropDownMenu) findViewById(R.id.drop_down_menu);

        final ListView cityView = new ListView(this);
        cityAdapter = new ListDropDownAdapter(this, Arrays.asList(cities));
        cityView.setDividerHeight(0);
        cityView.setAdapter(cityAdapter);

        final ListView routeView = new ListView(this);
        routeAdapter = new ListDropDownAdapter(this, Arrays.asList(routes));
        routeView.setDividerHeight(0);
        routeView.setAdapter(routeAdapter);

        final ListView dayView = new ListView(this);
        dayAdapter = new ListDropDownAdapter(this, Arrays.asList(days));
        dayView.setDividerHeight(0);
        dayView.setAdapter(dayAdapter);

        final ListView themeView = new ListView(this);
        themeAdapter = new ListDropDownAdapter(this, Arrays.asList(themes));
        themeView.setDividerHeight(0);
        themeView.setAdapter(themeAdapter);

        popupViews.add(cityView);
        popupViews.add(routeView);
        popupViews.add(dayView);
        popupViews.add(themeView);

        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setCheckItem(position);
                dropDownMenu.setTabText(position == 0 ? menuHeaders[0] : cities[position]);
                dropDownMenu.closeMenu();
            }
        });

        routeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                routeAdapter.setCheckItem(position);
                dropDownMenu.setTabText(position == 0 ? menuHeaders[1] : routes[position]);
                dropDownMenu.closeMenu();
            }
        });

        dayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dayAdapter.setCheckItem(position);
                dropDownMenu.setTabText(position == 0 ? menuHeaders[2] : days[position]);
                dropDownMenu.closeMenu();
            }
        });

        themeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                themeAdapter.setCheckItem(position);
                dropDownMenu.setTabText(position == 0 ? menuHeaders[3] : themes[position]);
                dropDownMenu.closeMenu();
            }
        });

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View wishListView = vi.inflate(R.layout.layout_wish_list_recycler_view, null, false);

        dropDownMenu.setDropDownMenu(Arrays.asList(menuHeaders), popupViews, wishListView);

        RecyclerView wishListRecyclerView = (RecyclerView) wishListView.findViewById(R.id.wish_list_recyclerView);
        wishListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        WishListAdapter adapter = new WishListAdapter(this);
        wishListRecyclerView.setAdapter(adapter);
        wishListRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onBackPressed() {
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, WishListActivity.class);
    }
}
