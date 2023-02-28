package com.points.spesqlitemanager.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author hfqf123@126.com
 * @brief description
 * @date 2023-02-18
 */
@Database(entities = {ServerModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ServerDao serverDao();
}
