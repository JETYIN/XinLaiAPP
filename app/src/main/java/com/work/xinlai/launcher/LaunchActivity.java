package com.work.xinlai.launcher;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.util.GpsUtils;
import com.work.xinlai.util.Utils;

import cn.jpush.android.api.InstrumentedActivity;


/**
 * Created by Administrator on 2016/11/23.
 */
public class LaunchActivity extends InstrumentedActivity {
    private static final int MSG_JUMP = 0;
    @ViewInject(R.id.tv_version)
    private TextView versionTV;
    private PackageInfo mPackgeInfo;

   /* JPushInterface.stopPush(getApplicationContext());
    JPushInterface.resumePush(getApplicationContext());
    JPushInterface.getRegistrationID(getApplicationContext());*/
    /**
     * 权限验证
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_spalsh);
        ViewUtils.inject(this);
        showSplash();
        /**定位广播**/
        GpsUtils.sendGPSBorderCast(this);
      /*  Toast.makeText(this,"经度:"+
                String.valueOf(GpsUtils.getLocation(this).getLongitude())
                +"/"+"纬度"+String.valueOf(GpsUtils.getLocation(this).getLatitude()),Toast.LENGTH_LONG).show();*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return true;
    }
    /**版本比较**/
    /**
     * 版本信息,获取manifest中设置的版本号
     **/
    private void showSplash() {
        mPackgeInfo = Utils.getPackageInfo(this);
        if (mPackgeInfo != null) {
            versionTV.setText(getString(R.string.xl_version_desc, mPackgeInfo.versionName));
            new Thread(new Jump()).start();
        }
    }

    /**
     * 验证是否登录，登录后页面结束进入主页面，未登录进入登录界面
     **/
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_JUMP:
                    /**如果登录，直接进入主界面，未登录进入登录**/
                    LaunchActivity.this.finish();
                    Intent intent = new Intent(LaunchActivity.this, LoginRegisterActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private class Jump implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = MSG_JUMP;
            mHandler.sendMessage(message);
        }
    }
}
