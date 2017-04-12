package com.wxk.myviewpagerindicate.indicator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/4/12
 */

public class IndicatorGroupView extends FrameLayout{

    private LinearLayout mIndicatorGroup;
    private View mBottomTrackView;
    private int mItemWidth;
    private FrameLayout.LayoutParams mTrackParams;
    private int mInitLeftMargin;

    public IndicatorGroupView(Context context) {
        this(context, null);
    }

    public IndicatorGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mIndicatorGroup = new LinearLayout(context);
        mIndicatorGroup.setOrientation(LinearLayout.HORIZONTAL);
        addView(mIndicatorGroup);
    }

    /**
     * 添加itemView
     */
    public void addItemView(View view) {
        mIndicatorGroup.addView(view);
    }

    /**
     * 获取当前位置的itemView
     */
    public View getItemViewAt(int position){
        return mIndicatorGroup.getChildAt(position);
    }

    /**
     * 添加底部指示器
     */
    public void addBottomTrackView(View bottomTrackView, int itemWidth) {

        if(bottomTrackView == null){
            return;
        }

        this.mItemWidth = itemWidth;
        this.mBottomTrackView = bottomTrackView;
        addView(mBottomTrackView);

        mTrackParams = (LayoutParams) mBottomTrackView.getLayoutParams();
        mTrackParams.gravity = Gravity.BOTTOM;

        int trackWidth = mTrackParams.width;
        //没有设置宽度
        if(mTrackParams.width == LayoutParams.MATCH_PARENT){
            trackWidth = mItemWidth;
        }
        //设置宽度过大
        if(trackWidth > mItemWidth){
            trackWidth = mItemWidth;
        }

        mTrackParams.width = trackWidth;

        mInitLeftMargin = (mItemWidth - trackWidth) / 2;
        mTrackParams.leftMargin = mInitLeftMargin;
    }

    public void scrollBottomTrack(int position, float positionOffset) {
        if(mBottomTrackView == null){
            return;
        }
        int leftMargin = (int) ((position + positionOffset) * mItemWidth);
        mTrackParams.leftMargin = leftMargin + mInitLeftMargin;
        mBottomTrackView.requestLayout();
    }

    /**
     * 点击移动底部
     */
    public void scrollBottomTrack(int position) {
        if(mBottomTrackView == null){
            return;
        }
        //要移动到的位置
        int finalLeftMargin = position * mItemWidth + mInitLeftMargin;
        //当前位置
        int currentLeftMargin = mTrackParams.leftMargin;
        //移动的距离
        int distance = finalLeftMargin - currentLeftMargin;

        //带动画
        ValueAnimator animator = ObjectAnimator.ofInt(currentLeftMargin, finalLeftMargin).setDuration((long) (Math.abs(distance) * 0.4f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTrackParams.leftMargin = value;
                mBottomTrackView.requestLayout();
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
