package com.qingwing.safekey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingwing.safekey.R;
import com.qingwing.safekey.okhttp3.response.OfflineDelAuthoryUserInfoResponse;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DelAuthoryAdapter extends BaseAdapter {

    private Context context;
    private List<OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo> data = new ArrayList<>();

    public DelAuthoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo data) {
        for (OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo aui : this.data) {
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

    public List<OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo> getData() {
        return this.data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo getItem(int position) {
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
        OfflineDelAuthoryUserInfoResponse.DelAuthoryUserInfo aui = data.get(position);
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
