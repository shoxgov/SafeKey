package com.qingwing.safekey.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtils {
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		try {
			// 获取ListView对应的Adapter
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				return;
			}

			int totalHeight = 0;
			for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
				View listItem = listAdapter.getView(i, null, listView);
//				int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
//				listItem.measure(desiredWidth, 0);
				listItem.measure(0, 0); // 计算子项View 的宽高
				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			// listView.getDividerHeight()获取子项间分隔符占用的高度
			// params.height最后得到整个ListView完整显示需要的高度
			listView.setLayoutParams(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 计算GridView宽高
	 * @param gridView
	 */
	public static void calGridViewWidthAndHeigh(GridView gridView) {

		// 获取GridView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int numColumns = gridView.getNumColumns();
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(0, 0); // 计算子项View 的宽高

			if ((i+1)%numColumns == 0) {
				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			}

			if ((i+1) == len && (i+1)%numColumns != 0) {
				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			}
		}

		totalHeight += 40;

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		gridView.setLayoutParams(params);
	}
}
