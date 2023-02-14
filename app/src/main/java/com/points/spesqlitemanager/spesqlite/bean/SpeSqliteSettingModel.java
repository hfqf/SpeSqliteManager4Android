package com.points.spesqlitemanager.spesqlite.bean;
import java.util.ArrayList;

/**
 * @author hfqf123@126.com
 * @brief 数据库总模型
 * @date 2023-02-14
 */
public class SpeSqliteSettingModel {
    /**
     * 数据库名
     */
    public String dbName;

    /**
     * 数据库版本号
     */
    public String dbVersion;

    /**
     * 所有表数组
     */
    public ArrayList<SpeSqliteTableSettingModel> dbTables;
}
