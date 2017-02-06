package com.work.xinlai.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/11/23.
 */

/**
 * DButils中构造方法私有，getInstance方法私有
 **/

public class DatabaseOpen extends SQLiteOpenHelper {
    protected static final String DB_NAME = "xinlaidb";
    private static final int DB_VERSION = 1;
    //创建弱引用
    public static WeakReference<DatabaseOpen> sInstanceRef;

    private DatabaseOpen(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 获取databaseopen实例
     **/
    public static synchronized DatabaseOpen getInstance(Context context) {
        //每次创建后会将实例保存在若引用，判断弱引用中是否拥有实例
        if (sInstanceRef != null && sInstanceRef.get() != null) {
            return sInstanceRef.get();
        }
        //若引用不存在实例则创建实例并且保存
        DatabaseOpen instance = new DatabaseOpen(context);
        sInstanceRef = new WeakReference<DatabaseOpen>(instance);
        return instance;
    }


    /*rivate static final String CREATE_CACHE_NEWS = new StringBuilder("CREATE TABLE ")
            .append(CacheNewsInfo.TABLE_NAME).append(" (")
            .append(CacheNewsInfo._ID).append(" INTEGER PRIMARY KEY ASC,")
            .append(CacheNewsInfo.ID).append(" INTEGER,")
            .append(CacheNewsInfo.URL).append(" VARCHAR(255),")
            .append(CacheNewsInfo.CONTENT_URL).append(" VARCHAR(255),")
            .append(CacheNewsInfo.PUBLIC_URL).append(" VARCHAR(255),")
            .append(CacheNewsInfo.TITLE).append(" VARCHAR(255),")
            .append(CacheNewsInfo.SUMMARY).append(" VARCHAR(255),")
            .append(CacheNewsInfo.COVER).append(" VARCHAR(255),")
            .append(CacheNewsInfo.THUMBNAIL).append(" VARCHAR(255),")
            .append(CacheNewsInfo.CREATE_TIME).append(" DOUBLE,")
            .append(CacheNewsInfo.SOURCE_ID).append(" INTEGER,")
            .append(CacheNewsInfo.SOURCE_NAME).append(" VARCHAR(255),")
            .append(CacheNewsInfo.FAVORITE).append(" BOOLEAN,")
            .append(CacheNewsInfo.LIKED).append(" BOOLEAN,")
            .append(CacheNewsInfo.LIKED_COUNT).append(" INTEGER,")
            .append(CacheNewsInfo.COMMENTED_COUNT).append(" INTEGER,")
            .append(CacheNewsInfo.TYPE).append(" INTEGER")
            .append(")").toString();*/

    /**
     * 创建的sql语句数据，按照数组的形式去创建数据库
     **/
   /* private static String[] sCreateSqls = new String[] {
            CREATE_COURSE,
            CREATE_USERENTRY,
            CREATE_USERINFO,
            CREATE_CHATRECORD,
            CREATE_GROUPINFO,
            CREATE_EMOTION_INFO,
            CREATE_SEARCH_TAG_INFO,
            CREATE_MATCH_JOB_INFO,
            CREATE_CACHE_BANNER,
            CREATE_CACHE_CHANNEL,
            CREATE_CACHE_IMAGE,
            CREATE_CACHE_POST,
            CREATE_CACHE_SCHOOL,
            CREATE_CACHE_USERINFO,
            CREATE_CACHE_VOTE,
            CREATE_CACHE_NEWS,
            CREATE_FAST_ENTRANCE_INFO
    };*/

    /**
     * sqlite 提供ALTER TABLE命令，允许用户重命名或是添加新字段到表中，但是不能从表中删除字段，
     * 并且添加字段智能在数据表的末尾。
     **/
   /* private static final String[] UPGRADE_2_3 = new String[] {
            new StringBuilder("ALTER TABLE ").append(UserInfo.TABLE_NAME).append(" ADD ")
                    .append(UserInfo.NICKNAME).append(" VARCHAR(255)")
                    .toString(),
            new StringBuilder("ALTER TABLE ").append(UserInfo.TABLE_NAME).append(" ADD ")
                    .append(UserInfo.GUID).append(" CHAR(32)")
                    .toString(),
            new StringBuilder("ALTER TABLE ").append(UserInfo.TABLE_NAME).append(" ADD ")
                    .append(UserInfo.AVATAR_FULL).append(" VARCHAR(255)")
                    .toString()
    };*/

    /**
     * 数据库更新使用类似模式
     **/
   /* private static String[][] sUpdateSqls = new String[][] {
            UPGRADE_1_2,
            UPGRADE_2_3,
            UPGRADE_3_4,
            UPGRADE_4_5,
            UPGRADE_5_6,
            UPGRADE_6_7,
            UPGRADE_7_8,
            UPGRADE_8_9,
            UPGRADE_9_10,
            UPGRADE_10_11,
            UPGRADE_11_12,
            UPGRADE_12_13,
            UPGRADE_13_14,
            UPGRADE_14_15,
            UPGRADE_15_16,
            UPGRADE_16_17,
            UPGRADE_17_18
    };*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**以数组的形式遍历数组大小进行数据库的创建**/
       /* for (String sql : sCreateSqls) {
            db.execSQL(sql);
        }*/
    }

    /**
     * 数据库升级
     **/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
           /* if ((i - 1) >= sUpdateSqls.length) {
                return;
            }
            String[] updates = sUpdateSqls[i - 1];
            for (int j = 0; j < updates.length; j++) {
                try {
                    db.execSQL(updates[j]);
                } catch (Exception e) {
                }
            }*/
        }

    }
}
