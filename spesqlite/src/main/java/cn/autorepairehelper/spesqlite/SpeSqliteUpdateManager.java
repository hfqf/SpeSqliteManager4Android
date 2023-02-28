package cn.autorepairehelper.spesqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import cn.autorepairehelper.spesqlite.bean.SpeSqliteColumnSettingModel;
import cn.autorepairehelper.spesqlite.bean.SpeSqliteSettingModel;
import cn.autorepairehelper.spesqlite.bean.SpeSqliteTableSettingModel;
import cn.autorepairehelper.spesqlite.utils.SpeSqliteJsonUtil;

import java.util.ArrayList;

/**
 * @author hfqf123@126.com
 * @brief 该管理类负责针对本地db的创建、新建表、表字段升级、删除表,通过配置的方式去升级数据库，减少代码的改动，核心思想：以静制动。
 * @date 2023-02-14
 */
public class SpeSqliteUpdateManager {
    /**
     *主app的context
     */
    private Context appContext = null;

    /**
     *本地db中的配置项
     */
    private SpeSqliteSettingModel localDBSetting = null;

    /**
     *当前app中assets的配置项
     */
    private SpeSqliteSettingModel currentAppDBSetting = null;

    private final  Gson gson = new Gson();

    private String currentDBJson = null;

    /**
     * 对应assets中的文件名
     */
    private static final String kDBJsonName  = "dbupdate.json";

    private SpeSqliteUpdateManager() {

    }

    public static SpeSqliteUpdateManager getInstance() {
        return SingletonClassInstance.instance;
    }

    private static class SingletonClassInstance {
        private static final SpeSqliteUpdateManager instance = new SpeSqliteUpdateManager();
    }

    public SpeSqliteUpdateManager init(Context context){
        this.appContext =context;
        return this;
    }

    /**
     * 数据库第一次创建时的调用函数
     * @param db db
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
            executeSQL(db,sql);
        }
        //SQLiteDatabase数据库才需要升级本地数据配置
        if(db instanceof SQLiteDatabase){
            SQLiteDatabase _db = (SQLiteDatabase)db;
            updateConfig2DB(_db,currentDBModel);
        }
    }

    /**
     * 升级数据库，此处涉及3种改动：1.新建表 2.老表新增字段 3.删除表
     * 注意该方法会被两个数据库依次触发，所以需要控制
     * 1.新建表的处理思路：比较简单直接create即可
     * 2.老表新增字段需要遍历db中的json表字段明细和当前app中的json明细
     * @param db db
     */
    public  void upgrade(SQLiteDatabase configdb,SupportSQLiteDatabase db){
        SpeSqliteSettingModel newConfig = this.currentAppDBSetting();
        SpeSqliteSettingModel localConfig = this.getAppLoclDBSetting(configdb);
        //该处判断可以不要，但是加了后(daupdate.json的dbversion字段)效率更高
        if(localConfig.dbVersion< newConfig.dbVersion){//通过dbversion直接判断是否要升级
            //防止room数据未创建表,就alter
            if(db != null){
                for(int i=0;i<localConfig.dbTables.size();i++) {
                    SpeSqliteTableSettingModel _local = localConfig.dbTables.get(i);
                    createTableSQL(db,_local);
                }
            }
            for(int j=0;j<newConfig.dbTables.size();j++){
                SpeSqliteTableSettingModel _new = newConfig.dbTables.get(j);
                for(int i=0;i<localConfig.dbTables.size();i++){
                    SpeSqliteTableSettingModel _local = localConfig.dbTables.get(i);
                    if(_local.tableName.equals(_new.tableName)){//找到，再判断字段是否有新增
                        _local.indexed = true;//被比较过,该表不用删除
                        if(_local.columns.size()<_new.columns.size()){
                            //执行alert去新增字段 //
                            alterCoulmns(db != null?db:configdb,_local,_new);
                        }
                        break;//只要匹配到就直接跳出该层循环
                    }
                }
                //本地数据没找到这个表需要新增
                createTableSQL(db != null?db:configdb,_new);
            }
            //针对被废弃的表需要在本地库中删除
            for(int i=0;i<localConfig.dbTables.size();i++){
                SpeSqliteTableSettingModel table = localConfig.dbTables.get(i);
                if(!table.indexed){
                    dropTables(db != null?db:configdb,localConfig.dbTables.get(i));
                }
            }
            //只有当是SQLiteDatabase升级时才能本地数据配置
            if(db == null){
                updateConfig2DB(configdb,newConfig);
            }
        }
    }

    /**
     * 移除老表
     * @param db db
     * @param table table
     * @param <T> 范型（需考虑SQLiteOpenHelper和room）
     */
    private <T> void dropTables(T db,SpeSqliteTableSettingModel table){
        String sql = " DROP TABLE IF EXISTS "+table.tableName;
        executeSQL(db,sql);
    }

