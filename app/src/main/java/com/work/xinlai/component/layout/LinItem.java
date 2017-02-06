package com.work.xinlai.component.layout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import com.work.xinlai.R;

/**
 * linItem = (IntentSetting) findViewById(R.id.setting_feedback);
 * linItem.setTitle("标题");
 * linItem.setClass(FeedbackActivity.class);
 * <p/>
 * Created by Administrator on 2016/12/19.
 */
public class LinItem extends LinearLayoutItem {
    private Intent mIntent;
    private nativeOperationListerner mListerner;

    public LinItem(Context context) {
        this(context, null);
    }

    public LinItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mIntent = new Intent();
    }

    /**
     * 设置要跳转的activity
     **/
    public void setClass(Class<?> clas) {
        mIntent.setClass(getContext(), clas);
    }

    public Intent getmIntent() {
        return mIntent;
    }

    /**
     * 动态设置右边指示图片
     **/
    @Override
    protected int getWidgetLayoutResID() {
        return R.layout.compant_linitem_item;
    }

    @Override
    protected void OnLinItemClick() {
        /**点击进行activity跳转**/
        if (mIntent.getComponent() != null) {
            getContext().startActivity(mIntent);
        }
        /**如果不需要进行跳转**/
        else if (mListerner != null) {
            mListerner.OnLinItemClick();
        }
    }

    public void rigisterOperationListerner(nativeOperationListerner listerner) {
        mListerner = listerner;

    }

    public void unrigisterOperationListerner() {
        mListerner = null;
    }

    public interface nativeOperationListerner {
        void OnLinItemClick();
    }
}
