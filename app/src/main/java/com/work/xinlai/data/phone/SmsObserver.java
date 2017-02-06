package com.work.xinlai.data.phone;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.work.xinlai.util.Logger;
import com.work.xinlai.launcher.RegisterActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/9.观察者莫斯-内容观察者
 */
public class SmsObserver extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    private Context mContext;
    private Handler mHandler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
    }

    /**
     * 状态改变时调用-接收短信
     **/
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Logger.e("SMS_LOADING", "sms has changed");
        Logger.e("SMS_URI", uri.toString());

        if (uri.toString().equals("content://sms/raw")) {
            return;
        }

        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor cursor = mContext.getContentResolver().query(inboxUri, null,
                null, null, "date desc");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String address = cursor.getString(cursor
                        .getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                Logger.e("SMS_USER", "发件人为：" + address + " " + "短信内容为：" + body);


                Pattern pattern = Pattern.compile("(\\d{4})");//正则表达式-4位验证码
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    String code = matcher.group(0);
                    Logger.e("SMS_CODE", "code is" + code);

                    mHandler.obtainMessage(RegisterActivity.SMS_CODE, code)
                            .sendToTarget();
                }
            }
        }
    }
}
