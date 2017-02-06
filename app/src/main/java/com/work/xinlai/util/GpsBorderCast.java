package com.work.xinlai.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;

import com.work.xinlai.common.Constant;
import com.work.xinlai.data.db.DataHelper;


/**
 * Created by Administrator on 2016/11/24.
 */
public class GpsBorderCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取是否进入指定区域
        boolean isEnter = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
        if (isEnter) {
            //进入了gps临近范围，保存当前状态
            SharedPreferences sharedPreferences = DataHelper.getSharedPreferences(context);
            sharedPreferences.edit().putInt("GPS", 1).commit();

        } else {
            Constant.IS_DOMAIN = false;
        }
    }
}
