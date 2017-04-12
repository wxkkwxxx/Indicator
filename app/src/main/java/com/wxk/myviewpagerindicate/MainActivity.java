package com.wxk.myviewpagerindicate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wxk.myviewpagerindicate.indicator.IndicatorAdapter;
import com.wxk.myviewpagerindicate.indicator.TrackIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] items = {"直播", "推荐", "视频", "段友秀", "图片", "段子", "精华", "同城", "游戏"};
    private TrackIndicatorView mIndicatorContainer;
    private List<ColorTrackTextView> mIndicators;
    private ViewPager mViewPager;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIndicatorContainer = (TrackIndicatorView) findViewById(R.id.indicator_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mIndicators = new ArrayList<>();

//        for (int i = 0; i < items.length; i++) {
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.weight = 1;
//            ColorTrackTextView trackTextView = new ColorTrackTextView(this);
//            trackTextView.setTextSize(20);
//            trackTextView.setText(items[i]);
//            trackTextView.setLayoutParams(layoutParams);
//            trackTextView.setChangeColor(ContextCompat.getColor(this, R.color.colorPrimary));
//            mIndicatorContainer.addView(trackTextView);
//            mIndicators.add(trackTextView);
//        }
        mIndicatorContainer.setAdapter(new IndicatorAdapter<ColorTrackTextView>() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public ColorTrackTextView getView(int position, ViewGroup parent) {
                ColorTrackTextView trackTextView = new ColorTrackTextView(MainActivity.this);
                trackTextView.setTextSize(20);
                trackTextView.setText(items[position]);
                trackTextView.setChangeColor(Color.RED);
                int padding = 20;
                trackTextView.setPadding(0, padding, 0, padding / 2);
                mIndicators.add(trackTextView);
                return trackTextView;
            }

            @Override
            public void highLightIndicator(ColorTrackTextView view) {
                view.setTextColor(Color.RED);
                view.setCurrentProgress(1);
            }

            @Override
            public void restoreIndicator(ColorTrackTextView view) {
                view.setTextColor(Color.BLACK);
                view.setCurrentProgress(0);
            }

            @Override
            public View getBottomTrackView() {
                View view = new View(MainActivity.this);
                view.setBackgroundColor(Color.RED);
                view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 6));
                return view;
            }
        }, mViewPager, false);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(TAG, "position-->" + position + ",positionOffset-->" + positionOffset + ",positionOffsetPixels-->" + positionOffsetPixels);
                ColorTrackTextView left = mIndicators.get(position);
                left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                left.setCurrentProgress(1 - positionOffset);

                try {
                    ColorTrackTextView right = mIndicators.get(position + 1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrentProgress(positionOffset);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
