package com.qingwing.safekey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingwing.safekey.R;
import com.qingwing.safekey.okhttp3.response.OfflineAuthoryUserInfoResponse;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class AuthoryAdapter extends BaseAdapter {

    private Context context;
    private List<OfflineAuthoryUserInfoResponse.AuthoryUserInfo> data = new ArrayList<>();

    public AuthoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<OfflineAuthoryUserInfoResponse.AuthoryUserInfo> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(OfflineAuthoryUserInfoResponse.AuthoryUserInfo data) {
        for (OfflineAuthoryUserInfoResponse.AuthoryUserInfo aui : this.data) {
            if (aui.getPersoncode().equals(data.getPersoncode())) {
                ToastUtil.showText("重复添加");
                return;
            }
        }
        this.data.add(data);
        notifyDataSetChanged();
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public List<OfflineAuthoryUserInfoResponse.AuthoryUserInfo> getData() {
        return this.data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public OfflineAuthoryUserInfoResponse.AuthoryUserInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_authory_item, null, false);
            viewHolder.info = (TextView) convertView.findViewById(R.id.item_info);
            viewHolder.del = (TextView) convertView.findViewById(R.id.item_del);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OfflineAuthoryUserInfoResponse.AuthoryUserInfo aui = data.get(position);
        viewHolder.info.setText("姓名：" + aui.getPersonname() + "    学号：" + aui.getPersoncode());
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                data.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView del;
        public TextView info;
    }


}
