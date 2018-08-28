package com.qingwing.safekey.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qingwing.safekey.R;
import com.qingwing.safekey.utils.LogUtil;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class MessageFragment extends BaseFragment {
    private static final String ARG = "arg";


    public MessageFragment() {
        LogUtil.d("MessageFragment non-parameter constructor");
    }

    public static MessageFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void init() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}