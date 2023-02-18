package com.points.spesqlitemanager.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * @author hfqf123@126.com
 * @brief description
 * @date 2023-02-18
 */

@Dao
public interface ServerDao {
    @Query("SELECT * FROM history_server")

    List<ServerModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ServerModel model);

    @Delete
    void delete(ServerModel model);

}