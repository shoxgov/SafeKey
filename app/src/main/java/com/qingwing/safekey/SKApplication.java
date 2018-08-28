package com.qingwing.safekey;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengyin on 2018-07-23.
 * email:shoxgov@126.com
 */

public class SKApplication extends Application {
    private static Context mContext;
    private static List<Activity> activityList = new ArrayList<>();
    public static int screenWidthPixels = 400;
    public static int screenHeightPixels = 800;
    /**
     * 全局私钥id
     */
    public static String rsaKeyid, rsaModulus, rsaExponent;
    /**
     * 登录token
     */
    public static String loginToken;
    public static String refreshentoken;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        mContext = this;
        super.onCreate();
    }

    /**
     * @Description 添加Activity到activityList，在onCreate()中调用
     */
    public static void addActivity(Activity activity) {
        if (activityList != null && activityList.size() > 0) {
            if (!activityList.contains(activity)) {
                activityList.add(activity);
            }
        } else {
            activityList.add(activity);
        }
    }

    /**
     * @Description 结束Activity到activityList，在onDestroy()中调用
     */
    public static void finishActivity(Activity activity) {
        if (activity != null && activityList != null && activityList.size() > 0) {
            activityList.remove(activity);
        }
    }

    /**
     * @Description 结束所有Activity
     */
    public static void finishAllActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (null != activity) {
                    activity.finish();
                }
            }
        }
        activityList.clear();
    }
}
