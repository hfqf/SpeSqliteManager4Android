package cn.autorepairehelper.spesqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author hfqf123@126.com
 * @brief 数据库管理抽象类,只提供构造函数
 * @date 2023-02-14
 */
public abstract class SpeSqliteAbstractDBService extends SQLiteOpenHelper {
    public static final     String TAG     = "SpeSqliteDBService";
    public static           SpeSqliteAbstractDBService instance  = null;
    public Context          context = null;
    public SQLiteDatabase db = null;
    public Class            roomClass;
    /**
     * 提供回调给调用者，通知其数据库状态
     */
    public SpeSqliteBaseInterface listener;

    /**
     * 利用SQLiteOpenHelper的特性发起升级流程
     * @param context context
     */
    public SpeSqliteAbstractDBService(Context context) {
        super(context, SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbName,
                null, SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbVersion);
    }

}

