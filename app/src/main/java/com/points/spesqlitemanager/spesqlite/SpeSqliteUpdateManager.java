package com.points.spesqlitemanager.spesqlite;

import android.content.Context;

import com.google.gson.Gson;
import com.points.spesqlitemanager.spesqlite.bean.SpeSqliteColumnSettingModel;
import com.points.spesqlitemanager.spesqlite.bean.SpeSqliteSettingModel;
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
}
