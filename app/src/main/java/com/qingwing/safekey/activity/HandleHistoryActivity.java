package com.qingwing.safekey.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.adapter.CustomBaseQuickAdapter;
import com.qingwing.safekey.dialog.WaitTool;
import com.qingwing.safekey.imp.TitleBarListener;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.okhttp3.response.DeviceHistoryListResponse;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;
import com.qingwing.safekey.view.DateTimePickDialogUtil;
import com.qingwing.safekey.view.RecyclerViewSwipeLayout;
import com.qingwing.safekey.view.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-08-14.
 * email:shoxgov@126.com
 */

public class HandleHistoryActivity extends BaseActivity {
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.history_start_date)
    TextView startDate;
    @Bind(R.id.history_end_date)
    TextView endDate;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private String selectDeviceType, selectDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_history);
        ButterKnife.bind(this);
        selectDeviceType = getIntent().getStringExtra("selectDeviceType");
        selectDeviceId = getIntent().getStringExtra("selectDeviceId");
        startDate.setTag("");
        endDate.setTag("");
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
        recyclerSwipeLayout.createAdapter(R.layout.list_handle_history_item, false);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        View head = LayoutInflater.from(this).inflate(R.layout.list_handle_history_item, null, false);
        recyclerSwipeLayout.addHeaderView(head);
    }

    /**
     * token	是	令牌
     * devicetype	是	设备类型
     * deviceid	是	设备id
     * sdate	否	开始时间
     * edate	否	结束时间
     * rows	是	每页记录数
     * page	是	页数
     */
    public void requestHistory() {
        WaitTool.showDialog(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("devicetype", selectDeviceType);
        params.put("deviceid", selectDeviceId);
        params.put("sdate", startDate.getTag().toString());
        params.put("edate", endDate.getTag().toString());
        params.put("page", "" + pageNo);
        params.put("rows", "" + pageSize);
        OkHttpUtils.getAsyn(NetWorkConfig.OBTAIN_DEVICE_HISTORY_LIST, params, DeviceHistoryListResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                DeviceHistoryListResponse dlr = (DeviceHistoryListResponse) br;
                WaitTool.dismissDialog();
                if (dlr.getErrCode() == 0) {
                    if (TextUtils.isEmpty(dlr.getTotal()) || dlr.getTotal().equals("0")) {
                        recyclerSwipeLayout.setEmpty();
                        return;
                    }
                    try {
                        totalPage = Integer.parseInt(dlr.getTotal()) / pageSize;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    recyclerSwipeLayout.addData(dlr.getRecord());
                } else {
                    ToastUtil.showText(dlr.getErrMsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                WaitTool.dismissDialog();
                ToastUtil.showText("request fail message=" + message);
            }
        });
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestHistory();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            DeviceHistoryListResponse.DeviceHistoryInfo dhi = (DeviceHistoryListResponse.DeviceHistoryInfo) itemModel;
            baseViewHolder.setText(R.id.handle_history_type, dhi.getRecordtype());
            baseViewHolder.setText(R.id.handle_history_date, dhi.getRecorddate());
            baseViewHolder.setText(R.id.handle_history_author, dhi.getUsername());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.history_start_date, R.id.history_end_date, R.id.history_date_ok})
    public void onViewClicked(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.history_start_date:
                dateSelect(0);
                break;
            case R.id.history_end_date:
                dateSelect(1);
                break;
            case R.id.history_date_ok:
//                if (startDate.getTag() == null) {
//                    ToastUtil.showText("请选择开始时间");
//                    return;
//                }
//                if (endDate.getTag() == null) {
//                    ToastUtil.showText("请选择结束时间");
//                    return;
//                }
                recyclerSwipeLayout.setNewData(new ArrayList<DeviceHistoryListResponse.DeviceHistoryInfo>());
                requestHistory();
                break;
        }
    }

    private void dateSelect(final int type) {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                String facttime = DateTimePickDialogUtil.getDateString(date, simpleDateFormat).toString();
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyMMdd HH:mm:ss");// HH:mm:ss
                String tagtime = DateTimePickDialogUtil.getDateString(date, simpleDateFormat2).toString();
                if (TextUtils.isEmpty(tagtime)) {
                    ToastUtil.showText("日期错误");
                    return;
                }
                switch (type) {
                    case 0:
                        startDate.setTag(tagtime);
                        startDate.setText(Html.fromHtml("开始时间<br>" + facttime.toString()));
                        break;
                    case 1:
                        endDate.setTag(tagtime);
                        endDate.setText(Html.fromHtml("结束时间<br>" + facttime.toString()));
                        break;
                }
            }
        }).setType(new boolean[]{true, true, true, true, true, true}).build();
        pvTime.show();
    }
}
