package com.qingwing.safekey.fragment;

import android.support.v4.app.Fragment;


import com.qingwing.safekey.utils.LogUtil;

import java.util.HashMap;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class FragmentFactory {
    public final static int F1 = 1;
    public final static int F2 = 2;
    public final static int F3 = 3;
    public final static int F4 = 4;
    private static HashMap<Integer, Fragment> mFragments = new HashMap<>();

    public static Fragment createFragment(int fragmentName) {
        LogUtil.d( "FragmentFactory createFragment  fragmentName="+fragmentName);
        // 从缓存中取出
        Fragment fragment = mFragments.get(fragmentName);
        if (fragment == null) {
            switch (fragmentName) {
                case F1:
                    fragment = HomeFragment.newInstance("100");
                    break;

                case F2:
                    fragment = EmployFragment.newInstance("200");
                    break;

                case F3:
                    fragment = MessageFragment.newInstance("300");
                    break;

                case F4:
                    fragment = MyFragment.newInstance("400");
                    break;
            }
            // 把frament加入到缓存中
            mFragments.put(fragmentName,fragment);
        }
        return fragment;
    }

    public static void clear(){
        LogUtil.d( "FragmentFactory clear");
        mFragments.clear();
    }
}