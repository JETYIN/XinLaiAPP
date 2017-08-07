package com.work.xinlai.launcher;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.work.xinlai.baidu.LocationService;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/23.
 */
public class XinlaiApplication extends Application {
    private static final String TAG = "JPush";
    /**
     * 获取全局上下文
     **/
    /**
     * 百度定位服务
     **/
    public LocationService locationService;
    public static Context mContext;
    private static XinlaiApplication app = null;
    /**
     * 当前进程名称
     **/
    private String mCurentProcessName;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mContext = getApplicationContext();
        locationService = new LocationService(mContext);
        //如果当前进程还存在并未被杀掉，那么就不在调用三方插件初始化
        if (!quickStart()) {
            init();
        }
    }

    /**
     * 百度等sdk实例化
     **/
    private void init() {
        SDKInitializer.initialize(getApplicationContext());
        //实例化极光推送
        JPushInterface.init(this);
        //设置开启debug模式，发布的时候需要关闭
        JPushInterface.setDebugMode(true);

    }

    public static Context getAppContext() {
        return mContext;
    }

    /**
     * 获取当前进程名称
     **/
    public boolean quickStart() {
        if (TextUtils.isEmpty(mCurentProcessName)) {
            mCurentProcessName = getCurProcessName(this);
        }
        if (contains(mCurentProcessName, ":mini")) {
            Log.d("loadDex", ":mini start!");
            return true;
        }
        return false;
    }

    public boolean contains(String str, String searchStr) {
        return str != null && searchStr != null ? str.indexOf(searchStr) >= 0 : false;
    }

    /**
     * 进程名称
     **/
    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * 结束进程
     **/
    public void exit() {

        System.exit(0);
    }

    public static XinlaiApplication getApp() {
        return app;
    }
}
