package com.points.spesqlitemanager.spesqlite;

import android.database.sqlite.SQLiteDatabase;

import androidx.room.RoomDatabase;

/**
 * @author hfqf123@126.com
 * @brief 数据库生命周期抽象接口
 * @date 2023-02-23
 */
public interface SpeSqliteBaseInterface {

        /**
         * DB创建成功，使用时需提前判断，参考如下
         *    if(db instanceof SQLiteDatabase){
         *
         *    }else if(db instanceof SupportSQLiteDatabase){
         *
         *    }
         * @param db db
         * @param room 当为RoomDatabase创建的此时有值
         * @param <T> 范型（需考虑SQLiteOpenHelper和room）
         */
        public <T> void onCreate(T db, RoomDatabase room);

        /**
         * DB创建打开，使用时需提前判断，参考如下
         *    if(db instanceof SQLiteDatabase){
         *
         *    }else if(db instanceof SupportSQLiteDatabase){
         *
         *    }
         * @param db db
         * @param room 当为RoomDatabase打开的此时有值
         * @param <T> 范型（需考虑SQLiteOpenHelper和room）
         */
        public <T> void onOpen(T db, RoomDatabase room);

        /**
         * SQLiteOpenHelper需要升级,使用时需提前判断，参考如下
         *    if(db instanceof SQLiteDatabase){
         *
         *    }else if(db instanceof SupportSQLiteDatabase){
         *
         *    }
         * @param db db
         * @param oldVersion 数据库之前版本号
         * @param newVersion 数据库最新版本号
         * @param room 当为RoomDatabase升级的此时有值
         * @param <T> 范型（需考虑SQLiteOpenHelper和room）
         */
        public <T> void onUpgrade(T db,int oldVersion,int newVersion, RoomDatabase room);
}
