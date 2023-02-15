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

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        SpeSqliteUpdateManager.getInstance().create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
         SpeSqliteUpdateManager.getInstance().upgrade(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        SpeSqliteUpdateManager.getInstance().open(db);
    }

    private  void  updateSQL(SQLiteDatabase db,int oldVersion,int newVersion) {

//        //2.2
//        if(newVersion == 2) {
//            List<String> arrTable = new ArrayList<String>(Arrays.asList(
//                    "alter table contact add column owner TEXT",
//                    "alter table contact add column idfromnode TEXT",
//                    "alter table repairHistory add column owner TEXT",
//                    "alter table repairHistory add column idfromnode TEXT",
//                    "alter table repairHistory add column inserttime TEXT"));
//            for (int i = 0; i < arrTable.size(); i++) {
//                String sql = arrTable.get(i);
//                Log.e(TAG,"updateSQL:"+sql);
//                db.execSQL(sql);
//            }
//        }else if(dbVersion == 3)//当是从3.2直接开始的新用户
//        {
//
//            List<String> arrAlertTable = new ArrayList<String>(Arrays.asList(
//                    "alter table contact add column inserttime TEXT",
//                    "alter table contact add column isbindweixin TEXT",
//                    "alter table contact add column weixinopenid TEXT",
//                    "alter table contact add column vin TEXT",
//                    "alter table contact add column carregistertime TEXT",
//                    "alter table contact add column headurl TEXT"));
//            for (int i = 0; i < arrAlertTable.size(); i++) {
//                String sql = arrAlertTable.get(i);
//                Log.e(TAG,"updateSQL:"+sql);
//                db.execSQL(sql);
//            }
//
//        }else if(dbVersion == 4)//当是从3.4直接开始的新用户
//        {
//            List<String> arrAlertTable = new ArrayList<String>(Arrays.asList(
//                    "alter table contact add column safecompany TEXT",
//                    "alter table contact add column safenexttime TEXT",
//                    "alter table contact add column yearchecknexttime TEXT",
//                    "alter table contact add column tqTime1 TEXT",
//                    "alter table contact add column tqTime2 TEXT"));
//            for (int i = 0; i < arrAlertTable.size(); i++) {
//                String sql = arrAlertTable.get(i);
//                Log.e(TAG,"updateSQL:"+sql);
//                db.execSQL(sql);
//            }
//        }

        String delsql = "DROP table contact";
        db.execSQL(delsql);


        List<String> arrTable  = new ArrayList<String>(Arrays.asList("create table if not exists  contact (carcode TEXT,name TEXT,tel TEXT,cartype TEXT,owner TEXT,idfromnode TEXT," +
                " inserttime TEXT,isbindweixin TEXT,weixinopenid TEXT,vin TEXT,carregistertime TEXT,headurl TEXT," +
                "safecompany TEXT,safenexttime TEXT,yearchecknexttime TEXT,tqTime1 TEXT,tqTime2 TEXT,key TEXT,isVip TEXT,carId TEXT,safecompany3 TEXT,safenexttime3 TEXT,tqTime3 TEXT,safetiptime3 TEXT)"
        ));

        for (int i=0;i<arrTable.size();i++)
        {
            String sql = arrTable.get(i);
            db.execSQL(sql);
        }

    }

    public static void closeDB(SQLiteDatabase db) {
        if(db != null)
        db.close();
    }

    public static SQLiteDatabase getDB() {
        SQLiteDatabase db = instance.getWritableDatabase();
        return db;
    }

