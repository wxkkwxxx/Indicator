package com.wxk.myviewpagerindicate.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.wxk.myviewpagerindicate.R;

/**
 * Created by Administrator on 2017/4/11
 */

public class TrackIndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener{

    private IndicatorGroupView mIndicatorGroup;
    private IndicatorAdapter mAdapter;
    //tab总数
    private int mItemCount;
    //可见tab总数
    private int mTabVisibleCounts = 0;
    //每个item宽度
    private int mItemWidth;
    //当前位置
    private int mCurrentPosition = 0;
    private ViewPager mViewPager;

    //解决点击抖动,点击不执行平滑滚动方法
    private boolean mIsExecuteScroll = false;

    private boolean mSmoothScroll;

    public TrackIndicatorView(Context context) {
        this(context, null);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrackIndicatorView);
        mTabVisibleCounts = typedArray.getInt(R.styleable.TrackIndicatorView_tabVisibleCounts, mTabVisibleCounts);
        typedArray.recycle();

        mIndicatorGroup = new IndicatorGroupView(context);
        addView(mIndicatorGroup);
    }

    public void setAdapter(IndicatorAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }

        this.mAdapter = adapter;
        mItemCount = mAdapter.getCount();
        for (int i = 0; i < mItemCount; i++) {

            View view = mAdapter.getView(i, mIndicatorGroup);
            mIndicatorGroup.addItemView(view);
            if (mViewPager != null) {
                switchItemClick(view, i);
            }
        }

        mAdapter.highLightIndicator(mIndicatorGroup.getItemViewAt(0));
    }

    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager) {

        setAdapter(adapter, viewPager, true);
    }

    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager, boolean smoothScroll) {
        this.mSmoothScroll = smoothScroll;
        if (viewPager == null) {
            throw new NullPointerException("viewPager is null");
        }
        this.mViewPager = viewPager;
        this.mViewPager.addOnPageChangeListener(this);
        setAdapter(adapter);
    }

    private void switchItemClick(View view, final int position) {

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position, mSmoothScroll);
                smoothScrollIndicator(position);
                mIndicatorGroup.scrollBottomTrack(position);
            }
        });
    }

    private void smoothScrollIndicator(int position) {

        //当前总共的位置
        float totalScroll = position * mItemWidth;
        //左边的偏移
        int offsetScroll = (getWidth() - mItemWidth) / 2;
        final int finalScroll = (int) (totalScroll - offsetScroll);
        smoothScrollTo(finalScroll, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {

            mItemWidth = getItemWidth();
            for (int i = 0; i < mItemCount; i++) {
                mIndicatorGroup.getItemViewAt(i).getLayoutParams().width = mItemWidth;
            }

            //添加底部指示器
            mIndicatorGroup.addBottomTrackView(mAdapter.getBottomTrackView(), mItemWidth);
        }
    }

    //获取item的宽度
    private int getItemWidth() {

        int parentWidth = getWidth();
        if (mTabVisibleCounts != 0) {
            return parentWidth / mTabVisibleCounts;
        }

        int itemWidth = 0;

        //获取最宽的
        int maxItemWidth = 0;
        for (int i = 0; i < mItemCount; i++) {
            View view = mIndicatorGroup.getItemViewAt(i);
            int currentItemWidth = view.getWidth();
            maxItemWidth = Math.max(currentItemWidth, maxItemWidth);
        }

        //宽度是最宽的
        itemWidth = maxItemWidth;

        int allItemWidth = mItemCount * itemWidth;

        //所有条目宽度想加不足屏幕宽度
        if (allItemWidth < parentWidth) {

            itemWidth = parentWidth / mItemCount;
        }

        return itemWidth;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(mIsExecuteScroll){

            scrollCurrentIndicator(position, positionOffset);
            mIndicatorGroup.scrollBottomTrack(position, positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

        //上一位置重置
        mAdapter.restoreIndicator(mIndicatorGroup.getItemViewAt(mCurrentPosition));
        mCurrentPosition = position;
        //当前位置高亮
        mAdapter.highLightIndicator(mIndicatorGroup.getItemViewAt(mCurrentPosition));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state){
            case ViewPager.SCROLL_STATE_IDLE:

                mIsExecuteScroll = false;
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:

                mIsExecuteScroll = true;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:

                break;
        }
    }

    private void scrollCurrentIndicator(int position, float positionOffset) {

        //当前总共的位置
        float totalScroll = (position + positionOffset) * mItemWidth;
        //左边的偏移
        int offsetScroll = (getWidth() - mItemWidth) / 2;
        final int finalScroll = (int) (totalScroll - offsetScroll);
        scrollTo(finalScroll, 0);
    }
}
