package com.example.nortonwei.skycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.nortonwei.skycar.Adapter.JourneyRecommendationAdapter;
import com.example.nortonwei.skycar.Customization.ExpandTextView;
import com.example.nortonwei.skycar.Customization.ScreenUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import me.shaohui.bottomdialog.BottomDialog;

public class CharteredTravelDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartered_travel_detail);

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
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.route_detail));
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        ExpandTextView textView = (ExpandTextView) findViewById(R.id.trip_info_textView);
        int width = ScreenUtils.getScreenWidth(this) - ScreenUtils.dip2px(this, 16 * 2);
        textView.initWidth(width);
        textView.setMaxLines(3);
        String info = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        textView.setCloseText(info);

        ImageView mapImageView = (ImageView) findViewById(R.id.map_imageView);
        String latDestination = "48.858235";
        String lngDestination = "2.294571";
        String url = "http://maps.google.com/maps/api/staticmap?center=" + latDestination + "," + lngDestination + "&zoom=15&size=500x500&sensor=false";

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(this).load(url).apply(options).into(mapImageView);

        LinearLayout journeyLinearLayout = (LinearLayout) findViewById(R.id.journey_reference_linearLayout);
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View footer = vi.inflate(R.layout.layout_journey_reference_footer, null);

        for (int i=0; i<5; i++) {
            View header = vi.inflate(R.layout.layout_journey_reference_header, null);
            View contentLast = vi.inflate(R.layout.layout_journey_reference_content_last, null);
            journeyLinearLayout.addView(header);
            for (int j=0; j<5; j++) {
                View content = vi.inflate(R.layout.layout_journey_reference_content, null);
                journeyLinearLayout.addView(content);
            }
            journeyLinearLayout.addView(contentLast);
        }
        journeyLinearLayout.addView(footer);

        LinearLayout commentLinearLayout = (LinearLayout) findViewById(R.id.journey_user_comment_linearLayout);

        for (int i=0; i<5; i++) {
            View commentView = vi.inflate(R.layout.layout_journey_user_comment, null);
            commentLinearLayout.addView(commentView);
        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recommendationRecyclerView = (RecyclerView) findViewById(R.id.recommendation_recyclerView);
        recommendationRecyclerView.setLayoutManager(layoutManager);
        recommendationRecyclerView.setAdapter(new JourneyRecommendationAdapter(this));

        Button charteredTravelButton = (Button) findViewById(R.id.chartered_travel_button);
        SpannableString charteredTravelButtonText = new SpannableString(getString(R.string.chartered_journey) + "\n" + "¥1000");
        charteredTravelButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, charteredTravelButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        charteredTravelButton.setText(charteredTravelButtonText);

        Button groupTravelButton = (Button) findViewById(R.id.group_travel_button);
        SpannableString groupTravelButtonText = new SpannableString(getString(R.string.group_journey) + "\n" + "¥100");
        groupTravelButtonText.setSpan(new RelativeSizeSpan(0.8f), 5, groupTravelButtonText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        groupTravelButton.setText(groupTravelButtonText);

        charteredTravelButton.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    Button confirmButton = (Button) v.findViewById(R.id.start_booking_button);
                    ImageButton closeButton = (ImageButton) v.findViewById(R.id.cancel_button);
                    TextView dateTextView = (TextView) v.findViewById(R.id.date_textView);

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                            Intent intent = CharteredTravelOrderActivity.makeIntent(CharteredTravelDetailActivity.this);
                            intent.putExtra("parentActivity", "chartered");
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        }
                    });

                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });

                    CalendarView calendarView = (CalendarView) v.findViewById(R.id.calendarView);
                    calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
                        @Override
                        public void onCalendarOutOfRange(Calendar calendar) {

                        }

                        @Override
                        public void onCalendarSelect(Calendar calendar, boolean isClick) {
                            dateTextView.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_date_picker)
                    .show();
        });

        groupTravelButton.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    Button confirmButton = (Button) v.findViewById(R.id.start_booking_button);
                    ImageButton closeButton = (ImageButton) v.findViewById(R.id.cancel_button);
                    TextView dateTextView = (TextView) v.findViewById(R.id.date_textView);

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                            Intent intent = CharteredTravelOrderActivity.makeIntent(CharteredTravelDetailActivity.this);
                            intent.putExtra("parentActivity", "group");
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        }
                    });

                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });

                    CalendarView calendarView = (CalendarView) v.findViewById(R.id.calendarView);
                    calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
                        @Override
                        public void onCalendarOutOfRange(Calendar calendar) {

                        }

                        @Override
                        public void onCalendarSelect(Calendar calendar, boolean isClick) {
                            dateTextView.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_date_picker)
                    .show();
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CharteredTravelDetailActivity.class);
    }
}
