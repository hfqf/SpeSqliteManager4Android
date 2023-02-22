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
@Entity(tableName = "history_server")
public class ServerModel {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String host;
    @ColumnInfo
    private String version;
    @ColumnInfo
    private String lang;
    @ColumnInfo
    private String k1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setK1(String k1) {
        this.k1 = k1;
    }

    public String getK1() {
        return k1;
    }
}