    /**
     * 老表新增字段
     * @param db db
     * @param _old 老表字段配置
     * @param _new 表新字段配置
     * @param <T> 范型（需考虑SQLiteOpenHelper和room）
     */
    private <T> void alterCoulmns(T db,SpeSqliteTableSettingModel _old,SpeSqliteTableSettingModel _new){
        for(int i=0;i<_new.columns.size();i++){
            if(i>=_old.columns.size()){
                SpeSqliteColumnSettingModel column = _new.columns.get(i);
                String sql = " alter table "+_old.tableName+" add column ";
                sql+=column.key;
                sql+=" ";
                sql+= column.keyType;
                executeSQL(db,sql);
            }
        }
    }

    /**
     * 新增表
     * @param db db
     * @param table 表配置
     * @param <T> 范型（需考虑SQLiteOpenHelper和room）
     */
    private <T> void createTableSQL(T db,SpeSqliteTableSettingModel table){
        String sql = " create table if not exists "+table.tableName+" (";
        for(int j=0;j<table.columns.size();j++){
            SpeSqliteColumnSettingModel column = table.columns.get(j);
            sql+=column.key+" ";
            sql+=column.keyType;
            if(j==table.columns.size()-1){
                sql+=")";
            }else {
                sql+=",";
            }
        }
        executeSQL(db,sql);
    }

    /**
     * 从assets中获取数据库配置信息，如果没有需要初始化一个
     * @return json
     */
    public String getCurrentDBJson() {
        currentDBJson = SpeSqliteJsonUtil.getJson(kDBJsonName,this.appContext);
        return currentDBJson;
    }

    /**
     * 当数据库升级完毕后，需要将此次数据库配置更新数据库中的dbconfig表,以便给下次升级数据库时的比较
     * @param db db
     * @param currentDBConfig 当前最新db配置
     */
    public  void updateConfig2DB(SQLiteDatabase db,SpeSqliteSettingModel currentDBConfig){
        //先删除之前的记录
        db.delete("dbconfig", "", new String[]{});
        //再直接插入新配置
        ContentValues cv = new ContentValues();
        cv.put("dbversion", currentDBConfig.dbVersion );
        cv.put("dbname", currentDBConfig.dbName );
        cv.put("dbtables", gson.toJson(currentDBConfig.dbTables) );
        db.insert("dbconfig",null,cv);
    }

    /**
     * 获取上次数据库配置
     * @param db db
     * @return 上次数据库配置
     */
    public SpeSqliteSettingModel getAppLoclDBSetting(SQLiteDatabase db){
        Cursor c = db.rawQuery("SELECT * FROM dbconfig",new String[]{});
        SpeSqliteSettingModel model = new SpeSqliteSettingModel();
        while (c.moveToNext()) {
            String dbName = c.getString(c.getColumnIndex("dbname"));
            String dbVersion = c.getString(c.getColumnIndex("dbversion"));
            String dbTables = c.getString(c.getColumnIndex("dbtables"));
            model.dbName = dbName;
            model.dbVersion =  Integer.parseInt(dbVersion);

            JsonArray array = JsonParser.parseString(dbTables).getAsJsonArray();
            ArrayList<SpeSqliteTableSettingModel> tables = new ArrayList<>();
            for(int i=0;i<array.size();i++){
                SpeSqliteTableSettingModel table = gson.fromJson(array.get(i),SpeSqliteTableSettingModel.class);
                tables.add(table);
            }
            model.dbTables = tables;
        }
        return model;
    }

    /**
     * 获取当前最新db配置
     * @return 当前最新db配置
     */
    public SpeSqliteSettingModel currentAppDBSetting(){
        SpeSqliteSettingModel model = gson.fromJson(this.getCurrentDBJson(), SpeSqliteSettingModel.class);
        if(model.dbName.equals("temp")){
            Log.e("SpeSqliteUpdateManager","需要在assets中新建json文件");
        }
        return model;
    }

    /**
     * 真正执行sql
     * @param db db
     * @param sql sql
     * @param <T> 范型（需考虑SQLiteOpenHelper和room）
     */
    private <T> void  executeSQL(T  db,String sql){
        Log.e("SpeSqliteUpdateManager",db.getClass().getName()+sql);
        if(db instanceof SQLiteDatabase){
            SQLiteDatabase _db = (SQLiteDatabase)db;
            _db.execSQL(sql);
        }else if(db instanceof SupportSQLiteDatabase){
            SupportSQLiteDatabase _db = (SupportSQLiteDatabase)db;
            _db.execSQL(sql);
        }
    }
}
