package com.qingwing.safekey.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.adapter.CustomBaseQuickAdapter;
import com.qingwing.safekey.bean.BtDeviceInfo;
import com.qingwing.safekey.bluetooth.BleObserverConstance;
import com.qingwing.safekey.bluetooth.BluetoothService;
import com.qingwing.safekey.dialog.AffirmDialog;
import com.qingwing.safekey.dialog.WaitTool;
import com.qingwing.safekey.imp.DialogCallBack;
import com.qingwing.safekey.imp.HouseFloorListener;
import com.qingwing.safekey.imp.TitleBarListener;
import com.qingwing.safekey.observable.ObservableBean;
import com.qingwing.safekey.observable.ObserverManager;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.okhttp3.response.DeviceListResponse;
import com.qingwing.safekey.okhttp3.response.LoginResponse;
import com.qingwing.safekey.utils.LogUtil;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;
import com.qingwing.safekey.view.HouseFloorFilter;
import com.qingwing.safekey.view.RecyclerViewSwipeLayout;
import com.qingwing.safekey.view.TitleBar;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-08-10.
 * email:shoxgov@126.com
 */

public class ScanListActivity extends BaseActivity implements Observer {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.house_floor_filter)
    HouseFloorFilter houseFloorFilter;

    /**
     * 请求的所有设备
     */
    private Vector<DeviceListResponse.DeviceInfo> deviceList = new Vector<>();
    /**
     * 筛选条件保存
     */
    private String selectHouseId, selectFloorId;
    private String selectDeviceType = "1";
    /**
     * 点击选中的设备
     */
    private String selectDeviceId = "";
    private String selectGatewayCode = "";
    private String selectRoomid = "";
    /**
     * 创建单长期线程，用于轮询
     */
    ScheduledExecutorService scheduledThreadPool;

    private int gocount = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    freshBtList();
                    break;
                case 1:
                    freshToken();
                    break;
            }
        }
    };

    @Override
    public void update(Observable o, Object obj) {
        ObservableBean ob = (ObservableBean) obj;
        switch (ob.getWhat()) {
            case BleObserverConstance.BOX_CONNTEC_BLE_STATUS:
                if ((boolean) ob.getObject()) {
                } else {
                    ToastUtil.showText("连接失败,请重试");
                    WaitTool.dismissDialog();
                }
                break;
            case BleObserverConstance.ACTION_GATT_CONNECT_SUCCESS:
//            case BleObserverConstance.BOX_CONNTECING_BLE:
                LogUtil.d("  BOX_CONNTECING_BLE  ");
                //创建旋转动画
                WaitTool.dismissDialog();
                ToastUtil.showText("连接成功");
                Intent intent = new Intent();
                intent.setClass(this, BtHandleActivity.class);
                intent.putExtra("selectHouseId", selectHouseId);
                intent.putExtra("selectFloorId", selectFloorId);
                intent.putExtra("selectDeviceType", selectDeviceType);
                intent.putExtra("luckId", selectRoomid);
                intent.putExtra("selectDeviceId", selectDeviceId);
                intent.putExtra("selectGatewayCode", selectGatewayCode);
                startActivity(intent);
                break;
        }
    }

    private void freshBtList() {
        if (bluetoothService != null) {
            List<BtDeviceInfo> scanMap = bluetoothService.getBtScanMap();
            if (!deviceList.isEmpty()) {
                for (int i = 0; i < deviceList.size(); i++) {
                    boolean find = false;
                    for (BtDeviceInfo bi : scanMap) {
                        if (!TextUtils.isEmpty(deviceList.get(i).getWcode()) && deviceList.get(i).getWcode().equals(bi.getName())) {
                            deviceList.get(i).setBtAddress(bi.getAddress());
                            deviceList.get(i).setRssi(bi.getRssi());
                            find = true;
                            break;
                        }
                    }
                    deviceList.get(i).setOnline(find);
                }
            }
            try {
                recyclerSwipeLayout.setNewData(deviceList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bluetoothService != null) {
            bluetoothService.clearDeviceMap();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_list);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        ObserverManager.getObserver().addObserver(this);
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        init();
        scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                gocount++;
                mHandler.sendEmptyMessage(0);
                if (gocount == 40) {
                    gocount = 0;
                    mHandler.sendEmptyMessage(1);
                }
            }
        }, 1000, 5000, TimeUnit.MILLISECONDS);//表示延迟100微秒后每750微秒执行一次。
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        ObserverManager.getObserver().deleteObserver(this);
        if (bluetoothService != null) {
            bluetoothService.onDestroy();
        }
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdownNow();
        }
        super.onDestroy();
    }

    private void init() {
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                ToastUtil.showText("刷新附近设备");
                if (bluetoothService != null) {
                    bluetoothService.clearDeviceMap();
                    mHandler.removeMessages(0);
                    mHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void rightClick() {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                if (TextUtils.isEmpty(selectDeviceId)) {
                    ToastUtil.showText("请选择设备");
                    return;
                }
                Intent history = new Intent();
                history.setClass(ScanListActivity.this, HandleHistoryActivity.class);
                history.putExtra("selectHouseId", selectHouseId);
                history.putExtra("selectFloorId", selectFloorId);
                history.putExtra("selectDeviceType", selectDeviceType);
                history.putExtra("selectDeviceId", selectDeviceId);
                startActivity(history);
            }
        });
        recyclerSwipeLayout.createAdapter(R.layout.list_scan_item, false);
        recyclerSwipeLayout.setXCallBack(callBack);

        houseFloorFilter.setListener(new HouseFloorListener() {
            @Override
            public void affirm(String house, String floor, String device) {
                selectHouseId = house;
                selectFloorId = floor;
                selectDeviceType = device;
                requestDevices(house, floor, device);
            }
        });
        requestDevices("", "", "");//传空获取默认授权列表
    }

    /* token	是	令牌
     ldid	否	楼栋id
     lcid	否	楼层id
     devicetype	否	设备类型
     rows	是	每页记录数
     page	是	页数*/
    private void requestDevices(String house, String floor, String device) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("ldid", house);
        params.put("lcid", floor);
        params.put("devicetype", device);
        params.put("rows", "" + pageSize);
        params.put("page", "" + pageNo);
        WaitTool.showDialog(this, "请求数据中");
        OkHttpUtils.getAsyn(NetWorkConfig.OBTAIN_DEVICE_LIST, params, DeviceListResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                WaitTool.dismissDialog();
                DeviceListResponse dlr = (DeviceListResponse) br;
                if (dlr.getErrCode() == 0) {
                    List<DeviceListResponse.DeviceInfo> deviceTemp = dlr.getDevice();
                    deviceList.clear();
                    try {
                        totalPage = (int) (Integer.parseInt(dlr.getTotal()) / pageSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (deviceTemp == null || deviceTemp.isEmpty()) {
                        ToastUtil.showText("没有请求到数据");
                        return;
                    }
                    for (DeviceListResponse.DeviceInfo di : deviceTemp) {
                        deviceList.add(di);
                    }
                    mHandler.removeMessages(0);
                    mHandler.sendEmptyMessage(0);
                } else {
                    ToastUtil.showText(dlr.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText("request fail message=" + message);
            }
        });
    }

    private void freshToken() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("refreshentoken", SKApplication.refreshentoken);
        OkHttpUtils.getAsyn(NetWorkConfig.FRESH_TOKEN, params, LoginResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                LoginResponse lr = (LoginResponse) br;
                if (lr.getErrCode() == 0) {
                    SKApplication.loginToken = lr.getToken();
                } else {
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(final BaseViewHolder baseViewHolder, Object itemModel) {
            final DeviceListResponse.DeviceInfo bdi = (DeviceListResponse.DeviceInfo) itemModel;
            baseViewHolder.setText(R.id.scan_bt_addr, bdi.getDevicename());
            baseViewHolder.setText(R.id.scan_bt_mode, "类型：" + bdi.getDevicetypename());
            if (bdi.isOnline()) {
                baseViewHolder.setText(R.id.scan_bt_rssi, "" + bdi.getRssi());
                baseViewHolder.setImageResource(R.id.scan_bt_status, R.mipmap.bluetooth_online);
            } else {
                baseViewHolder.setText(R.id.scan_bt_rssi, "");
                baseViewHolder.setImageResource(R.id.scan_bt_status, R.mipmap.bluetooth_disconnect);
            }
            if (selectDeviceId.equals(bdi.getDeviceid())) {
                baseViewHolder.getConvertView().setBackgroundResource(R.color.bluetheme);
            } else {
                baseViewHolder.getConvertView().setBackgroundResource(R.color.white);
            }
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDeviceId = bdi.getDeviceid();
                    selectGatewayCode = bdi.getGatewaycode();
                    selectRoomid = bdi.getRcode();
                    if (bdi.isOnline()) {
                        AffirmDialog warnDialog = new AffirmDialog(ScanListActivity.this, "确定连接XXOO？".replace("XXOO", bdi.getDevicename()), new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                                WaitTool.showDialog(ScanListActivity.this, "正在连接...");
                                if (bluetoothService != null) {
                                    bluetoothService.connectByAddress(bdi.getBtAddress());
                                }
                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        warnDialog.show();
                    } else {
//                        ToastUtil.showText("该设备不在附近或者搜索不到");
                    }
                    recyclerSwipeLayout.notifyDataSetChanged();
                }
            });
        }
    };

    private BluetoothService bluetoothService;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BluetoothService.MyBinder binder = (BluetoothService.MyBinder) service;
            bluetoothService = binder.getService();
            LogUtil.d("onServiceConnected -------------------------");
            bluetoothService.startBLEscan();
        }

        //client 和service连接意外丢失时，会调用该方法
        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("onServiceDisconnected -----------------------------");
        }
    };

}
