package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.room.RoomDatabase;

/**
 * @author hfqf123@126.com
 * @brief 用于支持对存储在SD卡上的数据库的访问
 * @date 2023-02-14
 */
public abstract class SpeSqliteAbstractDBService extends SQLiteOpenHelper   {
    public static final String TAG     = "SpeSqliteDBService";
    public static SpeSqliteAbstractDBService instance  = null;
    public Context context = null;
    public SQLiteDatabase db = null;
    public Class roomClass;

    public SpeSqliteAbstractDBService(Context context) {
        super(context,SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbName,
                null,SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbVersion);
    }

}

