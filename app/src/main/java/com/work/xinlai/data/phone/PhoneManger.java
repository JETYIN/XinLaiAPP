package com.work.xinlai.data.phone;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */
public class PhoneManger {

    //获取本机通讯
    public static List<PhoneInfo> getPhoneNum(Context context, Uri uri) {
        List<PhoneInfo> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        //根据uri获取手机通讯录
        //Cursor cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        //sim卡
        Uri simUri = Uri.parse("content://icc/adn");
        Cursor SIMcursor = contentResolver.query(simUri, null, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                /**获取名字和电话**/
                String User_Name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
                String User_Phone = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                if (TextUtils.isEmpty(User_Phone)) {
                    continue;
                }
                /**将数据封装**/
                PhoneInfo phoneInfo = new PhoneInfo(User_Name, User_Phone);
                list.add(phoneInfo);
            }
            //关闭查询组
            cursor.close();
        }
        if (null != SIMcursor) {
            while (SIMcursor.moveToNext()) {
                /**获取名字和电话**/
                String User_Name = SIMcursor.getString(SIMcursor.getColumnIndex("name"));
                String User_Phone = SIMcursor.getString(SIMcursor.getColumnIndex("number"));
                if (TextUtils.isEmpty(User_Phone)) {
                    continue;
                }
                /**将数据封装**/
                PhoneInfo phoneInfo = new PhoneInfo(User_Name, User_Phone);
                list.add(phoneInfo);
            }
            //关闭查询组
            SIMcursor.close();
        }

        return list;
    }

    /**
     * SIM卡中的号码content://icc/adn - content://sim/adn
     **/
    public static List<PhoneInfo> getPhoneNumSIM(Context context) {
        List<PhoneInfo> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        //根据uri获取手机通讯录
        //Cursor cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        Uri uri = Uri.parse("content://icc/adn");
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                /**获取名字和电话**/
                String User_Name = cursor.getString(cursor.getColumnIndex("name"));
                String User_Phone = cursor.getString(cursor.getColumnIndex("number"));
                if (TextUtils.isEmpty(User_Phone)) {
                    continue;
                }
                /**将数据封装**/
                PhoneInfo phoneInfo = new PhoneInfo(User_Name, User_Phone);
                list.add(phoneInfo);
            }
            //关闭查询组
            cursor.close();
        }

        return list;
    }
}
