package com.work.xinlai.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class WifiUtils {
    /**
     * 如果static是写在单例中，高并发访问是会出问题的，
     * 这时候就要设置线程等待了，static是在容器加载的时候就已经加载到内存中，
     * 所以static方法和变量不宜过度使用，有选择的使用，
     **/
    private static String TAG = "WifiUtils";

    /**
     * 检查网络状态-wifi-移动数据
     **/
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            return connectivityManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    /**
     * 未打开wifi进入时自动获取android权限打开wifi，不进入wifi设置界面
     **/
    public static void openWifiWithPermission(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            /**wifi可用时不操作，不可用时打开**/
            //wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(true);
        }

    }

    /**
     * 判断wifi是否连接-wifi
     **/
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isAvailable()) {
            return true;
        }
        return false;
    }

    /**
     * 进入wifi设置界面
     **/
    public static boolean isEnterWifiSettingInterFace(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return true;
        }
        return false;
    }

    /**
     * 获取wifi列表
     **/
    public static List getWifiList(Context context) {
        List list = new ArrayList();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        /**扫描结果不为空**/
        if (isWifi(context)) {
            list = wifiManager.getScanResults();
        }
        return list;
    }

    private static boolean isWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.getScanResults() == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取wifi名字
     **/
    public static String getWifiSSID(Context context) {
        //获取wifi管理
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        //获取wifi实例
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Logger.e(TAG, wifiInfo.getSSID());
        Logger.e(TAG + "BSSID", wifiInfo.getBSSID());
        Logger.e(TAG + "MAC", wifiInfo.getMacAddress());
        Logger.e(TAG + "IP", String.valueOf(wifiInfo.getIpAddress()));
        Logger.e(TAG + "NETWORKID", String.valueOf(wifiInfo.getNetworkId()));
        return MyStringUtils.getWifi(wifiInfo.getSSID());

    }

    /**
     * 获取wifiIP
     **/
    public static String getWifiIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intToIp(wifiInfo.getIpAddress()).toString();
    }

    /**
     * 返回Ip按位与
     **/
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }
}
