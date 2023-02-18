package com.points.spesqlitemanager.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author hfqf123@126.com
 * @brief description
 * @date 2023-02-18
 */
@Entity(tableName = "notice")
public class NoticeModel {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo
    private Integer orderId;
    @ColumnInfo
    private String title;

    @ColumnInfo
    private String age;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
