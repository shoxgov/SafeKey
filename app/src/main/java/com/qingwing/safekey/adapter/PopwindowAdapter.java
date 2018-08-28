package com.qingwing.safekey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingwing.safekey.R;

import java.util.List;


public class PopwindowAdapter extends BaseAdapter {

    private List<String> countryGridCode;
    private Context context;

    public PopwindowAdapter(List<String> data, Context context) {
        this.countryGridCode = data;
        this.context = context;
    }

    public PopwindowAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> data) {
        this.countryGridCode = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return countryGridCode == null ? 0 : countryGridCode.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        /*  缓存子布局文件中的控件对象*/
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_popwindow_item, null, false);
            holder = new ViewHolder();
            holder.tvCountryGridCode = (TextView) view.findViewById(R.id.item_name);
            view.setTag(holder);
        }
        //缓存已滑入ListView中的item view
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tvCountryGridCode.setText(countryGridCode.get(position));
        return view;
    }

    class ViewHolder {
        TextView tvCountryGridCode;
    }
}