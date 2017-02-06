package com.work.xinlai.launcher;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.component.MlWebView;
import com.work.xinlai.util.Utils;

/**
 * Created by Administrator on 2016/11/29.
 */
public class AboutCompanyActivity extends BaseActivity implements MlWebView.WebViewListener {
    @ViewInject(R.id.parent_fl)
    private FrameLayout mParentView;
    @ViewInject(R.id.content_wv)
    protected MlWebView mBrowser;
    private final static String URL = "http://www.scxinlai.com/";
    private boolean isDialogShow;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_aboutcompanyactivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle("公司");
        mBrowser.setWebViewListener(this);
        mBrowser.loadUrl(URL);
    }

    public void refresh() {
        mBrowser.reload();
    }

    @Override
    public void onBackPressed() {
        if (mBrowser.canGoBack()) {
            mBrowser.goBack();
        } else {
            super.onBackPressed();
            mBrowser.setWebViewListener(null);
        }
    }

    /**
     * 重写方法
     **/
    @Override
    public boolean shouldOverrideUrlLoading(String url) {
        return false;
    }

    @Override
    public void onPageStarted(String url) {
        if (!isDialogShow) {
            Utils.showProgressDialog(this);
            isDialogShow = true;
        }
    }

    @Override
    public void onPageFinished(String url) {
        Utils.hidePgressDialog();
        updateTitle();
    }

    /**
     * 网络错误,动态添加错误提示信息
     **/
    @Override
    public void onPageReceivedError() {

    }

    /**
     * 显示title-获取网页端的文字title
     **/
    private void updateTitle() {
        showTitle(mBrowser.getTitle());
    }
}
