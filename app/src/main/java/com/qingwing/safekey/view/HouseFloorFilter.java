package com.qingwing.safekey.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.imp.HouseFloorListener;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.okhttp3.response.FloorInfoResponse;
import com.qingwing.safekey.okhttp3.response.HouseInfoResponse;
import com.qingwing.safekey.okhttp3.response.ModelInfoResponse;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseFloorFilter extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private SpinerPopWindow housePop, floorPop, modelPop;
    private HouseFloorListener listener = null;
    private TextView houseTv, floorTv, modelTv;
    private LinearLayout houseLayout, floorLayout, modelLayout;
    private List<HouseInfoResponse.HouseInfo> houseDate = new ArrayList<>();
    private List<FloorInfoResponse.FloorInfo> floorDate = new ArrayList<>();
    private List<ModelInfoResponse.DevoceInfo> modelDate = new ArrayList<>();
    private String oldHouseSelectid = "";
    private String oldFloorSelectid = "";
    private String oldDeviceSelectid = "";
    private String houseSelectid = "";
    private String floorSelectid = "";
    private String deviceSelectid = "1";

    public void setListener(HouseFloorListener listener) {
        this.listener = listener;
    }

    public HouseFloorFilter(Context context) {
        super(context);
    }

    public HouseFloorFilter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//要用this 不然不会跑到下面的构造函数
    }

    public HouseFloorFilter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(Context context) {
        inflate(context, R.layout.house_floor_filter, this);
        setId(R.id.house_floor_filter);
        // 取到布局中的控件
        houseTv = (TextView) findViewById(R.id.filter_house);
        floorTv = (TextView) findViewById(R.id.filter_floor);
        modelTv = (TextView) findViewById(R.id.filter_model);
        houseLayout = (LinearLayout) findViewById(R.id.filter_house_layout);
        floorLayout = (LinearLayout) findViewById(R.id.filter_floor_layout);
        modelLayout = (LinearLayout) findViewById(R.id.filter_model_layout);
        houseTv.setOnClickListener(this);
        floorTv.setOnClickListener(this);
        modelTv.setOnClickListener(this);
        findViewById(R.id.filter_ok).setOnClickListener(this);
        ///////////////
        //////////////
        requestHouse();
        requestModel();
    }

    private void initHousePop() {
        final List<String> houselist = new ArrayList<>();
        for (HouseInfoResponse.HouseInfo hi : houseDate) {
            houselist.add(hi.getAgname());
        }
        housePop = new SpinerPopWindow<String>(mContext, houselist, houseItemClickListener);
        housePop.setOnDismissListener(dismissListener);
    }

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            //setTextImage(R.drawable.icon_down);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener houseItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (housePop != null && housePop.isShowing()) {
                housePop.dismiss();
            }
            houseTv.setText(houseDate.get(position).getAgname());
            requestFloor(houseDate.get(position).getAgid());
            houseSelectid = houseDate.get(position).getAgid();
        }
    };
    private AdapterView.OnItemClickListener floorItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (floorPop != null && floorPop.isShowing()) {
                floorPop.dismiss();
            }
            floorTv.setText(floorDate.get(position).getAgname());
            floorSelectid = floorDate.get(position).getAgid();
        }
    };
    private AdapterView.OnItemClickListener deviceItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (modelPop != null && modelPop.isShowing()) {
                modelPop.dismiss();
            }
            modelTv.setText(modelDate.get(position).getDevicename());
            deviceSelectid = modelDate.get(position).getDeviceid();
        }
    };

    private void initFloorPop() {
        List<String> floorlist = new ArrayList<>();
        if (floorDate != null) {
            for (FloorInfoResponse.FloorInfo fi : floorDate) {
                floorlist.add(fi.getAgname());
            }
        }
        floorPop = new SpinerPopWindow<String>(mContext, floorlist, floorItemClickListener);
        floorPop.setOnDismissListener(dismissListener);
    }

    private void initModelPop() {
        List<String> modellist = new ArrayList<>();
        for (ModelInfoResponse.DevoceInfo di : modelDate) {
            modellist.add(di.getDevicename());
        }
        modelPop = new SpinerPopWindow<String>(mContext, modellist, deviceItemClickListener);
        modelPop.setOnDismissListener(dismissListener);
    }

    @Override
    public void onClick(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.filter_house:
                if (housePop == null) {
                    return;
                }
                if (floorPop != null && floorPop.isShowing()) {
                    floorPop.dismiss();
                }
                if (modelPop != null && modelPop.isShowing()) {
                    modelPop.dismiss();
                }
                housePop.setWidth(houseLayout.getWidth());
                housePop.showUp(houseLayout);
                break;
            case R.id.filter_floor:
                if (floorPop == null) {
                    ToastUtil.showText("请选择楼栋");
                    return;
                }
                if (housePop != null && housePop.isShowing()) {
                    housePop.dismiss();
                }
                if (modelPop != null && modelPop.isShowing()) {
                    modelPop.dismiss();
                }
                int[] flocation = new int[2];
                floorLayout.getLocationOnScreen(flocation);
                //在控件上方显示
                floorPop.setWidth(floorLayout.getWidth());
                floorPop.showAtLocation(floorLayout, Gravity.NO_GRAVITY, flocation[0], flocation[1]);

                break;
            case R.id.filter_model:
                if (modelPop == null) {
                    return;
                }
                if (floorPop != null && floorPop.isShowing()) {
                    floorPop.dismiss();
                }
                if (housePop != null && housePop.isShowing()) {
                    housePop.dismiss();
                }
                modelPop.setWidth(modelLayout.getWidth());
                modelPop.showUp(modelLayout);
                break;
            case R.id.filter_ok:
                if (TextUtils.isEmpty(houseSelectid)) {
                    ToastUtil.showText("请选择楼栋");
                    return;
                }
                if (TextUtils.isEmpty(floorSelectid)) {
                    ToastUtil.showText("请选择楼层");
                    return;
                }
                if (TextUtils.isEmpty(deviceSelectid)) {
                    ToastUtil.showText("请选择设备类型");
                    return;
                }
                if (oldHouseSelectid.equals(houseSelectid) && oldFloorSelectid.equals(floorSelectid) && oldDeviceSelectid.equals(deviceSelectid)) {
                    return;
                }
                oldHouseSelectid = houseSelectid;
                oldFloorSelectid = floorSelectid;
                oldDeviceSelectid = deviceSelectid;
                if (listener != null) {
                    listener.affirm(houseSelectid, floorSelectid, deviceSelectid);
                }
                break;
        }
    }

    private void requestHouse() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        OkHttpUtils.getAsyn(NetWorkConfig.OBTAIN_HOUSE_NUMBER, params, HouseInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                HouseInfoResponse hr = (HouseInfoResponse) br;
                if (hr.getErrCode() == 0) {
                    houseDate = hr.getLd();
                    if (houseDate != null && !houseDate.isEmpty()) {
                        initHousePop();
                    }
                } else {
                    ToastUtil.showText(hr.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText("request fail message=" + message);
            }
        });
    }

    private void requestFloor(String agid) {
        floorSelectid = "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("agid", agid);
        OkHttpUtils.getAsyn(NetWorkConfig.OBTAIN_FLOOR_NUMBER, params, FloorInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                FloorInfoResponse hr = (FloorInfoResponse) br;
                if (hr.getErrCode() == 0) {
                    floorDate = hr.getLc();
                    initFloorPop();
                } else {
                    ToastUtil.showText(hr.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText("request fail message=" + message);
            }
        });
    }

    private void requestModel() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        OkHttpUtils.getAsyn(NetWorkConfig.OBTAIN_MODEL_NUMBER, params, ModelInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                ModelInfoResponse mr = (ModelInfoResponse) br;
                if (mr.getErrCode() == 0) {
                    modelDate = mr.getDevicetype();
                    if (modelDate != null && !modelDate.isEmpty()) {
                        initModelPop();
                    }
                } else {
                    ToastUtil.showText(mr.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText("request fail message=" + message);
            }
        });
    }

}
