package com.qingwing.safekey.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.adapter.AuthoryAdapter;
import com.qingwing.safekey.bean.CardOrderResult;
import com.qingwing.safekey.bean.FingerOrderResult;
import com.qingwing.safekey.bean.OfflineAuthoryUserInfo;
import com.qingwing.safekey.bean.OrderResultBean;
import com.qingwing.safekey.bean.OrderResultServerBean;
import com.qingwing.safekey.bluetooth.BleObserverConstance;
import com.qingwing.safekey.bluetooth.BluetoothService;
import com.qingwing.safekey.dialog.AffirmDialog;
import com.qingwing.safekey.dialog.WaitTool;
import com.qingwing.safekey.imp.DialogCallBack;
import com.qingwing.safekey.imp.TitleBarListener;
import com.qingwing.safekey.observable.ObservableBean;
import com.qingwing.safekey.observable.ObserverManager;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.okhttp3.response.OfflineAuthoryUserInfoResponse;
import com.qingwing.safekey.okhttp3.response.OrderResponse;
import com.qingwing.safekey.utils.LogUtil;
import com.qingwing.safekey.utils.SerializationDefine;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;
import com.qingwing.safekey.view.DateTimePickDialogUtil;
import com.qingwing.safekey.view.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-08-13.
 * email:shoxgov@126.com
 */

public class OfflineAuthorizationActivity extends BaseActivity implements Observer {
    @Bind(R.id.offline_authory_settle_rg)
    RadioGroup settleRg;
    @Bind(R.id.offline_authory_search_edit)
    EditText searchEdit;
    @Bind(R.id.offline_authory_search_ok)
    TextView searchBtn;
    @Bind(R.id.offline_authory_pwd_layout)
    LinearLayout pwdLayout;
    @Bind(R.id.offline_authory_start_time)
    TextView startTimeTv;
    @Bind(R.id.offline_authory_end_time)
    TextView endTimeTv;
    @Bind(R.id.offline_authory_password)
    EditText pwdEdit;
    @Bind(R.id.offline_authory_count)
    EditText countEdit;
    @Bind(R.id.offline_authory_info_list)
    ListView infoList;
//    @Bind(R.id.offline_authory_info_edit)
//    EditText infoEdit;

