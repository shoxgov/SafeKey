package com.qingwing.safekey.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.qingwing.safekey.utils.LogUtil;

import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("BaseFragment onCreateView");
        View view = inflater.inflate(provieResourceID(), container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    protected abstract void init();


    protected abstract int provieResourceID();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
