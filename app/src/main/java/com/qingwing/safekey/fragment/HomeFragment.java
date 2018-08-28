package com.qingwing.safekey.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qingwing.safekey.R;
import com.qingwing.safekey.utils.LogUtil;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class HomeFragment extends BaseFragment {
    private static final String ARG = "arg";

    public HomeFragment() {
        LogUtil.d("HomeFragment non-parameter constructor");
    }

    public static HomeFragment newInstance(String arg) {
        LogUtil.d("HomeFragment newInstance");
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtil.d("HomeFragment onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

}