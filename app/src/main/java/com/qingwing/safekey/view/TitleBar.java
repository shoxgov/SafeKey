package com.qingwing.safekey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingwing.safekey.R;
import com.qingwing.safekey.imp.TitleBarListener;

public class TitleBar extends RelativeLayout implements View.OnClickListener {

    private Drawable leftBtnBackground;
    private Drawable rightBtnBackground;
    private int titleTextColor;
    private int leftTextColor;
    private int rightTextColor;
    private int titleBackgroundColor;
    private String titleName;
    private String leftText;
    private String rightText;
    private boolean showLeftBtn;
    private boolean showRightBtn;
    private TitleBarListener listener = null;
    private TextView title;
    private TextView leftButton;
    private TextView rightButton;
    private RelativeLayout background;

    public void setTitleBarListener(TitleBarListener listener) {
        this.listener = listener;
    }

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//要用this 不然不会跑到下面的构造函数
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TitleBar);
        showLeftBtn = a.getBoolean(R.styleable.TitleBar_showLeftBtn,
                true);
        showRightBtn = a.getBoolean(R.styleable.TitleBar_showRightBtn,
                false);
        leftText = a.getString(R.styleable.TitleBar_leftBtnText);
        rightText = a.getString(R.styleable.TitleBar_rightBtnText);
        leftTextColor = a.getColor(R.styleable.TitleBar_leftBtnTextcolor, context.getResources().getColor(R.color.textblack));
        rightTextColor = a.getColor(R.styleable.TitleBar_rightBtnTextcolor, context.getResources().getColor(R.color.white));
        titleTextColor = a.getColor(R.styleable.TitleBar_titleTextColor, context.getResources().getColor(R.color.textblack));
        titleBackgroundColor = a.getColor(R.styleable.TitleBar_titleBackground, context.getResources().getColor(R.color.white));
        titleName = a.getString(R.styleable.TitleBar_titleName);
        leftBtnBackground = a.getDrawable(R.styleable.TitleBar_leftBtnBackground);
        rightBtnBackground = a.getDrawable(R.styleable.TitleBar_rightBtnBackground);
        a.recycle();
        init(context);
    }

    public void setTitleName(String titleName) {
        title.setText(titleName);
    }

    public void setRightVisible(boolean isVisible, String title){
        rightText = title;
        rightButton.setText(rightText);
        setRightVisible(isVisible);
    }
    public void setRightVisible(boolean isVisible){
        showRightBtn = isVisible;
        if(isVisible) {
            rightButton.setVisibility(VISIBLE);
        }else{
            rightButton.setVisibility(INVISIBLE);
        }
    }

    public void setLeftVisible(boolean isVisible){
        showLeftBtn = isVisible;
        if(isVisible) {
            leftButton.setVisibility(VISIBLE);
        }else{
            leftButton.setVisibility(INVISIBLE);
        }
    }

    public void setRightBtnBackgroundResource(int drawableId){
//        rightButton.setBackgroundResource(drawableId);
        rightButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(drawableId), null, null, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(Context context) {
        inflate(context, R.layout.titlebar, this);
        setId(R.id.titlelayout);
        // 取到布局中的控件
        background = (RelativeLayout) findViewById(R.id.titlelayoutbackground);
        title = (TextView) findViewById(R.id.title_name);
        leftButton = (TextView) findViewById(R.id.title_left_img);
        rightButton = (TextView) findViewById(R.id.title_right_img);
        if (!TextUtils.isEmpty(titleName)) {
            title.setText(titleName);
        }
        if (showLeftBtn) {
            leftButton.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(leftText)) {
                leftButton.setText(leftText);
            }
            if (leftBtnBackground != null) {
                leftButton.setCompoundDrawablesWithIntrinsicBounds(leftBtnBackground, null, null, null);
            }
            leftButton.setTextColor(leftTextColor);
        } else {
            leftButton.setVisibility(View.GONE);
        }
        if (showRightBtn) {
            rightButton.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(rightText)) {
                rightButton.setText(rightText);
            }
            if (rightBtnBackground != null) {
                rightButton.setCompoundDrawablesWithIntrinsicBounds(rightBtnBackground, null, null, null);
            }
            rightButton.setTextColor(rightTextColor);
        } else {
            rightButton.setVisibility(View.GONE);
        }
        if (titleBackgroundColor != -1) {
            background.setBackgroundColor(titleBackgroundColor);
        }
//        if (titleTextColor != -1) {
        title.setTextColor(titleTextColor);
//        }
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_img:
                if (listener != null) {
                    listener.leftClick();
                }
                break;

            case R.id.title_right_img:
                if (listener != null) {
                    listener.rightClick();
                }
                break;

        }
    }
}
