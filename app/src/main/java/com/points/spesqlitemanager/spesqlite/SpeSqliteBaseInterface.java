package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author hfqf123@126.com
 * @brief description
 * @date 2023-02-23
 */
public interface SpeSqliteBaseInterface {
        public void onSpeSqliteCreate(SQLiteDatabase db);
        public void onSpeSqliteOpen(SQLiteDatabase db);
        public void onSpeSqliteUpgrade(SQLiteDatabase db,int oldVersion,int newVersion);
}
