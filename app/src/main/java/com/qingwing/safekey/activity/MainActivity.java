package com.qingwing.safekey.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.fragment.EmployFragment;
import com.qingwing.safekey.fragment.FragmentFactory;
import com.qingwing.safekey.fragment.HomeFragment;
import com.qingwing.safekey.fragment.MessageFragment;
import com.qingwing.safekey.fragment.MyFragment;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.utils.LogUtil;
import com.qingwing.safekey.utils.PreferenceUtil;
import com.qingwing.safekey.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-07-26.
 * email:shoxgov@126.com
 */

public class MainActivity extends BaseFragmentActivity {
    private static final int REQUEST_ENABLE_BT = 21;
    @Bind(R.id.rb_home)
    TextView tvBottomNav1;
    @Bind(R.id.rb_employ)
    TextView tvBottomNav2;
    @Bind(R.id.rb_msg)
    TextView tvBottomNav3;
    @Bind(R.id.rb_my)
    TextView tvBottomNav4;
    private FragmentManager mFragmentManager;
    private Fragment homeFragment;
    private Fragment employFragment;
    private Fragment msgFragment;
    private Fragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("MainFragmentActivity onCreate");
        setContentView(R.layout.activity_main);
        PreferenceUtil.init(this);
        ButterKnife.bind(this);
        Utils.verifyStoragePermissions(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SKApplication.screenWidthPixels = dm.widthPixels;
        SKApplication.screenHeightPixels = dm.heightPixels;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            homeFragment = FragmentFactory.createFragment(FragmentFactory.F1);
            selectFragment(homeFragment);
            LogUtil.d("MainFragmentActivity savedInstanceState is  null");
            startServer();
        } else {
            position = savedInstanceState.getInt("position");
            Map<String, String> params = new HashMap<String, String>();
            params.put("phone", PreferenceUtil.getString("LoginTel", ""));
            params.put("passWord", PreferenceUtil.getString("passWord", ""));
            OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, BaseResponse.class, new HttpCallback() {
                @Override
                public void onSuccess(BaseResponse br) {
                    super.onSuccess(br);
                    preventOverlap();
                }

                @Override
                public void onFailure(int code, String message) {
                    super.onFailure(code, message);
                    preventOverlap();
                }
            });
        }
    }

    private void startServer() {
//        Intent server = new Intent(getBaseContext(), MQService.class);
//        server.putExtra("command", MQService.START_YUNIM);
//        bindService(server,conn);
//        startService(server);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 防止Fragment重叠的问题，切换横竖屏或者内存不够，重走了Activity的生命周期
     * 第一次要createFragment
     * 重启以后，要通过tag来找到对应的Fragment
     * <p>
     * 重启的标准是savedInstanceState是否为null
     * 因为重启之前savedInstanceState=null
     * 重启之后savedInstanceState！=null
     */
    private void preventOverlap() {
//        如果savedInstanceState为空，证明没有发生重走Activity的生命周期的情况，这时候要创建createFragment
        LogUtil.d("MainFragmentActivity savedInstanceState is not null");
//            使用mFragmentManager通过Tag来取得，只要他add过，就给他添加了Tag
//            否则直接重写创建一个Fragment的话,会导致重叠
        homeFragment = mFragmentManager.findFragmentByTag(HomeFragment.class.getName());
        employFragment = mFragmentManager.findFragmentByTag(EmployFragment.class.getName());
        msgFragment = mFragmentManager.findFragmentByTag(MessageFragment.class.getName());
        myFragment = mFragmentManager.findFragmentByTag(MyFragment.class.getName());
//=======================以下代码，不要系统也会自动识别，上次死亡位置，但是除了死亡位置，savedInstanceState还可以传递其他数据============
//            获得上次死亡重启的位置
        LogUtil.d(position + "=======");
        switch (position) {
            case 1:
                tvBottomNav1.performClick();
//                    selectFragment(homeFragment);
                break;
            case 2:
                tvBottomNav2.performClick();
//                    selectFragment(employFragment);
                break;
            case 3:
                tvBottomNav3.performClick();
//                    selectFragment(msgFragment);
                break;
            case 4:
                tvBottomNav4.performClick();
//                    selectFragment(myFragment);
                break;
        }
        startServer();
//===================以上代码需要配合onSaveInstanceState（）方法里面记录数据================
    }

    @OnClick({R.id.rb_home, R.id.rb_employ, R.id.rb_msg, R.id.rb_my})
    public void onViewClicked(View view) {
        LogUtil.d("onViewClicked");
        switch (view.getId()) {
            case R.id.rb_home:
                if (homeFragment == null) {
                    homeFragment = FragmentFactory.createFragment(FragmentFactory.F1);
                }
                selectFragment(homeFragment);
                position = 1;
                break;
            case R.id.rb_employ:
                if (employFragment == null) {
                    employFragment = FragmentFactory.createFragment(FragmentFactory.F2);
                }
                selectFragment(employFragment);
                position = 2;
                break;
            case R.id.rb_msg:
                if (msgFragment == null) {
                    msgFragment = FragmentFactory.createFragment(FragmentFactory.F3);
                }
                selectFragment(msgFragment);
                position = 3;
                break;
            case R.id.rb_my:
                if (myFragment == null) {
                    myFragment = FragmentFactory.createFragment(FragmentFactory.F4);
                }
                selectFragment(myFragment);
                position = 4;
                break;
        }
    }

    public void changeFragment(int page) {
        switch (page) {
            case 0:
                tvBottomNav1.performClick();
                break;
            case 1:
                tvBottomNav2.performClick();
                break;
            case 2:
                tvBottomNav3.performClick();
                break;
            case 3:
                tvBottomNav4.performClick();
                break;
        }
    }

    /**
     * 选择显示某一个Fragment
     *
     * @param fragment
     */
    private void selectFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAll(transaction);
        if (!fragment.isAdded()) {
            transaction.add(R.id.fl_content, fragment, fragment.getClass().getName());//tag
//            transaction.addToBackStack(null);//加入回退栈
        }
        transaction.show(fragment).commit();
    }

    private void hideAll(FragmentTransaction transaction) {
        if (homeFragment != null) {
//            必须使用同一个transaction，跟add/show时候一样
//            同一个事物进行了所有的add/hide/show，之后统一commit就行了
//            中间不能commit事物，因为一个事物只能是提交一次，重复提交会报错
//            即：每一次点击，生成一个事务，操作完后，提交这个事务
            transaction.hide(homeFragment);
        }
        if (employFragment != null) {
            transaction.hide(employFragment);
        }
        if (msgFragment != null) {
            transaction.hide(msgFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    //记录Fragment的位置
    private int position;

    /**
     * 这个方法会在activity重启前调用，用来保存一些数据
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //记录当前的position
        outState.putInt("position", position);
        LogUtil.d(position + ">>>>onSaveInstanceState");
//        ★★★★★★★★别忘了下下面的super.onSave，否则会报错
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            WarnDialog warnDialog = new WarnDialog(MainFragmentActivity.this, "确定退出应用？", new DialogCallBack() {
//                @Override
//                public void OkDown(Object obj) {
            SKApplication.finishAllActivity();
            finish();
//                    try {
//                        int id = android.os.Process.myPid();
//                        android.os.Process.killProcess(id);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void CancleDown() {
//                }
//            });
//            warnDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                LogUtil.d("Acitivity全部关闭！！");
            } else if (resultCode == Activity.RESULT_OK) {
            }
        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                LogUtil.d("广播传过来的action：" + action);
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    LogUtil.d("监听到的蓝牙状态变化 state=" + state);
                    switch (state) {
                        case BluetoothAdapter.STATE_TURNING_OFF://手机蓝牙正在关闭
                            break;
                        case BluetoothAdapter.STATE_OFF://手机蓝牙关闭
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            break;
                        case BluetoothAdapter.STATE_ON:
                            LogUtil.d("打开蓝牙!!!");
                            break;
                    }
                }// 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
                // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
                // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
                else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                    if (Utils.isNetworkAvailable(context)) { // connected to the internet
                        LogUtil.d("CONNECTIVITY_ACTION 网络连接已经打开");
                    } else {   // not connected to the internet
                        LogUtil.d("CONNECTIVITY_ACTION 当前没有网络连接，请确保你已经打开网络 ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}