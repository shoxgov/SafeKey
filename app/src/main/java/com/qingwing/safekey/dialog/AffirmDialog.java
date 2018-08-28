package com.qingwing.safekey.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;
import com.qingwing.safekey.imp.DialogCallBack;

public class AffirmDialog extends Dialog implements OnClickListener {

    private Context context;
    private DialogCallBack dialogcallback;
    private String warn;
    private String ok;
    private String cancle;

    public AffirmDialog(Context context, String warn, DialogCallBack dialogcallback) {
        super(context, R.style.CustomDialog_warn);
        this.context = context;
        this.warn = warn;
        this.dialogcallback = dialogcallback;
    }

    public AffirmDialog(Context context, String warn, String ok, String cancle, DialogCallBack dialogcallback) {
        super(context, R.style.CustomDialog_warn);
        this.context = context;
        this.warn = warn;
        this.ok = ok;
        this.cancle = cancle;
        this.dialogcallback = dialogcallback;
    }

    public AffirmDialog(Context context, int themeId) {
        super(context, themeId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_affirm);
        setCanceledOnTouchOutside(false);
        TextView warnInfoText = (TextView) findViewById(R.id.dialog_warn_info);
        Button okBtn = (Button) findViewById(R.id.agreeBtn);
        Button cancleBtn = (Button) findViewById(R.id.refuseBtn);
        warnInfoText.setText(warn);
        if (!TextUtils.isEmpty(ok)) {
            okBtn.setText(ok);
        }
        if (!TextUtils.isEmpty(cancle)) {
            cancleBtn.setText(cancle);
        }
        okBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        int width = (int) (SKApplication.screenWidthPixels * 0.9);
        getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agreeBtn:
                if (dialogcallback != null) {
                    dialogcallback.OkDown("");
                }
                break;
            case R.id.refuseBtn:
                break;
        }
        dismiss();
    }

}
