package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.points.spesqlitemanager.spesqlite.bean.SpeSqliteColumnSettingModel;
import com.points.spesqlitemanager.spesqlite.bean.SpeSqliteSettingModel;
import com.points.spesqlitemanager.spesqlite.bean.SpeSqliteTableSettingModel;
import com.points.spesqlitemanager.spesqlite.utils.JsonUtil;

/**
 * @author hfqf123@126.com
 * @brief 用于支持对存储在SD卡上的数据库的访问
 * @date 2023-02-14
 */
public class SpeSqliteUpdateManager {
    /**
     *主app的context
     */
    private Context appContext = null;

    /**
     *本地sd卡中的配置项
     */
    private SpeSqliteSettingModel localDBSetting = null;

    /**
     *当前app中assets的配置项
     */
    private SpeSqliteSettingModel currentAppDBSetting = null;

    private final  Gson gson = new Gson();

    private String currentDBJson = null;

    private static final String kDBJsonName  = "dbupdate.json";

    private SpeSqliteUpdateManager() {

    }

    public static SpeSqliteUpdateManager getInstance() {
        return SingletonClassInstance.instance;
    }

    private static class SingletonClassInstance {
        private static final SpeSqliteUpdateManager instance = new SpeSqliteUpdateManager();
    }

    private boolean checkUpdate(){
        return true;
    }

    public String getCurrentDBJson() {
        currentDBJson = JsonUtil.getJson(kDBJsonName,this.appContext);
        return currentDBJson;
    }

    public SpeSqliteSettingModel currentAppDBSetting(){
        SpeSqliteSettingModel model = gson.fromJson(this.getCurrentDBJson(), SpeSqliteSettingModel.class);
        return model;
    }

    public SpeSqliteUpdateManager init(Context context){
        this.appContext =context;
        return this;
    }


    private SpeSqliteColumnSettingModel getAppLoclDBSetting(){
        return null;
    }


    /**
     * 数据库第一次创建时的调用函数
     * @param db
     */
    public void create(SQLiteDatabase db){
        SpeSqliteSettingModel currentDBModel = SpeSqliteUpdateManager.getInstance().currentAppDBSetting();
        for(int i=0;i<currentDBModel.dbTables.size();i++){
            SpeSqliteTableSettingModel table = currentDBModel.dbTables.get(i);
            String sql = " create table if not exists "+table.tableName+" (";
            for(int j=0;j<table.columns.size();j++){
                SpeSqliteColumnSettingModel column = table.columns.get(j);
                sql+=column.key+" ";
                sql+=column.keyType;
                if(j==table.columns.size()-1){
                    sql+=" ";
                }else {
                    sql+=",";
                }
            }
            sql+=")";
            db.execSQL(sql);
        }
    }

    /**
     * 升级数据库，此处涉及3种改动：1.新建表 2.老表新增字段 3.删除表
     * 1.新建表的处理思路：比较简单直接create即可
     * 2.老表新增字段需要遍历db中的json表字段明细和当前app中的json明细
     * @param db
     */
    public void upgrade(SQLiteDatabase db){

    }
    public void open(SQLiteDatabase db){

    }
}
