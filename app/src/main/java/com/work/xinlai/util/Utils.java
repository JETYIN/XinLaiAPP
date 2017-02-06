package com.work.xinlai.util;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.work.xinlai.component.ProgressDialog;
import com.work.xinlai.launcher.XinlaiApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/23.
 */
public class Utils {
    private static final String TAG = "Utils";
    private static volatile PackageInfo sPackageInfo;
    private static ProgressDialog mProgressDialog;
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";


    /**
     * 返回TAG
     **/
    public static String getDebugTAG(Class cla) {
        return cla.getSimpleName();
    }

    /**
     * 获取版本号
     **/
    public static PackageInfo getPackageInfo(Context context) {
        context = context == null ? XinlaiApplication.getAppContext() : context;
        if (sPackageInfo == null) {
            String pkgName = context.getPackageName();
            try {
                sPackageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return sPackageInfo;
    }

    /**
     * 显示
     **/
    public static void showProgressDialog(Context context) {
        if (context == null) {
            return;
        }
        mProgressDialog = new ProgressDialog(context);
        //mProgressDialog.setDialogContent(mResId);
        mProgressDialog.show();
    }

    /**
     * 隐藏
     **/
    public static void hidePgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * mVibrator.vibrate(1000);//振动提醒已到设定位置附近
     **/

    public static Vibrator getVibrator() {
        Vibrator mVibrator = (Vibrator) XinlaiApplication.getAppContext()
                .getSystemService(Service.VIBRATOR_SERVICE);
        return mVibrator;
    }

    /**
     * 6.0权限
     **/
    @TargetApi(23)
    public static void getPersimmions() {

    }

    /**
     * 极光推送检查appkey
     **/
// 取得AppKey
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appKey;
    }

    /**
     * 获取手机的imei编号
     **/
    public static String getImei(Context context, String imei) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.e(Utils.class.getSimpleName(), e.getMessage());
        }
        return imei;
    }

    /**
     * 获取手机deviceid
     **/
    public static String getDeviceId(Context context) {
        String deviceId = JPushInterface.getUdid(context);
        return deviceId;
    }

    /**
     * 时间选择器
     **/
    public static void datePick(Context context, final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                textView.setText(new StringBuilder()
                        .append(year)
                        .append("-")
                        .append((month + 1) < 10 ? "0" + (month + 1)
                                : (month + 1)).append("-")
                        .append((day < 10) ? "0" + day : day));
            }
        }, year, monthOfYear, dayOfMonth).show();
    }

    /**
     * 进入设置时间
     **/
    public static void setDataPickTime(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        textView.setText(new StringBuilder()
                .append(year)
                .append("-")
                .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
                        : (monthOfYear + 1)).append("-")
                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));

    }

    /**
     * 隐藏软键盘
     **/
    public static void hideInput(EditText etView) {
        if (etView != null) {
            InputMethodManager imm = (InputMethodManager) etView.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etView.getWindowToken(), 0);
        }
    }

    /**
     * 转码
     **/
    public static String urlDecode(String src) {
        if (!TextUtils.isEmpty(src)) {
            try {
                return URLDecoder.decode(src, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Logger.e(TAG, e.getMessage());
            } catch (IllegalArgumentException e) {
                Logger.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 登录转码
     **/
    public static String encodeTxt(String string) {
        if (!TextUtils.isEmpty(string)) {
            try {
                return URLEncoder.encode(string, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 上传图片获取图片的长宽
     **/
    public static int[] getImageSize(String pathName) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opts);
        return new int[]{opts.outWidth, opts.outHeight};
    }

    /**
     * convert dp to px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
