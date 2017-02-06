package com.work.xinlai.data.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.work.xinlai.util.Logger;
import com.work.xinlai.util.Utils;

/**
 * Created by Administrator on 2016/11/23.
 */
public class DataHelper {
    private static final String TAG = Utils.getDebugTAG(DataHelper.class);
    private static final String NAME_SHARED_PREFS = "light_weight_data";

    //share-NAME_SHARED_PREFS
    public static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(NAME_SHARED_PREFS, 0);
    }

    /**
     * 删除所有数据-用户改变
     **/
    public static void deleteAlldata(Context context) {
        //删除数据库
        context.deleteDatabase(DatabaseOpen.DB_NAME);
        //删除share
        context.getSharedPreferences(NAME_SHARED_PREFS, 0).edit().clear().commit();
    }

    /*public static synchronized boolean updateChatRecordbyMsgType(Context context, ChatRecord record) {
        SQLiteDatabase db = getRead(context);
        if (db == null) {
            return false;
        }

        int result = db.update(ChatRecord.TABLE_NAME, fillChatRecord(record), ChatRecord.MSG_TYPE + "='" + record.msgType + "'",
                null);
        db.close();

        return result > 0;
    }
    */
    //返回可读数据库
    public static SQLiteDatabase getRead(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return DatabaseOpen.getInstance(context).getReadableDatabase();
        } catch (SQLiteException e) {
            Logger.e(TAG, e.getMessage());
        }
        return null;
    }

    //返回可写数据库
    public static SQLiteDatabase getWrite(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return DatabaseOpen.getInstance(context).getWritableDatabase();
        } catch (SQLiteException e) {
            Logger.e(TAG, e.getMessage());
        }
        return null;
    }
}
