package com.qingwing.safekey.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.imp.TitleBarListener;
import com.qingwing.safekey.utils.PreferenceUtil;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;
import com.qingwing.safekey.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-07-26.
 * email:shoxgov@126.com
 */

public class HostNameSettingActivity extends BaseActivity {
    @Bind(R.id.host_ip_edit)
    EditText hostIpEdit;
    @Bind(R.id.host_port_edit)
    EditText hostPortEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostip_setting);
        ButterKnife.bind(this);
        PreferenceUtil.init(this);
        String ips = PreferenceUtil.getString("hostIp", "");
        String ports = PreferenceUtil.getString("hostPort", "");
        if (!TextUtils.isEmpty(ips)) {
            hostIpEdit.setText(ips);
        }
        if (!TextUtils.isEmpty(ports)) {
            hostPortEdit.setText(ports);
        }
        init();
    }

    private void init() {
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        String ips = hostIpEdit.getText().toString();
        String ports = hostPortEdit.getText().toString();

        if (TextUtils.isEmpty(ips)) {
            ToastUtil.showText("IP不能为空");
            return;
        }
        if (TextUtils.isEmpty(ports)) {
            ToastUtil.showText("端口号不能为空");
            return;
        }
        if (!Utils.checkIp(ips)) {
            ToastUtil.showText("请输入合法的IP");
            return;
        }
        PreferenceUtil.commitString("hostIp", ips);
        PreferenceUtil.commitString("hostPort", ports);
        ToastUtil.showText("设置成功");
        NetWorkConfig.HTTPS = "https://" + ips + ":" + ports;
        finish();
    }
}
