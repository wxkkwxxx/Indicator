package demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.wxk.myviewpagerindicate.R;

/**
 * Created by Administrator on 2017/4/12
 */

public class DemoActivity extends AppCompatActivity{

    private String[] items = {"直播", "推荐", "视频"};
    private LinearLayout mIndicatorContainer;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mIndicatorContainer = (LinearLayout) findViewById(R.id.indicator_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return null;
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });
    }
}
