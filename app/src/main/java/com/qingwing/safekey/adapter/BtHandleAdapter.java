package com.qingwing.safekey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingwing.safekey.R;
import com.qingwing.safekey.bean.BtHandleBean;

import java.util.List;


public class BtHandleAdapter extends BaseAdapter {

    private Context context;
    private List<BtHandleBean> data;

    public BtHandleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<BtHandleBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public BtHandleBean getItem(int position) {
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
                    R.layout.list_bt_handle_item, null, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.bt_handle_img);
            viewHolder.title = (TextView) convertView.findViewById(R.id.bt_handle_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final BtHandleBean si = data.get(position);
        viewHolder.title.setText(si.getTitle());
        viewHolder.icon.setImageResource(si.getIcon());
        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public ImageView icon;
    }


}
