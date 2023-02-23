package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author hfqf123@126.com
 * @brief description
 * @date 2023-02-23
 */
public class SpeSqliteOpenHelperService extends  SpeSqliteAbstractDBService {
    public SpeSqliteOpenHelperService(Context context) {
        super(context);
    }

    public  static SpeSqliteAbstractDBService getInstance(Context context) {
        if (instance == null){
            synchronized (SpeSqliteOpenHelperService.class){
                if (instance == null){
                    instance = new SpeSqliteOpenHelperService(context);
                    instance.context = context;
                    instance.db = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SpeSqliteUpdateManager.getInstance().create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        SpeSqliteUpdateManager.getInstance().upgrade(db,null);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }
}
