package com.work.xinlai.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.work.xinlai.component.ExitDialog;

/**
 * Created by Administrator on 2016/11/23.
 */
public class GpsUtils {
    private static final String ACTION_GPS = "JETYIN";
    /**J 104.05185,W 30.555354**/

    /**
     * gps是否开启,ACTION_LOCATION_SOURCE_SETTINGS设置界面
     **/
    public static boolean isGPSOpened(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;

        } else {
            return false;
        }
    }
    public static void showGpsNotOpenDialog(final Context context){
        new ExitDialog(context).setTitleLd("如需定位更加精准，请打开gps") .setNegativeButton("不在提示", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Settings.getInstance(context).setGpsHint(false);
            }
        }).setPositiveButton("现在打开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).show();

    }

    /**
     * gps发送临近警告广播
     **/
    public static void sendGPSBorderCast(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //定义经纬度-百度104.05185,30.555354
        double longitude = 104.05185;
        double latitude = 30.555354;
        //定义半径
        float radius = 1000;
        //定义intent事件
        Intent intent = new Intent(context, GpsBorderCast.class);
        intent.setAction(ACTION_GPS);
        //将intent包装成paddingIntent，事件延迟触发
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -1, intent, 0);
        //添加临近警告
        locationManager.addProximityAlert(latitude, longitude, radius, -1, pendingIntent);
    }

    /**
     * 返回位置信息s
     **/
    public static Location getLocation(Context context) {
        Location location;
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String previder = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(previder);
        /**
         * //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
         //参数2，位置信息更新周期，单位毫秒
         //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
         //参数4，监听
         //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

         // 1秒更新一次，或最小位移变化超过1米更新一次；
         //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
         * **/

       /* locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 8, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //gps定位发生改变时
                updateView(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
//当gps locationprivader可用时，更新位置


                updateView(locationManager.getLastKnownLocation(provider));
            }

            @Override
            public void onProviderDisabled(String provider) {
                updateView(null);
            }
        });*/
        return location;
    }
}
