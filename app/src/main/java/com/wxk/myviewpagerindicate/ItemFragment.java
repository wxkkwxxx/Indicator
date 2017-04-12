package com.wxk.myviewpagerindicate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/11
 */

public class ItemFragment extends Fragment {

    public static ItemFragment newInstance(String title) {

        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        Bundle bundle = getArguments();
        tv.setText(bundle.getString("title"));
        return view;
    }
}
