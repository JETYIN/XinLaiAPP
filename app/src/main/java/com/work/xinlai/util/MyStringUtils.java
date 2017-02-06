package com.work.xinlai.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/28.
 */
public class MyStringUtils {
    //TIMER 9:30
    public static boolean compareTime(String TIMER, int TYPE) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long hour = -1, minute = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            //设置打卡时间
            Date StandardTimer = sdf.parse(TIMER);
            //获取系统当前时间
            Date NowTimer = new Date();
            //获取时间差，毫秒数
            long diff = NowTimer.getTime() - StandardTimer.getTime();
            hour = diff % nd / nh;
            // 计算差多少分钟
            minute = diff % nd % nh / nm;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (TYPE == 0) {
            return getWorkTimerCompare(hour, minute);
        }
        if (TYPE == 1) {
            return getWorkOutTimerCompare(hour, minute);
        }
        return false;
    }

    /**
     * 下班时间
     **/
    private static boolean getWorkOutTimerCompare(long hour, long minute) {
        boolean isFlag = false;
        //17;20
        if (hour == 0 && minute >= 0) {
            isFlag = true;
        }
        if (hour > 0 && hour < 7) {
            isFlag = true;
        }
        if (hour >= 8) {
            isFlag = false;
        }
        return isFlag;
    }

    /**
     * 判断打卡时间是否超时
     **/
    private static boolean getWorkTimerCompare(long hour, long minute) {
        boolean isFlag = false;
        //9:25
        if (hour == 0 && minute <= 0) {
            isFlag = true;
        }
        if (hour > 0 || (hour == 0 && minute > 0)) {
            isFlag = false;
        }
        return isFlag;
    }

    public static String getWifi(String s) {
        int length = s.length();
        String str = s.substring(1, length - 1);
        return str;
    }

    /**
     * hh：返回12小时制，HH：返回24小时制
     **/
    public static String getNowDate() {
        SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");
        return sp.format(new Date());
    }

    public static String getNowDateWithoutSD() {
        SimpleDateFormat sp = new SimpleDateFormat("HH:mm");
        return sp.format(new Date());
    }

    public static void isCompareTime() {

    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * 截取字符串-后两位
     **/
    public static String getLastTwoString(String targetString) {
        if (targetString.length() == 1) {
            return targetString;
        }
        if (targetString.length() >= 2) {
            return targetString.substring(targetString.length() - 2, targetString.length());
        }
        return null;
    }

}
