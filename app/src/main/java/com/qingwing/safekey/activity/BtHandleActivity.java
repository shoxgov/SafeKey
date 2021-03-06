package com.qingwing.safekey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import com.qingwing.safekey.bean.LockStatus;
import com.qingwing.safekey.bean.LockStatusEmue;
import com.qingwing.safekey.bluetooth.BLECommandManager;
import com.qingwing.safekey.bluetooth.BleObserverConstance;
import com.qingwing.safekey.bluetooth.BluetoothService;
import com.qingwing.safekey.dialog.AffirmDialog;
import com.qingwing.safekey.dialog.WaitTool;
import com.qingwing.safekey.imp.DialogCallBack;
import com.qingwing.safekey.observable.ObservableBean;
import com.qingwing.safekey.observable.ObserverManager;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.okhttp3.response.OfflineRecorderResponse;
import com.qingwing.safekey.okhttp3.response.OfflineUnlockResponse;
import com.qingwing.safekey.okhttp3.response.UploadRecordResultResponse;
import com.qingwing.safekey.utils.ListViewUtils;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    //    @Bind(R.id.bt_handle_text_info)
//    TextView testInfo;
//    @Bind(R.id.bt_handle_test)
//    EditText commandEdit;
    @Bind(R.id.bt_handle_edit)
    TextView infoEdit;
    @Bind(R.id.bt_handle_list)
    ListView listView;
    /**
     * 筛选条件保存
     */
    private String selectHouseId, selectFloorId, selectDeviceType, luckId, selectDeviceId, selectGatewayCode;
    /*
    离线指令保存
     */
    private OfflineUnlockResponse offlineUnlockResponse;
    private OfflineRecorderResponse offlineRecorderResponse;

//    private StringBuffer recordInfo = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_handle);
        ButterKnife.bind(this);
