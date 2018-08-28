package com.qingwing.safekey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.adapter.BtHandleAdapter;
import com.qingwing.safekey.bean.BtHandleBean;
import com.qingwing.safekey.bluetooth.BLECommandManager;
import com.qingwing.safekey.bluetooth.BleObserverConstance;
import com.qingwing.safekey.bluetooth.BluetoothService;
import com.qingwing.safekey.observable.ObservableBean;
import com.qingwing.safekey.observable.ObserverManager;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.okhttp3.response.OfflineRecorderResponse;
import com.qingwing.safekey.okhttp3.response.OfflineUnlockResponse;
import com.qingwing.safekey.utils.ListViewUtils;
import com.qingwing.safekey.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-08-10.
 * email:shoxgov@126.com
 */

public class BtHandleActivity extends BaseActivity implements Observer {
    @Bind(R.id.bt_handle_text_info)
    TextView testInfo;
    @Bind(R.id.bt_handle_test)
    EditText commandEdit;
    @Bind(R.id.bt_handle_edit)
    EditText infoEdit;
    @Bind(R.id.bt_handle_list)
    ListView listView;
    /**
     * 筛选条件保存
     */
    private String selectHouseId, selectFloorId, selectDeviceType, selectDeviceId, selectGatewayCode;
    /*
    离线指令保存
     */
    private OfflineUnlockResponse offlineUnlockResponse;
    private OfflineRecorderResponse offlineRecorderResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_handle);
        ButterKnife.bind(this);
        ObserverManager.getObserver().addObserver(this);
        selectHouseId = getIntent().getStringExtra("selectHouseId");
        selectFloorId = getIntent().getStringExtra("selectFloorId");
        selectDeviceType = getIntent().getStringExtra("selectDeviceType");
        selectDeviceId = getIntent().getStringExtra("selectDeviceId");
        selectGatewayCode = getIntent().getStringExtra("selectGatewayCode");
        init();
    }

    private void init() {
        testInfo.setText("收到 selectHouseId=" + selectHouseId + ",selectFloorId=" + selectFloorId + ",selectDeviceType=" + selectDeviceType);
        BtHandleAdapter adapter = new BtHandleAdapter(this);
        listView.setAdapter(adapter);
        List<BtHandleBean> data = new ArrayList<>();
        BtHandleBean bb1 = new BtHandleBean();
        bb1.setIcon(R.mipmap.ic_launcher);
        bb1.setTitle("状态查询");
        data.add(bb1);
        BtHandleBean bb2 = new BtHandleBean();
        bb2.setIcon(R.mipmap.ic_launcher);
        bb2.setTitle("离线授权");
        data.add(bb2);
        BtHandleBean bb22 = new BtHandleBean();
        bb22.setIcon(R.mipmap.ic_launcher);
        bb22.setTitle("离线删除授权");
        data.add(bb22);
        BtHandleBean bb3 = new BtHandleBean();
        bb3.setIcon(R.mipmap.ic_launcher);
        bb3.setTitle("离线开锁");
        data.add(bb3);
        BtHandleBean bb4 = new BtHandleBean();
        bb4.setIcon(R.mipmap.ic_launcher);
        bb4.setTitle("离线上传记录");
        data.add(bb4);
        adapter.setData(data);
        listView.setOnItemClickListener(onItemClickListener);
        ListViewUtils.setListViewHeightBasedOnChildren(listView);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        ObserverManager.getObserver().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
//                    Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
//                    String strb = getSendBlueId(BlueId, "0025", startTime + level + ServiceStopTime + userpassword);
//                    intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, strb);
//                    context.sendBroadcast(intent);
                    BLECommandManager.queryStatus(BtHandleActivity.this, selectGatewayCode, selectDeviceId);
                    break;
                case 1:
                    Intent authorization = new Intent();
                    authorization.setClass(BtHandleActivity.this, OfflineAuthorizationActivity.class);
                    authorization.putExtra("roomid", selectDeviceId);
                    startActivity(authorization);
                    break;
                case 2:
                    Intent delete = new Intent();
                    delete.setClass(BtHandleActivity.this, OfflineDeleteAuthorizationActivity.class);
                    delete.putExtra("roomid", selectDeviceId);
                    startActivity(delete);
                    break;
                case 3:
                    obatinOfflineUnlockCommand();
                    break;
                case 4:
                    obtainOfflineRecorderCommand();
                    break;
            }
        }
    };

    private void obatinOfflineUnlockCommand() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("roomid", selectDeviceId);
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_AUTHORY_UNLOCK, params, OfflineUnlockResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                OfflineUnlockResponse our = (OfflineUnlockResponse) br;
                if (our.getErrCode() == 0) {
                    offlineUnlockResponse = our;
                    Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
                    intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, our.getOrder());
                    sendBroadcast(intent);
                } else {
                    ToastUtil.showText(br.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }

    /**
     * itid	是	远程开门原始id
     * orderresult	是	指令执行结果
     */
    private void submitOfflineUnlock() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("itid", offlineUnlockResponse.getItid());
        params.put("orderresult", "1");
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_AUTHORY_UNLOCK_SAVE_RESULT, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                if (br.getErrCode() == 0) {
                } else {
                    ToastUtil.showText(br.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }


    private void obtainOfflineRecorderCommand() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("roomid", selectDeviceId);
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_AUTHORY_HISROTY_RECORDER, params, OfflineRecorderResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                OfflineRecorderResponse orr = (OfflineRecorderResponse) br;
                if (orr.getErrCode() == 0) {
                    offlineRecorderResponse = orr;
                    Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
                    intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, orr.getOrder());
                    sendBroadcast(intent);
                } else {
                    ToastUtil.showText(orr.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }

    /**
     * itid	是	查询门锁记录原始id
     * record	是	记录信息
     */
    private void submitOfflineRecorderCommand() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("itid", offlineRecorderResponse.getItid());
        params.put("record", "1");
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_AUTHORY_RECORDER_SAVE_RESULT, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                if (br.getErrCode() == 0) {
                } else {
                    ToastUtil.showText(br.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }

    @OnClick({R.id.bt_handle_disconnect, R.id.bt_handle_test_bt})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_handle_test_bt:
                String command = commandEdit.getText().toString();
                if (TextUtils.isEmpty(command)) {
                    return;
                }
                Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
                intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, command);
                sendBroadcast(intent);
                break;
            case R.id.bt_handle_disconnect:
                Intent disconnect = new Intent();
                disconnect.setAction(BluetoothService.ACTION_BT_COMMAND);
                disconnect.putExtra("command", "DISCONNECT_BT");
                sendBroadcast(disconnect);
                break;
        }
    }

    @Override
    public void update(Observable arg0, Object obj) {
        ObservableBean ob = (ObservableBean) obj;
        switch (ob.getWhat()) {
            case BleObserverConstance.BOX_RECEIVER_READINFO:
                String data = ob.getObject().toString();
                if (TextUtils.isEmpty(data)) {
                    infoEdit.setText("null");
                } else {
                    infoEdit.setText(data);
                }
                break;
            case BleObserverConstance.BOX_CONNTEC_BLE_STATUS:
                if ((boolean) ob.getObject()) {
                } else {
                    ToastUtil.showText("蓝牙已断开");
                    finish();
                }
                break;
        }

    }
}