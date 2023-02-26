package cn.autorepairehelpe.spesqlite.bean;

/**
 * @author hfqf123@126.com
 * @brief sql中字段模型
 * @date 2023-02-14
 */
public class SpeSqliteColumnSettingModel {
    /**
     * key名
     */
    public String key;

    /**
     * 该key的在sql中的key字段修饰,比如INTEGER PRIMARY KEY AUTOINCREMENT或TEXT
     */
    public String keyType;
}
