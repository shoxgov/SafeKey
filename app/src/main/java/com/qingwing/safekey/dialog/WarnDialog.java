package com.qingwing.safekey.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.qingwing.safekey.R;
import com.qingwing.safekey.SKApplication;

public class WarnDialog extends Dialog {


    private TextView infos;

    public WarnDialog(Context context) {
        this(context, R.style.CustomDialog_discovery);
    }

    public WarnDialog(Context context, int themeId) {
        super(context, themeId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_warn);
        infos = (TextView) findViewById(R.id.infos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int width = (int) (SKApplication.screenWidthPixels * 0.8);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
    }

    public void show(String infos) {
        this.infos.setText(infos);
        show();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
