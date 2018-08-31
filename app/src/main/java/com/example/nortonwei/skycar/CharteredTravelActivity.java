package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.widget.Button;

import com.example.nortonwei.skycar.Adapter.CharteredTravelAdapter;

import java.util.ArrayList;

public class CharteredTravelActivity extends AppCompatActivity {
    private ArrayList<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartered_travel);

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
        HomeActivity.setUpActionBar(this, getString(R.string.choose_destination_city));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        RecyclerView destinationRecyclerView = (RecyclerView) findViewById(R.id.destination_recyclerView);
        Button wishListButton = (Button) findViewById(R.id.wish_list_button);

        CharteredTravelAdapter adapter = new CharteredTravelAdapter(dataList, this);
        destinationRecyclerView.setAdapter(adapter);
        destinationRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        wishListButton.setOnClickListener(view -> {
            Intent intent = WishListActivity.makeIntent(this);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CharteredTravelActivity.class);
    }
}