    private String roomid;
    AuthoryAdapter adapter;
    private HashMap<String, OrderResultBean> orderHashMap = new HashMap<>();
    private Vector<String> orderKeyVector = new Vector<>();
    private String runingOrderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_authory);
        ButterKnife.bind(this);
        ObserverManager.getObserver().addObserver(this);
        roomid = getIntent().getStringExtra("roomid");
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObserverManager.getObserver().deleteObserver(this);
        ButterKnife.unbind(this);
    }

    private void init() {
        TitleBar titleBar = findViewById(R.id.titlelayout);
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        adapter = new AuthoryAdapter(this);
        infoList.setAdapter(adapter);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    searchBtn.performClick();
                }
                return false;
            }

        });
    }

    @OnClick({R.id.offline_authory_search_ok, R.id.offline_authory_start_time, R.id.offline_authory_end_time, R.id.offline_authory_send})
    public void onViewClicked(View view) {
        if(Utils.isFastDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.offline_authory_start_time:
                dateSelect(0);
                break;
            case R.id.offline_authory_end_time:
                dateSelect(1);
                break;
            /**
             * token	是	令牌
             roomid	是	房间id
             sqtype	是	授权类型
             search	否	学号/姓名
             rows	是	每页记录数
             page	是	页数
             */
            case R.id.offline_authory_search_ok:
                String searchContent = searchEdit.getText().toString();
                if (TextUtils.isEmpty(searchContent)) {
                    ToastUtil.showText("请输入学号");
                    return;
                }
                WaitTool.showDialog(this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", SKApplication.loginToken);
                params.put("roomid", roomid);
                params.put("search", searchContent);
                params.put("rows", "100");
                params.put("page", "1");
                switch (settleRg.getCheckedRadioButtonId()) {//"1（密码）/2（指纹）/3（卡片）",
                    case R.id.offline_authory_settle_type_1:
                        params.put("sqtype", "1");
                        break;
                    case R.id.offline_authory_settle_type_2:
                        params.put("sqtype", "2");
                        break;
                    case R.id.offline_authory_settle_type_3:
                        params.put("sqtype", "3");
                        break;
                }
                OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_AUTHORY_OBTAIN_USERINFO, params, OfflineAuthoryUserInfoResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse br) {
                        super.onSuccess(br);
                        OfflineAuthoryUserInfoResponse or = (OfflineAuthoryUserInfoResponse) br;
                        WaitTool.dismissDialog();
                        if (or.getErrCode() == 0) {
                            if (or.getPerson() == null || or.getPerson().isEmpty()) {
                                ToastUtil.showText("无搜索到结果");
                                return;
                            }
                            if (or.getPerson().size() > 1) {
                                showListDialog(or.getPerson());
                            } else {
                                addAffirmDialog(or.getPerson().get(0));
                            }
                        } else {
                            ToastUtil.showText(or.getErrMsg());
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        ToastUtil.showText(message);
                    }
                });
                break;
            case R.id.offline_authory_send:
                String startTime = startTimeTv.getText().toString();
                String endTime = endTimeTv.getText().toString();
                String count = countEdit.getText().toString();
                if (TextUtils.isEmpty(startTime)) {
                    ToastUtil.showText("请输入开始时间");
                    return;
                }
                if (TextUtils.isEmpty(endTime)) {
                    ToastUtil.showText("请输入结束时间");
                    return;
                }
                if (TextUtils.isEmpty(count)) {
                    ToastUtil.showText("请输入有效次数");
                    return;
                }
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("token", SKApplication.loginToken);
                params2.put("roomid", roomid);
                List<OfflineAuthoryUserInfoResponse.AuthoryUserInfo> tempData = adapter.getData();
                if (tempData == null || tempData.isEmpty()) {
                    ToastUtil.showText("请添加授权用户");
                    return;
                }
                List<OfflineAuthoryUserInfo> roomcard = new ArrayList<>();
                for (OfflineAuthoryUserInfoResponse.AuthoryUserInfo aui : tempData) {
                    OfflineAuthoryUserInfo oui = new OfflineAuthoryUserInfo();
                    oui.setSdate(startTime);
                    oui.setEdate(endTime);
                    oui.setRccount(count);
                    oui.setPersoncode(aui.getPersoncode());
                    switch (settleRg.getCheckedRadioButtonId()) {//"1（密码）/2（指纹）/3（卡片）",
                        case R.id.offline_authory_settle_type_1:
                            oui.setRoomcardtype(1);
                            String pwd = pwdEdit.getText().toString();
                            if (TextUtils.isEmpty(pwd)) {
                                ToastUtil.showText("请输入授权密码");
                                return;
                            }
                            oui.setPassword(pwd);
                            break;
                        case R.id.offline_authory_settle_type_2:
                            oui.setRoomcardtype(2);
                            oui.setPassword("");
                            break;
                        case R.id.offline_authory_settle_type_3:
                            oui.setRoomcardtype(3);
                            oui.setPassword("");
                            break;
                    }
                    roomcard.add(oui);
                }
                params2.put("roomcard", SerializationDefine.List2Str(roomcard));

                OkHttpUtils.postAsyn(NetWorkConfig.OBTAIN_OFFLINE_AUTHORY_SAVE, params2, OrderResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse br) {
                        super.onSuccess(br);
                        OrderResponse or = (OrderResponse) br;
                        if (or.getErrCode() == 0) {
                            List<OrderResponse.CardOrder> cardOrder = or.getOrder().getCard();
                            if (cardOrder != null && !cardOrder.isEmpty()) {
                                for (OrderResponse.CardOrder co : cardOrder) {
                                    OrderResultBean orb = new OrderResultBean();
                                    orb.setType(2);//0:密码；1：指纹； 2：卡片;
                                    orb.setIds(co.getRcids());
                                    orb.setOrder(co.getCardorder());
                                    orderHashMap.put(2 + "#" + co.getRcids(), orb);
                                }
                            }
                            List<OrderResponse.FingerOrder> fingerOrder = or.getOrder().getFinger();
                            if (fingerOrder != null && !fingerOrder.isEmpty()) {
                                for (OrderResponse.FingerOrder fo : fingerOrder) {
                                    OrderResultBean orb = new OrderResultBean();
                                    orb.setType(1);//0:密码；1：指纹； 2：卡片;
                                    orb.setIds(fo.getRfids());
                                    orb.setOrder(fo.getFinorder());
                                    orderHashMap.put(1 + "#" + fo.getFinorder(), orb);
                                }
                            }
                            if (!orderHashMap.isEmpty()) {
                                WaitTool.showDialog(OfflineAuthorizationActivity.this);
                                sendOrderToBt();
                            }
                        } else {
                            ToastUtil.showText(or.getErrMsg());
                        }
                    }


                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        ToastUtil.showText(message);
                    }
                });
                break;
        }
    }

    private void sendOrderToBt() {
        Set<String> orderKeySet = orderHashMap.keySet();
        for (String s : orderKeySet) {
            orderKeyVector.add(s);
        }
        runingOrderKey = orderKeyVector.remove(0);
        sendOrderCommand(runingOrderKey);
    }

    private void sendOrderCommand(String key) {
        Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
        intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, orderHashMap.get(key).getOrder());
        sendBroadcast(intent);
    }

    @Override
    public void update(Observable o, Object obj) {
        ObservableBean ob = (ObservableBean) obj;
        switch (ob.getWhat()) {
            case BleObserverConstance.LOCK_OFFLINE_AUTHORY_COMMAND_RESULT:
                LogUtil.d("  LOCK_OFFLINE_AUTHORY_COMMAND_RESULT  ");
                //创建旋转动画
                OrderResultBean order = orderHashMap.get(runingOrderKey);
                order.setOrderresult("result");
                orderHashMap.remove(runingOrderKey);
                orderHashMap.put(runingOrderKey, order);
                if (orderKeyVector.isEmpty()) {

                    submitOfflineCommand();
                } else {
                    runingOrderKey = orderKeyVector.remove(0);
                    sendOrderCommand(runingOrderKey);
                }
                break;
        }
    }

    private void dateSelect(final int type) {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                String facttime = DateTimePickDialogUtil.getDateString(date, simpleDateFormat).toString();
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");// HH:mm:ss
                String tagtime = DateTimePickDialogUtil.getDateString(date, simpleDateFormat2).toString();
                if (TextUtils.isEmpty(tagtime)) {
                    ToastUtil.showText("日期错误");
                    return;
                }
                switch (type) {
                    case 0:
                        startTimeTv.setTag(tagtime);
                        startTimeTv.setText(tagtime);
                        break;
                    case 1:
                        endTimeTv.setTag(tagtime);
                        endTimeTv.setText(tagtime);
                        break;
                }
            }
        }).setType(new boolean[]{false, false, false, true, true, false}).build();
        pvTime.show();
    }

    private void addAffirmDialog(final OfflineAuthoryUserInfoResponse.AuthoryUserInfo person) {
        final String info = "姓名：" + person.getPersonname() + "    学号：" + person.getPersoncode();
        AffirmDialog warnDialog = new AffirmDialog(OfflineAuthorizationActivity.this, info, "确认添加", "暂不添加", new DialogCallBack() {
            @Override
            public void OkDown(Object obj) {
                adapter.addData(person);
            }

            @Override
            public void CancleDown() {

            }
        });
        warnDialog.show();
    }

    private void showListDialog(final List<OfflineAuthoryUserInfoResponse.AuthoryUserInfo> person) {
        final String[] items = new String[person.size()];//{"我是1", "我是2", "我是3", "我是4"};
        int i = 0;
        for (OfflineAuthoryUserInfoResponse.AuthoryUserInfo aui : person) {
            items[i] = "姓名：" + aui.getPersonname() + "    学号：" + aui.getPersoncode();
            i++;
        }
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setTitle("请选择添加用户");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // which 下标从0开始 ...To-do
                addAffirmDialog(person.get(which));
            }
        });
        listDialog.show();
    }

    private void submitOfflineCommand() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("roomid", roomid);
        OrderResultServerBean orsb = new OrderResultServerBean();
        List<FingerOrderResult> fingerOrderResults = new ArrayList<>();
        List<CardOrderResult> cardOrderResult = new ArrayList<>();
        for(OrderResultBean orb: orderHashMap.values()){

            switch (orb.getType()) {//0:密码； 1：指纹；2：卡片;
                case 0:
                    break;
                case 1:
                    FingerOrderResult fr = new FingerOrderResult();
                    fr.setFinorderresult(orb.getOrderresult());
                    fr.setRfids(orb.getIds());
                    fingerOrderResults.add(fr);
                    break;
                case 2:
                    CardOrderResult cr = new CardOrderResult();
                    cr.setCardorderresult(orb.getOrderresult());
                    cr.setRcids(orb.getIds());
                    cardOrderResult.add(cr);
                    break;
            }
        }
        orsb.setFingerresult(SerializationDefine.List2Str(fingerOrderResults));
        orsb.setCardresult(SerializationDefine.List2Str(cardOrderResult));
        params.put("orderresult", SerializationDefine.Object2String(orsb));
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_AUTHORY_SAVE_RESULT, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                WaitTool.dismissDialog();
                if (br.getErrCode() == 0) {
                    ToastUtil.showText("下发成功");
                    adapter.clear();
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

}
