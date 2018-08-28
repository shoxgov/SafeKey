package com.qingwing.safekey.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.bean.OfflineDelAuthoryRoomBack;
import com.qingwing.safekey.dialog.AffirmDialog;
import com.qingwing.safekey.imp.DialogCallBack;
import com.qingwing.safekey.imp.TitleBarListener;
import com.qingwing.safekey.okhttp3.http.HttpCallback;
import com.qingwing.safekey.okhttp3.http.OkHttpUtils;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.okhttp3.response.OfflineAuthoryUserInfoResponse;
import com.qingwing.safekey.okhttp3.response.OfflineDelAuthoryUserInfoResponse;
import com.qingwing.safekey.okhttp3.response.OrderResponse;
import com.qingwing.safekey.utils.SerializationDefine;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-08-14.
 * email:shoxgov@126.com
 */

public class OfflineDeleteAuthorizationActivity extends BaseActivity {
    @Bind(R.id.offline_del_settle_rg)
    RadioGroup settleRg;
    @Bind(R.id.offline_del_search_edit)
    EditText searchEdit;
    @Bind(R.id.offline_del_result_edit)
    EditText resultEdit;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 100;
    private String roomid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_del_authory);
        ButterKnife.bind(this);
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
    private void addAffirmDialog(OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo person) {
        final String info = "姓名：" + person.getPersonname() + "    学号：" + person.getPersoncode();
        AffirmDialog warnDialog = new AffirmDialog(OfflineDeleteAuthorizationActivity.this, info, "确认添加", "暂不添加", new DialogCallBack() {
            @Override
            public void OkDown(Object obj) {
                String content = resultEdit.getText().toString();
                content += info;
                content += "\n";
                resultEdit.setText(content);
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
    /**
     * roomback	是	取消授权信息数组
     * roombacktype	是	取消授权类型（1（密码）,2（指纹）,3（卡片））
     * roombackid	否	授权id
     */
    private void obtainOfflineDelAuthorCommand() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("roomid", "1");
        List<OfflineDelAuthoryRoomBack> roomback = new ArrayList<>();
        OfflineDelAuthoryRoomBack rb = new OfflineDelAuthoryRoomBack();
        rb.setRoombacktype(1);
        rb.setRoombackid("11");
        roomback.add(rb);
        params.put("roomback", SerializationDefine.List2Str(roomback));
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_DEL_AUTHORY, params, OrderResponse.class, new HttpCallback() {
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

    private void submitOfflineDelAuthorResult() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SKApplication.loginToken);
        params.put("roomid", "1");
        OrderResponse.OrderInfo order;
//        params.put("orderresult", order.toString());
        OkHttpUtils.postAsyn(NetWorkConfig.OFFLINE_DEL_AUTHORY_SAVE_RESULT, params, BaseResponse.class, new HttpCallback() {
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

    @OnClick(R.id.offline_del_send)
    public void onViewClicked() {
    }
}
