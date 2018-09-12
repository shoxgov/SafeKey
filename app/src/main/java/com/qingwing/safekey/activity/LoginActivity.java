package com.qingwing.safekey.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.dialog.WaitTool;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.okhttp3.response.LoginResponse;
import com.qingwing.safekey.okhttp3.response.RSAKeyResponse;
import com.qingwing.safekey.utils.LogUtil;
import com.qingwing.safekey.utils.PreferenceUtil;
import com.qingwing.safekey.utils.RSAUtils;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-07-23.
 * email:shoxgov@126.com
 */

public class LoginActivity extends BaseActivity {
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    @Bind(R.id.input_layout_name)
    EditText mName;
    @Bind(R.id.input_layout_psw)
    EditText mPsw;
    @Bind(R.id.btn_login)
    TextView mBtnLogin;
    @Bind(R.id.cb)
    CheckBox cb;
    /**
     * 是否开始了动画效果
     */
    private boolean isStartAnimator = false;

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtil.showText("isStartAnimator is over");
            progress.setVisibility(View.GONE);
            mInputLayout.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        PreferenceUtil.init(this);
        if (!TextUtils.isEmpty(PreferenceUtil.getString("username", ""))) {
            mName.setText(PreferenceUtil.getString("username", ""));
            mPsw.setText(PreferenceUtil.getString("password", ""));
            cb.setChecked(true);
        }
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        String ips = PreferenceUtil.getString("hostIp", "");
        String port = PreferenceUtil.getString("hostPort", "");
        String username = mName.getText().toString();
        String pwd = mPsw.getText().toString();
        if (TextUtils.isEmpty(ips)) {
            ToastUtil.showText("请先设置服务器IP");
            Intent setting = new Intent();
            setting.setClass(this, HostNameSettingActivity.class);
            startActivityForResult(setting, 13);
            return;
        }
        NetWorkConfig.HTTPS = "https://" + ips + ":" + port;//"https://s.keenzy.cn";
        requestRSAkey();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 13 && resultCode == RESULT_OK) {
            requestRSAkey();
        }
    }

    private void requestRSAkey() {
        OkHttpUtils.postAsyn(NetWorkConfig.RSAKEY, null, RSAKeyResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                RSAKeyResponse rsk = (RSAKeyResponse) br;
                SKApplication.rsaKeyid = rsk.getPrivateKeyid();
                SKApplication.rsaModulus = rsk.getPublicKeyModulus();
                SKApplication.rsaExponent = rsk.getPublicKeyExponent();
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mHander = null;
    }

    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                LogUtil.d("mInputLayout onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtil.d("mInputLayout onAnimationRepeat");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.d("mInputLayout onAnimationEnd");
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtil.d("mInputLayout onAnimationCancel");
            }
        });
    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1200);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtil.d("progress onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isStartAnimator = false;
                LogUtil.d("progress onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtil.d("progress onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtil.d("progress onAnimationRepeat");
            }
        });

    }

    @OnClick({R.id.login_forgetpwd, R.id.login_setting, R.id.btn_login})
    public void onViewClicked(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.login_forgetpwd:
                break;
            case R.id.login_setting:
                Intent setting = new Intent();
                setting.setClass(this, HostNameSettingActivity.class);
                startActivity(setting);
                break;
            case R.id.btn_login:
                String ips = PreferenceUtil.getString("hostIp", "");
                String port = PreferenceUtil.getString("hostPort", "");
                String username = mName.getText().toString();
                String pwd = mPsw.getText().toString();
                if (TextUtils.isEmpty(ips)) {
                    ToastUtil.showText("请先设置服务器IP");
                    return;
                }
//                NetWorkConfig.HTTPS = "https://s.keenzy.cn";
                NetWorkConfig.HTTPS = "https://" + ips + ":" + port;
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.showText("请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.showText("请输入密码");
                    return;
                }
                isStartAnimator = true;
//                mWidth = mBtnLogin.getMeasuredWidth();
//                mHeight = mBtnLogin.getMeasuredHeight();
//                mName.setVisibility(View.INVISIBLE);
//                mPsw.setVisibility(View.INVISIBLE);
//                inputAnimator(mInputLayout, mWidth, mHeight);
                login(username, pwd);
//                login("admin", "qingyixinxi");
                break;
        }
    }

    class JellyInterpolator extends LinearInterpolator {
        private float factor;

        public JellyInterpolator() {
            this.factor = 0.15f;
        }

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input)
                    * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }

    private void login(final String username, final String pwd) {
        if (TextUtils.isEmpty(SKApplication.rsaKeyid)) {
            requestRSAkey();
            ToastUtil.showText("Keyid为空或者服务器连接异常,请重新设置服务器");
            return;
        }
        WaitTool.showDialog(this, "");
        Map<String, String> params = new HashMap<String, String>();
        RSAPublicKey publicKey = RSAUtils.getPublicKey(SKApplication.rsaModulus, SKApplication.rsaExponent);
        params.put("userlogin", RSAUtils.encryptByPublicKey(username, publicKey));
        params.put("password", RSAUtils.encryptByPublicKey(pwd, publicKey));
        params.put("privateKeyid", SKApplication.rsaKeyid);
        OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, LoginResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                LoginResponse lr = (LoginResponse) br;
                if (lr.getErrCode() == 0) {
                    if (cb.isChecked()) {
                        PreferenceUtil.commitString("username", username);
                        PreferenceUtil.commitString("password", pwd);
                    } else {
                        PreferenceUtil.commitString("username", "");
                        PreferenceUtil.commitString("password", "");
                    }
                    SKApplication.loginToken = lr.getToken();
                    SKApplication.refreshentoken = lr.getRefreshentoken();
                    Intent main = new Intent();
                    main.setClass(LoginActivity.this, ScanListActivity.class);
//                main.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(main);
                    WaitTool.dismissDialog();
                    finish();
                } else {
                    WaitTool.dismissDialog();
                    ToastUtil.showText(lr.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText("服务器连接异常 " + message);
            }
        });
    }
}
