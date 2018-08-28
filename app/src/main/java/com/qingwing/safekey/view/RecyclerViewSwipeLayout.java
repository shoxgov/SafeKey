package com.qingwing.safekey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingwing.safekey.R;
import com.qingwing.safekey.adapter.CustomBaseQuickAdapter;
import com.qingwing.safekey.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewSwipeLayout<T> extends RelativeLayout {

    private int dividerDrawable;
    private float dividerHeight;
    private int dividerColor;
    private int itemLayout;
    private boolean isFooterFresh;
    private boolean isHeaderFresh;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomBaseQuickAdapter adapter;
    private LinearLayout noDataView;
    private RecycleViewDivider recycleViewDivider;
    private DividerItemDecoration dividerItemDecoration;
    private ImageView noDataImg;
    private TextView noDataHint;

    public RecyclerViewSwipeLayout(Context context) {
        super(context);
    }

    public RecyclerViewSwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//要用this 不然不会跑到下面的构造函数
    }

    public RecyclerViewSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RecyclerView);
        isFooterFresh = a.getBoolean(R.styleable.RecyclerView_swipeFooterFresh,
                true);
        isHeaderFresh = a.getBoolean(R.styleable.RecyclerView_swipeHeaderFresh,
                false);
        dividerColor = a.getColor(R.styleable.RecyclerView_dividerColor, -100);
        dividerHeight = a.getDimension(R.styleable.RecyclerView_dividerHeight, 2);
        dividerDrawable = a.getResourceId(R.styleable.RecyclerView_dividerDrawable, -100);
        itemLayout = a.getResourceId(R.styleable.RecyclerView_itemlayout, -11);
        a.recycle();
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(Context context) {
        LogUtil.d("RecyclerViewSwipeLayout init");
        inflate(context, R.layout.recyclerview_layout, this);
        setId(R.id.recyclerRefreshLayout);
        // 取到布局中的控件
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        noDataView = (LinearLayout) findViewById(R.id.searchbar_nodata);
        noDataImg = (ImageView) findViewById(R.id.searchbar_nodata_img);
        noDataHint = (TextView) findViewById(R.id.searchbar_nodata_txt);
        swipeRefreshLayout.setEnabled(isHeaderFresh);
        //////////////////////////
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (dividerColor != -100) {
            recycleViewDivider = new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, (int) dividerHeight, dividerColor);
            recyclerView.addItemDecoration(recycleViewDivider);
        } else if (dividerDrawable != -100) {
            recycleViewDivider = new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, dividerDrawable);//此处画的横线是横向的线，
            recyclerView.addItemDecoration(recycleViewDivider);
        } else {
            dividerItemDecoration = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);//分割线 原生的VERTICAL就是画的横线“-”， 反之为坚线“｜”
        }
        //////////////
        /////////////recyclerView 配置
        if (itemLayout != -11) {
            adapter = new CustomBaseQuickAdapter(itemLayout, null);//itemLayout);
            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            adapter.isFirstOnly(false);
            adapter.setAdapter(recyclerView);
        }
    }

    public void createAdapter(int itemLayout, boolean isAnimation) {
        this.itemLayout = itemLayout;
        adapter = new CustomBaseQuickAdapter(itemLayout, null);//itemLayout);
        if (isAnimation) {
            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        }
        adapter.isFirstOnly(false);
        adapter.setAdapter(recyclerView);
    }

    public void createAdapter(int itemLayout) {
        this.createAdapter(itemLayout, true);
    }

    public void setDividerColor(Context context, int dividerColor, int dividerHeight) {
        this.dividerColor = dividerColor;
        this.dividerHeight = dividerHeight;
        if (recycleViewDivider != null) {
            recyclerView.removeItemDecoration(recycleViewDivider);
        }
        if (dividerItemDecoration != null) {
            recyclerView.removeItemDecoration(dividerItemDecoration);
        }
        recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, (int) dividerHeight, dividerColor));
    }

    public void setDividerDrawable(Context context, int dividerDrawable) {
        this.dividerDrawable = dividerDrawable;
        if (recycleViewDivider != null) {
            recyclerView.removeItemDecoration(recycleViewDivider);
        }
        if (dividerItemDecoration != null) {
            recyclerView.removeItemDecoration(dividerItemDecoration);
        }
        recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, dividerDrawable));
    }

    public void setDividerDashline(Context context, int dividerColor, int dividerHeight) {
        this.dividerColor = dividerColor;
        this.dividerHeight = dividerHeight;
        if (recycleViewDivider != null) {
            recyclerView.removeItemDecoration(recycleViewDivider);
        }
        if (dividerItemDecoration != null) {
            recyclerView.removeItemDecoration(dividerItemDecoration);
        }
        //设置分割线
        recyclerView.addItemDecoration(new DashlineItemDivider(dividerColor, dividerHeight));
    }

    public ViewGroup getBaseParent() {
        return (ViewGroup) recyclerView.getParent();
    }

    public void addHeaderView(View header) {
        adapter.addHeaderView(header);
    }

    public void addHeaderView(View header, int index) {
        adapter.addHeaderView(header, index);
    }

    public void addFooterView(View footer) {
        adapter.addFooterView(footer);
    }

    public void addFooterView(View footer, int index) {
        adapter.addFooterView(footer, index);
    }

    //动画效果类别
    public void openLoadAnimation(int scalein) {
        adapter.openLoadAnimation(scalein);
    }

    //设置页数量
    public void openLoadMore(int pageSize) {
        adapter.openLoadMore(pageSize);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public void notifyItemChanged(int poisition) {
        adapter.notifyItemChanged(poisition);
    }

    public void notifyItemRemoved(int poisition) {
        adapter.notifyItemRemoved(poisition);
    }

    public void notifyItemChanged(int poisition, Object obj) {
        adapter.notifyItemChanged(poisition, obj);
    }

    public void remove(int poisition) {
        adapter.remove(poisition);
    }

    public Object getItem(int position) {
        return adapter.getItem(position);
    }

    public int getItemCount() {
        return adapter.getItemCount();
    }

    //本次数据加载结束并且还有下页数据
    public void loadComplete() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.loadComplete();
            }
        }, 500);
    }

    public void setRefreshing(boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    //下拉拉加载更多监听
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener swipeRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener);
    }

    //上拉加载更多监听
    public void setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener) {
        adapter.setOnLoadMoreListener(quickAdapterListener);
    }

    public void setEmpty() {
        noDataView.setVisibility(View.VISIBLE);
    }

    public void setEmpty(int imgResId, String hint) {
        noDataView.setVisibility(View.VISIBLE);
        noDataImg.setImageResource(imgResId);
        noDataHint.setText(hint);
    }

    public void setEmptyView(View view) {
        adapter.setEmptyView(view);
    }

    //自定义Itemd页面回调
    public void setXCallBack(CustomBaseQuickAdapter.QuickXCallBack callBack) {
        adapter.setXCallBack(callBack);
    }

    public void addData(List<T> data) {
        noDataView.setVisibility(View.GONE);
        adapter.addData(data);
    }

    public List<T> getData() {
        return adapter.getData();
    }

    public void setNewData(List<T> data) {
        noDataView.setVisibility(View.GONE);
        adapter.setNewData(data);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener);
    }
}
