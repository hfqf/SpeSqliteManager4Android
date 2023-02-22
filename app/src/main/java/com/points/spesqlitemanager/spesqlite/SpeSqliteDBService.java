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
    public OnUpdateInterface listener;
    public interface  OnUpdateInterface{
        public void onCreate(SQLiteDatabase db);
        public void onOpen(SQLiteDatabase db);
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion);
    }
    public static synchronized SpeSqliteDBService getInstance(Context context,OnUpdateInterface listener) {
        if (instance == null){
            synchronized (SpeSqliteDBService.class){
                if (instance == null){
                    instance = new SpeSqliteDBService(context);
                    if(listener != null){
                        instance.listener = listener;
                    }
                    instance.context = context;
                    instance.db = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    public void setListener(OnUpdateInterface listener) {
        instance.listener = listener;
    }

    public SpeSqliteDBService(Context context) {
        super(context,SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbName,
                null,SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(instance.listener != null){
            instance.listener.onCreate(db);
        }
        SpeSqliteUpdateManager.getInstance().create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        if(instance.listener != null){
            instance.listener.onUpgrade(db,oldVersion,newVersion);
        }
        SpeSqliteUpdateManager.getInstance().upgrade(db,null);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(instance.listener != null){
            instance.listener.onOpen(db);
        }

    }

    public  SQLiteDatabase getDB(){
        return instance.db;
    }
}

