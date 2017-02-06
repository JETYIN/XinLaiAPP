package com.work.xinlai.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.component.MlWebView;
import com.work.xinlai.component.TabFragment;
import com.work.xinlai.util.Utils;

/**
 * Created by Administrator on 2016/11/23.
 */
public class CompanyFragment extends TabFragment implements MlWebView.WebViewListener {

    @ViewInject(R.id.parent_fly)
    private FrameLayout mParentView;
    @ViewInject(R.id.content_wlmv)
    protected MlWebView mBrowser;
    private final static String URL = "http://www.scxinlai.com/";
    private boolean isDialogShow;


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_one;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mBrowser.addJavascriptInterface();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        /**fragment 中的注入**/
        ViewUtils.inject(this, view);
        showTitle("公司");
        mBrowser.setWebViewListener(this);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        mBrowser.loadUrl(URL);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBrowser.canGoBack()) {
            mBrowser.goBack();
        } else {
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
            //Utils.showProgressDialog(getActivity());
            isDialogShow = true;
        }
    }

    @Override
    public void onPageFinished(String url) {
        /**加载完成设置参数**/
        //Utils.hidePgressDialog();
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
