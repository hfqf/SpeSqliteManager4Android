package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.points.spesqlitemanager.spesqlite.bean.SpeSqliteColumnSettingModel;
import com.points.spesqlitemanager.spesqlite.bean.SpeSqliteSettingModel;
import com.points.spesqlitemanager.spesqlite.bean.SpeSqliteTableSettingModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hfqf123@126.com
 * @brief 用于支持对存储在SD卡上的数据库的访问
 * @date 2023-02-14
 */
public class SpeSqliteDBService extends SQLiteOpenHelper {

    private static final String TAG     = "SpeSqliteDBService";
    private static SpeSqliteDBService instance  = null;
    private Context context = null;
    private SQLiteDatabase db = null;

    public static synchronized SpeSqliteDBService getInstance(Context context) {
        if (instance == null){
            synchronized (SpeSqliteDBService.class){
                if (instance == null){
                    instance = new SpeSqliteDBService(context);
                    instance.context = context;
                    instance.db = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    public SpeSqliteDBService(Context context) {
        super(context,SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbName,null,SpeSqliteUpdateManager.getInstance().init(context).currentAppDBSetting().dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SpeSqliteUpdateManager.getInstance().create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
         SpeSqliteUpdateManager.getInstance().upgrade(this,db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }

//    public  static  void updateContact(Contact contact){
//        Log.e(TAG,"updateContact:"+LoggerUtil.jsonFromObject(contact));
//        SQLiteDatabase db = instance.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("carcode", contact.getCarCode().length() == 0? "" :contact.getCarCode() );
//        db.update("contact", cv, "idfromnode=?", new String[]{contact.getIdfromnode()});
//        db.close();
//    }
//    public  static ArrayList queryAllContact(){
//        Log.e(TAG,"queryAllContact");
//        SQLiteDatabase db = instance.getWritableDatabase();
//        Cursor c = db.rawQuery("SELECT * FROM contact", null);
//        ArrayList<Contact> arr = new ArrayList<>();
//        while (c.moveToNext()) {
//            Contact con = DBService.getContact(c);
//            arr.add(con);
//        }
//
//        Gson gson2 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
//        String obj2 = gson2.toJson(arr);
//        Log.e(TAG,"queryAllContact 获取完毕:"+obj2);
//        c.close();
//        db.close();
//        return arr.size() > 0 ? arr : new  ArrayList();
//    }


}