//        recordInfo.setLength(200);
        ObserverManager.getObserver().addObserver(this);
        selectHouseId = getIntent().getStringExtra("selectHouseId");
        selectFloorId = getIntent().getStringExtra("selectFloorId");
        selectDeviceType = getIntent().getStringExtra("selectDeviceType");
        luckId = getIntent().getStringExtra("luckId");
        selectDeviceId = getIntent().getStringExtra("selectDeviceId");
        selectGatewayCode = getIntent().getStringExtra("selectGatewayCode");
        init();
    }

    private void init() {
        infoEdit.setMovementMethod(ScrollingMovementMethod.getInstance());
//        testInfo.setText("收到 selectHouseId=" + selectHouseId + ",selectFloorId=" + selectFloorId + ",selectDeviceType=" + selectDeviceType);
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

    private static String getThreeSystemTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    void refreshLogView(String msg) {
        infoEdit.append(getThreeSystemTime() + ">>>" + msg);
        int offset = infoEdit.getLineCount() * infoEdit.getLineHeight();
        if (offset > infoEdit.getHeight()) {
            infoEdit.scrollTo(0, offset - infoEdit.getHeight());
        }
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
            AffirmDialog warnDialog = new AffirmDialog(BtHandleActivity.this, "确定强制断开连接？", new DialogCallBack() {
                @Override
                public void OkDown(Object obj) {
                    Intent disconnect = new Intent();
                    disconnect.setAction(BluetoothService.ACTION_BT_COMMAND);
                    disconnect.putExtra("command", "DISCONNECT_BT");
                    sendBroadcast(disconnect);
                    finish();
                }

                @Override
                public void CancleDown() {

                }
            });
            warnDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (Utils.isFastDoubleClick()) {
                return;
            }
            switch (position) {
                case 0:
//                    Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
//                    String strb = getSendBlueId(BlueId, "0025", startTime + level + ServiceStopTime + userpassword);
//                    intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, strb);
//                    context.sendBroadcast(intent);
                    WaitTool.showDialog(BtHandleActivity.this);
                    BLECommandManager.queryStatus(BtHandleActivity.this, selectGatewayCode, luckId);
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
                    WaitTool.showDialog(BtHandleActivity.this);
                    obatinOfflineUnlockCommand();
//                    BLECommandManager.offlineForceUnlock(BtHandleActivity.this, selectGatewayCode, luckId);
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
    private void submitOfflineUnlock(String order) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("itid", offlineUnlockResponse.getItid());
        params.put("orderresult", order);
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_AUTHORY_UNLOCK_SAVE_RESULT, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                if (br.getErrCode() == 0) {
                    ToastUtil.showText("上传服务器成功");
                    refreshLogView("离线开锁上传服务器成功\n");
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
        WaitTool.showDialog(this);
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
                    WaitTool.dismissDialog();
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

    @OnClick({R.id.bt_handle_disconnect/*, R.id.bt_handle_test_bt*/})
    public void onViewClicked(View v) {
        switch (v.getId()) {
//            case R.id.bt_handle_test_bt:
//                String command = commandEdit.getText().toString();
//                if (TextUtils.isEmpty(command)) {
//                    return;
//                }
//                Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
//                intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, command);
//                sendBroadcast(intent);
//                break;
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
//            case BleObserverConstance.BOX_RECEIVER_READINFO:
//                String data = ob.getObject().toString();
//                if (TextUtils.isEmpty(data)) {
//                    infoEdit.setText("null");
//                } else {
//                    infoEdit.setText(data);
//                }
//                break;
            case BleObserverConstance.RECEIVER_BT_FORCE_UNLUCK:
                WaitTool.dismissDialog();
                // 强制离线开锁返回，01代表正确，02代表当前门锁处于锁定状态，失败
                String order = ob.getObject().toString();
                String interceptData = order.substring(20, 22);
                if (interceptData.equals("01")) {
                    ToastUtil.showText("开锁成功");
//                    recordInfo.append("开锁成功\n");
                    refreshLogView("开锁成功\n");
//                    infoEdit.setText(recordInfo.toString());
                    submitOfflineUnlock(order);
                } else if (interceptData.equals("02")) {
                    ToastUtil.showText("门锁处于锁定状态，无法开锁");
//                    recordInfo.append("门锁处于锁定状态，无法开锁\n");
                    refreshLogView("门锁处于锁定状态，无法开锁\n");
//                    infoEdit.setText(recordInfo.toString());
                }
                break;
            case BleObserverConstance.BOX_CONNTEC_BLE_STATUS:
                if ((boolean) ob.getObject()) {
                } else {
                    ToastUtil.showText("蓝牙断开连接成功");
                    finish();
                }
                break;

            case BleObserverConstance.RECEIVER_BT_SATUS:
                WaitTool.dismissDialog();
                LockStatus ls = (LockStatus) ob.getObject();
                LockStatusEmue lse = ls.getLockStatus();
                StringBuffer lockStatus = new StringBuffer();
                if (lse.isHasDoorMagnetic()) {
                    lockStatus.append("支持门磁|");
                } else {
                    lockStatus.append("不支持门磁|");
                }
                if (lse.isHasBackLock()) {
                    lockStatus.append("支持反锁|");
                } else {
                    lockStatus.append("不支持反锁|");
                }
                if (lse.isBackLock()) {
                    lockStatus.append("已反锁|");
                } else {
                    lockStatus.append("未反锁|");
                }
                if (lse.isHasDoorMagnetic()) {
                    if (lse.isHasDoorMagnetic()) {
                        lockStatus.append("门开");
                    } else {
                        lockStatus.append("门关");
                    }
                } else {
                    lockStatus.deleteCharAt(lockStatus.length() - 1);
                }
                StringBuffer sb = new StringBuffer();
                sb.append("门锁状态:\n").append("网关地址:").append(ls.getAddr()).append("\n")
                        .append("生命周期状态:").append(lockStatus).append("\n")
                        .append("时分秒:").append(ls.getTime()).append("\n")
                        .append("电量:").append(ls.getElectricValue() + "%").append("\n")
                        .append("学生开门卡数量:").append(ls.getStudentOpenCardNum()).append("\n")
                        .append("管理开门卡数量:").append(ls.getManagerOpenCardNum()).append("\n")
                        .append("特殊开门卡数量:").append(ls.getOtherOpenCardNum()).append("\n")
                        .append("授权卡数量:").append(ls.getAuthoryCardNum()).append("\n")
                        .append("记录条数信息:").append(ls.getRecordNum()).append("\n")
                        .append("版本号:").append(ls.getVersonCode()).append("\n");
//                recordInfo.append(sb.toString());
                refreshLogView(sb.toString());
//                infoEdit.setText(recordInfo.toString());
                break;
            //接收离线上传记录
            case BleObserverConstance.RECEIVER_OFFLINE_UPLOAD_RECORD:
                final boolean show;
                if (ob.getObject().toString().subSequence(40, 44).equals("0000") && ob.getObject().toString().substring(44, 46).equals("00")) {
//                    ToastUtil.showText("没有可上传记录信息");
//                    recordInfo.append("没有可上传记录信息\n");
                    refreshLogView("无记录信息\n");
//                    infoEdit.setText(recordInfo.toString());
//                    WaitTool.dismissDialog();
//                    return;
                    show = false;
                } else {
                    show = true;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", SKApplication.loginToken);
                params.put("itid", offlineRecorderResponse.getItid());
                params.put("record", ob.getObject().toString());
//                final String recordNum = ob.getObject().toString().substring(44, 46);
                OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_AUTHORY_RECORDER_SAVE_RESULT, params, UploadRecordResultResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse br) {
                        super.onSuccess(br);
                        UploadRecordResultResponse urrr = (UploadRecordResultResponse) br;
                        WaitTool.dismissDialog();
                        if (urrr.getErrCode() == 0) {
                            if (show) {
                                ToastUtil.showText("离线上传记录成功");
//                            recordInfo.append("离线上传记录成功\n");
                                refreshLogView("离线上传记录成功\n");
                            }
                            Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
                            intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, urrr.getResultOrder());
                            sendBroadcast(intent);
//                            if (offlineRecorderResponse != null) {
//                                String orignOrder = offlineRecorderResponse.getOrder();
//                                StringBuffer newOrder = new StringBuffer();
//                                newOrder.append(orignOrder.substring(0, 20)).append("01A9").append(orignOrder.substring(24, 30)).append(recordNum);
//                                Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
//                                intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, BLECommandManager.getSendBlueId(newOrder.toString(), "", ""));
//                                sendBroadcast(intent);
//                            }
                        } else {
                            ToastUtil.showText(br.getErrMsg());
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        ToastUtil.showText(message);
                        WaitTool.dismissDialog();
                    }
                });
                break;
        }

    }
}
