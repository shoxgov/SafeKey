package com.qingwing.safekey.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.bluetooth.BLECommandManager;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.utils.LogUtil;
import com.qingwing.safekey.utils.PreferenceUtil;
import com.qingwing.safekey.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;


public class WelcomeActivity extends BaseActivity {
    private static final int REQUEST_ENABLE_BT = 21;
    /**
     * 初次访问者角色
     */
    private Handler mHandler = new Handler();
    private String loginTel, passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("WelcomeActivity onCreate");
        setContentView(R.layout.activity_welcome);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SKApplication.screenWidthPixels = dm.widthPixels;
        SKApplication.screenHeightPixels = dm.heightPixels;        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！a3oAgG56Ip");
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        PreferenceUtil.init(this);
        loginTel = PreferenceUtil.getString("LoginTel", "");
        passWord = PreferenceUtil.getString("passWord", "");
        LogUtil.d("WelcomeActivity  loginTel=" + loginTel + ",passWord=" + passWord);
        if (BLECommandManager.isSupportBLE(this)) {
            // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                if (TextUtils.isEmpty(loginTel)) {
                    goLoginActivity();
                } else {
                    login();
                }
            }
        } else {
            Toast.makeText(this, "该设备蓝牙不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    private void goMainActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent();
                i.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            if (TextUtils.isEmpty(loginTel)) {
                goLoginActivity();
            } else {
                login();
            }
        } else {
            finish();
        }
    }

    private void goLoginActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent();
                i.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }

    private void login() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", loginTel);
        if (!TextUtils.isEmpty(passWord)) {
            params.put("passWord", passWord);
        } else {
            goLoginActivity();
        }
        OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                goMainActivity();
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
                goLoginActivity();
            }
        });
    }
}
