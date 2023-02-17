package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author hfqf123@126.com
 * @brief 用于支持对存储在SD卡上的数据库的访问
 * @date 2023-02-14
 */
public class SpeSqliteDBService extends SQLiteOpenHelper {
    private static final String TAG     = "SpeSqliteDBService";
    private static SpeSqliteDBService instance  = null;
    private Context context = null;
    private SQLiteDatabase db = null;
    public static synchronized SpeSqliteDBService getInstance(Context context) {
        if (instance == null){
            synchronized (SpeSqliteDBService.class){
                if (instance == null){
                    instance = new SpeSqliteDBService(context);
                    instance.context = context;
                    instance.db = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    public SpeSqliteDBService(Context context) {
        super(context,SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbName,
                null,SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SpeSqliteUpdateManager.getInstance().create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
         SpeSqliteUpdateManager.getInstance().upgrade(db);
    }
}

