package com.wxk.myviewpagerindicate.indicator;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/11
 * getCount()获取条目总数
 * getView(int position, ViewGroup parent)
 */

public abstract class IndicatorAdapter<T extends View> {

    //获取总共的条数
    public abstract int getCount();

    //根据当前位置获取view
    public abstract T getView(int position, ViewGroup parent);

    //当前位置高亮
    public void highLightIndicator(T view){
    }

    //重置当前位置
    public void restoreIndicator(T view){
    }

    public View getBottomTrackView(){
        return null;
    }
}
