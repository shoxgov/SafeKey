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
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.adapter.DelAuthoryAdapter;
import com.qingwing.safekey.bean.OfflineDelAuthoryRoomBack;
import com.qingwing.safekey.bean.OrderResultBean;
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
import com.qingwing.safekey.okhttp3.response.OfflineDelAuthoryUserInfoResponse;
import com.qingwing.safekey.okhttp3.response.OrderResponse;
import com.qingwing.safekey.utils.LogUtil;
import com.qingwing.safekey.utils.SerializationDefine;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;
import com.qingwing.safekey.view.TitleBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
 * Created by wangshengyin on 2018-08-14.
 * email:shoxgov@126.com
 */

public class OfflineDeleteAuthorizationActivity extends BaseActivity implements Observer {
    @Bind(R.id.offline_del_settle_rg)
    RadioGroup settleRg;
    @Bind(R.id.offline_del_search_edit)
    EditText searchEdit;
    //    @Bind(R.id.offline_del_result_edit)
//    EditText resultEdit;
    @Bind(R.id.offline_del_authory_info_list)
    ListView infoList;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 100;
    private String roomid;
    private DelAuthoryAdapter adapter;
    private HashMap<String, OrderResultBean> orderHashMap = new HashMap<>();
    private Vector<String> orderKeyVector = new Vector<>();
    private String runingOrderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_del_authory);
        ButterKnife.bind(this);
        ObserverManager.getObserver().addObserver(this);
        roomid = getIntent().getStringExtra("roomid");
        init();
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
        adapter = new DelAuthoryAdapter(this);
        infoList.setAdapter(adapter);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    String key = searchEdit.getText().toString();
                    if (!TextUtils.isEmpty(key)) {
                        rquestOfflineDelUser(key);
                    }
                }
                return false;
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ObserverManager.getObserver().deleteObserver(this);
    }

    private void rquestOfflineDelUser(String key) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("roomid", roomid);
        switch (settleRg.getCheckedRadioButtonId()) {//"1（密码）/2（指纹）/3（卡片）",
            case R.id.offline_del_settle_type_1:
                params.put("sqtype", "1");
                break;
            case R.id.offline_del_settle_type_2:
                params.put("sqtype", "2");
                break;
            case R.id.offline_del_settle_type_3:
                params.put("sqtype", "3");
                break;
        }
        params.put("search", key);
        params.put("rows", pageSize + "");
        params.put("page", pageNo + "");
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_DEL_AUTHORY_USERINFO, params, OfflineDelAuthoryUserInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                OfflineDelAuthoryUserInfoResponse odi = (OfflineDelAuthoryUserInfoResponse) br;
                WaitTool.dismissDialog();
                if (odi.getErrCode() == 0) {
                    if (odi.getRoomcard() == null || odi.getRoomcard().isEmpty()) {
                        ToastUtil.showText("无搜索到结果");
                        return;
                    }
                    if (odi.getRoomcard().size() > 1) {
                        showListDialog(odi.getRoomcard());
                    } else {
                        addAffirmDialog(odi.getRoomcard().get(0));
                    }
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

    private void addAffirmDialog(final OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo person) {
        String type = "密码";//"1（密码）/2（指纹）/3（卡片）",
        if (person.getSqtype().equals("1")) {
            type = "密码";
        } else if (person.getSqtype().equals("2")) {
            type = "指纹";
        } else if (person.getSqtype().equals("3")) {
            type = "卡片";
        }
        final String info = "姓名：" + person.getPersonname() + "    学号：" + person.getPersoncode() + "\n授权类型：" + type;
        AffirmDialog warnDialog = new AffirmDialog(OfflineDeleteAuthorizationActivity.this, info, "确认添加", "暂不添加", new DialogCallBack() {
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

    private void showListDialog(final List<OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo> person) {
        final String[] items = new String[person.size()];//{"我是1", "我是2", "我是3", "我是4"};
        int i = 0;
        for (OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo aui : person) {
            String type = "密码";//"1（密码）/2（指纹）/3（卡片）",
            if (aui.getSqtype().equals("1")) {
                type = "密码";
            } else if (aui.getSqtype().equals("2")) {
                type = "指纹";
            } else if (aui.getSqtype().equals("3")) {
                type = "卡片";
            }
            items[i] = "姓名：" + aui.getPersonname() + "    学号：" + aui.getPersoncode() + "\n授权类型：" + type;
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

    private void submitOfflineDelAuthorResult() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("roomid", roomid);
        JSONObject orderresult = new JSONObject();
        JSONArray cardOrderResult = new JSONArray();
        JSONArray fingerOrderResults = new JSONArray();
//        OrderResultServerBean orsb = new OrderResultServerBean();
//        List<FingerOrderResult> fingerOrderResults = new ArrayList<>();
//        List<CardOrderResult> cardOrderResult = new ArrayList<>();
        for (OrderResultBean orb : orderHashMap.values()) {
            switch (orb.getType()) {//0:密码； 1：指纹；2：卡片;
                case 0:
                    break;
                case 1:
                    try {
                        JSONObject fjson = new JSONObject();
                        fjson.put("finorderresult", orb.getOrderresult());
                        fjson.put("rfids", orb.getIds());
                        fingerOrderResults.put(fjson);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
//                    FingerOrderResult fr = new FingerOrderResult();
//                    fr.setFinorderresult(orb.getOrderresult());
//                    fr.setRfids(orb.getIds());
                    break;
                case 2:
                    try {
                        JSONObject cjson = new JSONObject();
                        cjson.put("finorderresult", orb.getOrderresult());
                        cjson.put("rfids", orb.getIds());
                        cardOrderResult.put(cjson);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
//                    CardOrderResult cr = new CardOrderResult();
//                    cr.setCardorderresult(orb.getOrderresult());
//                    cr.setRcids(orb.getIds());
//                    cardOrderResult.put(cr.toString());
                    break;
            }
        }
//        orsb.setFingerresult(SerializationDefine.List2Str(fingerOrderResults));
//        orsb.setCardresult(SerializationDefine.List2Str(cardOrderResult));
        try {
            orderresult.put("fingerresult", fingerOrderResults);
            orderresult.put("cardresult", cardOrderResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("orderresult", orderresult.toString());
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_DEL_AUTHORY_SAVE_RESULT, params, BaseResponse.class, new HttpCallback() {
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

    /**
     * roomback	是	取消授权信息数组
     * roombacktype	是	取消授权类型（1（密码）,2（指纹）,3（卡片））
     * roombackid	否	授权id
     */
    @OnClick({R.id.offline_del_send, R.id.offline_del_authory_search_ok})
    public void onViewClicked(View v) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.offline_del_authory_search_ok:
                String key = searchEdit.getText().toString();
                if (!TextUtils.isEmpty(key)) {
                    WaitTool.showDialog(this);
                    rquestOfflineDelUser(key);
                }
                break;

            case R.id.offline_del_send:
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", SKApplication.loginToken);
                params.put("roomid", roomid);
                List<OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo> tempData = adapter.getData();
                if (tempData == null || tempData.isEmpty()) {
                    ToastUtil.showText("请添加授权用户");
                    return;
                }
                List<OfflineDelAuthoryRoomBack> roomback = new ArrayList<>();
                for (OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo daui : tempData) {
                    OfflineDelAuthoryRoomBack rb = new OfflineDelAuthoryRoomBack();
                    rb.setRoombacktype(daui.getSqtype());
                    rb.setRoombackid(daui.getSqid());
                    roomback.add(rb);
                }
                params.put("roomback", SerializationDefine.List2Str(roomback));
                OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_DEL_AUTHORY, params, OrderResponse.class, new HttpCallback() {
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
                                WaitTool.showDialog(OfflineDeleteAuthorizationActivity.this);
                                sendOrderToBt();
                            } else {
                                WaitTool.dismissDialog();
                                ToastUtil.showText("该用户没有指令数据");
                            }
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
            case BleObserverConstance.LOCK_OFFLINE_DEL_AUTHORY_COMMAND_RESULT:
                LogUtil.d("  LOCK_OFFLINE_AUTHORY_COMMAND_RESULT  ");
                //创建旋转动画
                OrderResultBean order = orderHashMap.get(runingOrderKey);
                order.setOrderresult(ob.getObject().toString());
                orderHashMap.remove(runingOrderKey);
                orderHashMap.put(runingOrderKey, order);
                if (orderKeyVector.isEmpty()) {
                    submitOfflineDelAuthorResult();
                } else {
                    runingOrderKey = orderKeyVector.remove(0);
                    sendOrderCommand(runingOrderKey);
                }
                break;
        }
    }

}
