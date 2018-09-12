package com.qingwing.safekey.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qingwing.safekey.R;

import java.util.List;

public class ListViewDialog extends Dialog {

    private AdapterView.OnItemClickListener listener;
    private String title;
    private Context mContext;
    private ListView mListView;
    private TextView mTitle;
    private ArrayAdapter<String> stringArrayAdapter;

    public ListViewDialog(Context context, String title, AdapterView.OnItemClickListener listener) {
        super(context);
        this.listener = listener;
        mContext = context;
        this.title = title;
        initView();
        initListView();
    }

    private void initView() {
        View contentView = View.inflate(mContext, R.layout.spinner_window_layout, null);
        mListView = (ListView) contentView.findViewById(R.id.listview);
        mTitle = (TextView) contentView.findViewById(R.id.title);
        setContentView(contentView);
        mListView.setOnItemClickListener(listener);
        mTitle.setText(title);
    }

    private void initListView() {
        stringArrayAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, R.id.tv_content);
        mListView.setAdapter(stringArrayAdapter);

    }

    public void setData(List<String> data) {
        stringArrayAdapter.clear();
        stringArrayAdapter.addAll(data);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        setHeight();
    }

    private void setHeight() {
        Window window = getWindow();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (displayMetrics.widthPixels * 0.7);
        if (window.getDecorView().getHeight() >= (int) (displayMetrics.heightPixels * 0.8)) {
            attributes.height = (int) (displayMetrics.heightPixels * 0.8);
        }
        window.setAttributes(attributes);
    }
}