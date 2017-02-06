package com.work.xinlai.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.work.xinlai.common.Constant;
import com.work.xinlai.util.WifiUtils;

/**
 * Created by Administrator on 2016/11/29.
 */
public class MlWebView extends WebView {
    private WebViewListener mListener;
    private String cacheDirPath;

    /**
     * 、缓存构成
     * /data/data/package_name/cache/
     * /data/data/package_name/database/webview.db
     * /data/data/package_name/database/webviewCache.db
     * getCacheDir()方法用于获取/data/data/<application package>/cache目录
     * getFilesDir()方法用于获取/data/data/<application package>/files目录
     * 这两个方法获取的都是在app安装文件包中的文件
     **/
    public MlWebView(Context context) {
        this(context, null);
    }

    //判断是否有网络，有的话，使用LOAD_DEFAULT，无网络时，使用LOAD_CACHE_ELSE_NETWORK。
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    public MlWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        //设置是否支持缩放
        webSettings.setSupportZoom(true);
        /*//设置是否出现缩放工具
        webSettings.setBuiltInZoomControls(true);*/
        /**设置缓存**/
        setCachMode(webSettings, context);

        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        //路径
        cacheDirPath = context.getFilesDir().getAbsolutePath() + Constant.WEBVIEW_CACHE_NAME;
        //设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        /**让网页跳转在app内部执行**/
        setWebViewClient(new MlWebViewClient());
    }

    /**
     * 根据网络是否连接设置连接模式
     **/
    private void setCachMode(WebSettings webSettings, Context context) {
        if (WifiUtils.isNetConnected(context)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    public void setWebViewListener(WebViewListener listener) {

        mListener = listener;
    }

    private class MlWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return mListener == null ? false : mListener.shouldOverrideUrlLoading(url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (mListener != null) {
                mListener.onPageStarted(url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mListener != null) {
                mListener.onPageFinished(url);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (mListener != null) {
                mListener.onPageReceivedError();
            }
        }
    }

    ;

    public interface WebViewListener {
        boolean shouldOverrideUrlLoading(String url);

        void onPageStarted(String url);

        void onPageFinished(String url);

        void onPageReceivedError();
    }
}