//    public static void addNewContact(Contact contact,SQLiteDatabase db){
//
//
//        ContentValues cv = new ContentValues();
//        cv.put("carcode", contact.getCarCode().length() == 0? "" :contact.getCarCode() );
//
//        cv.put("name",contact.getName().length() == 0? "" :contact.getName() );
//
//        cv.put("cartype", contact.getCarType().length() == 0? "" :contact.getCarType() );
//
//        cv.put("owner",contact.getOwner().length() == 0? "" :contact.getOwner() );
//
//        cv.put("idfromnode", contact.getIdfromnode().length() == 0? "" :contact.getIdfromnode() );
//
//        cv.put("isbindweixin", contact.getIsbindweixin().length() == 0? "0" :contact.getIsbindweixin() );
//
//        cv.put("weixinopenid",contact.getWeixinopenid().length() == 0? "" :contact.getWeixinopenid() );
//
//        cv.put("vin",contact.getVin().length() == 0? "" :contact.getVin());
//
//        cv.put("carregistertime", contact.getCarregistertime().length() == 0? "" :contact.getCarregistertime());
//
//        cv.put("headurl", contact.getHeadurl().length() == 0? "" :contact.getHeadurl());
//
//        cv.put("inserttime", contact.getInserttime().length() == 0? "" :contact.getInserttime());
//
//        cv.put("tel", contact.getTel().length() == 0? "" :contact.getTel());
//
//
//        cv.put("safecompany", contact.getSafecompany().length() == 0? "" :contact.getSafecompany());
//        cv.put("safenexttime", contact.getSafenexttime().length() == 0? "" :contact.getSafenexttime());
//        cv.put("yearchecknexttime", contact.getYearchecknexttime().length() == 0? "" :contact.getYearchecknexttime());
//        cv.put("tqTime1", contact.getTqTime1().length() == 0? "" :contact.getTqTime1());
//        cv.put("tqTime2", contact.getTqTime2().length() == 0? "" :contact.getTqTime2());
//        cv.put("key", contact.getCar_key() == null? "" :contact.getCar_key());
//        cv.put("isVip", contact.getisVip() == null? "" :contact.getisVip());
//        cv.put("carId", contact.getCar_id() == null? "" :contact.getCar_id());
//
//        cv.put("safecompany3", contact.getSafecompany3() == null? "" :contact.getSafecompany3());
//        cv.put("safenexttime3", contact.getSafenexttime3() == null? "" :contact.getSafenexttime3());
//        cv.put("tqTime3", contact.getTqTime3() == null? "" :contact.getTqTime3());
//        cv.put("safetiptime3", contact.getSafetiptime3() == null? "" :contact.getSafetiptime3());
//
//        db.insert("contact",null,cv);
//
//    }
//    public  static  boolean deleteContact(Contact contact){
//        Log.e(TAG,"deleteContact:"+LoggerUtil.jsonFromObject(contact));
//        SQLiteDatabase db = instance.getWritableDatabase();
//        db.delete("contact", "tel=?", new String[]{contact.getTel()});
//        db.close();
//        return true;
//    }
//    public  static  void updateContact(Contact contact){
//        Log.e(TAG,"updateContact:"+LoggerUtil.jsonFromObject(contact));
//        SQLiteDatabase db = instance.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put("carcode", contact.getCarCode().length() == 0? "" :contact.getCarCode() );
//
//        cv.put("name",contact.getName().length() == 0? "" :contact.getName() );
//
//        cv.put("cartype", contact.getCarType().length() == 0? "" :contact.getCarType() );
//
//        cv.put("owner",contact.getOwner().length() == 0? "" :contact.getOwner() );
//
//        cv.put("idfromnode", contact.getIdfromnode().length() == 0? "" :contact.getIdfromnode() );
//
//        cv.put("isbindweixin", contact.getIsbindweixin().length() == 0? "0" :contact.getIsbindweixin() );
//
//        cv.put("weixinopenid",contact.getWeixinopenid().length() == 0? "" :contact.getWeixinopenid() );
//
//        cv.put("vin",contact.getVin().length() == 0? "" :contact.getVin());
//
//        cv.put("carregistertime", contact.getCarregistertime().length() == 0? "" :contact.getCarregistertime());
//
//        cv.put("headurl", contact.getHeadurl().length() == 0? "" :contact.getHeadurl());
//
//        cv.put("tel", contact.getTel().length() == 0? "" :contact.getTel());
//
//
//        cv.put("safecompany", contact.getSafecompany().length() == 0? "" :contact.getSafecompany());
//        cv.put("safenexttime", contact.getSafenexttime().length() == 0? "" :contact.getSafenexttime());
//        cv.put("yearchecknexttime", contact.getYearchecknexttime().length() == 0? "" :contact.getYearchecknexttime());
//        cv.put("tqTime1", contact.getTqTime1().length() == 0? "" :contact.getTqTime1());
//        cv.put("tqTime2", contact.getTqTime2().length() == 0? "" :contact.getTqTime2());
//        cv.put("key", contact.getCar_key() == null? "" :contact.getCar_key());
//        cv.put("isVip", contact.getisVip() == null? "" :contact.getisVip());
//        cv.put("carId", contact.getCar_id() == null? "" :contact.getCar_id());
//
//        cv.put("safecompany3", contact.getSafecompany3() == null? "" :contact.getSafecompany3());
//        cv.put("safenexttime3", contact.getSafenexttime3() == null? "" :contact.getSafenexttime3());
//        cv.put("tqTime3", contact.getTqTime3() == null? "" :contact.getTqTime3());
//        cv.put("safetiptime3", contact.getSafetiptime3() == null? "" :contact.getSafetiptime3());
//
//
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

