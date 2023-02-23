package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author hfqf123@126.com
 * @brief 专门针对SQLiteOpenHelper数据库的升级服务类,需借助dbupdate.json完成数据创建、升级
 * @date 2023-02-23
 */
public class SpeSqliteOpenHelperService extends  SpeSqliteAbstractDBService {
    public SpeSqliteOpenHelperService(Context context) {
        super(context);
    }

    /**
     * 提供创建方法，需监听db
     * @param context context
     * @param listener listener
     * @return SpeSqliteAbstractDBService
     */
    public  static SpeSqliteAbstractDBService getInstance(Context context,SpeSqliteBaseInterface listener) {
        if (instance == null){
            synchronized (SpeSqliteOpenHelperService.class){
                if (instance == null){
                    instance = new SpeSqliteOpenHelperService(context);
                    instance.listener = listener;
                    instance.context = context;
                    instance.db = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    /**
     * 提供简单创建方法，无需监听db
     * @param context context
     * @return SpeSqliteOpenHelperService
     */
    public  static SpeSqliteOpenHelperService getInstance(Context context) {
        return  SpeSqliteOpenHelperService.getInstance(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SpeSqliteUpdateManager.getInstance().create(db);
        if(instance.listener!=null){
            instance.listener.onCreate(db,null);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        SpeSqliteUpdateManager.getInstance().upgrade(db,null);
        if(instance.listener!=null){
            instance.listener.onUpgrade(db,oldVersion,newVersion,null);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if(instance.listener!=null){
            instance.listener.onOpen(db,null);
        }
    }
}
