package cn.autorepairehelper.spesqlite.bean;
import java.util.ArrayList;

/**
 * @author hfqf123@126.com
 * @brief 数据库表模型
 * @date 2023-02-14
 */
public class SpeSqliteTableSettingModel {
    /**
     * 表名
     */
    public String tableName;

    /**
     * 当前表字段设计
     */
    public ArrayList<SpeSqliteColumnSettingModel> columns;

    /**
     * 是否被索引过：代表是否在新表里被删除了
     */
    public  boolean indexed;

}
