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


    public SpeSqliteSettingModel getAppAssetsDBSetting(Context context){
        String json = JsonUtil.getJson("dbupdate.json",context);
        Gson gson = new Gson();
        SpeSqliteSettingModel model = gson.fromJson(json, SpeSqliteSettingModel.class);
        return model;
    }

    public void init(Context context){
        this.appContext =context;
    }
    private SpeSqliteColumnSettingModel getAppLoclDBSetting(){
        return null;
    }
}
