package com.work.xinlai.component.layout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.xinlai.R;

/**
 * Created by Administrator on 2016/12/19.
 * 线性item
 */

public abstract class LinearLayoutItem extends LinearLayout {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected View view;
    protected TextView titleTV;
    protected TextView contentTV;
    protected LinearLayout parentLL;
    protected View arrowView;


    public LinearLayoutItem(Context context) {
        this(context, null);
    }

    public LinearLayoutItem(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        setOrientation(VERTICAL);
        mLayoutInflater = LayoutInflater.from(context);
        view = mLayoutInflater.inflate(R.layout.compant_linitem, this, false);

        titleTV = (TextView) view.findViewById(R.id.tv_setting_title);
        contentTV = (TextView) view.findViewById(R.id.tv_setting_summary);
        parentLL = (LinearLayout) view.findViewById(R.id.ll_item);
        //动态设置的箭头或是其他图标
        arrowView = onCreateWidgetView((ViewGroup) view.findViewById(R.id.ll_setting_widget_frame));
        //为父布局设置监听
        view.setOnClickListener(onClickListener);
        //将view添加到布局viewgroup
        addView(view);

    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            OnLinItemClick();
        }
    };

    /**
     * 右侧箭头
     **/
    protected View onCreateWidgetView(ViewGroup root) {
        View widget = null;
        int resource = getWidgetLayoutResID();
        if (resource == 0) {
            root.setVisibility(GONE);
        } else {
            widget = LayoutInflater.from(getContext()).inflate(resource, root, false);
            root.addView(widget);
        }
        return widget;
    }

    protected void setTitleTV(int resource) {
        titleTV.setText(resource);
    }

    protected void setTitleTV(CharSequence charSequence) {
        titleTV.setText(charSequence);
    }

    protected void setContentTV(int resource) {
        contentTV.setText(resource);
    }

    protected void setContentTV(CharSequence charSequence) {
        contentTV.setText(charSequence);
    }

    /**
     * 定义抽象方法
     **/
    protected abstract int getWidgetLayoutResID();

    protected abstract void OnLinItemClick();

    /**
     * 设置背景
     **/
    protected void setLinItemBackground(String color) {
        parentLL.setBackgroundColor(Color.parseColor(color));
    }


}
