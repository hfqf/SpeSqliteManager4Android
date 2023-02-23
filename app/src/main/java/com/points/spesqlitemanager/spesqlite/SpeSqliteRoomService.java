package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.points.spesqlitemanager.room.AppDatabase;
import com.points.spesqlitemanager.room.NoticeModel;
import com.points.spesqlitemanager.room.ServerModel;

import org.jetbrains.annotations.NotNull;

/**
 * @author hfqf123@126.com
 * @brief description
 * @date 2023-02-23
 */
public class SpeSqliteRoomService extends  SpeSqliteAbstractDBService  {
    private static RoomDatabase roomdb;
    private static final String kRoomDBName  = "localdb2";
    public SpeSqliteRoomService(Context context) {
        super(context);
    }

    public  static SpeSqliteAbstractDBService getInstance(Context context,Class roomDatabaseClass) {
        if (instance == null){
            synchronized (SpeSqliteAbstractDBService.class){
                if (instance == null){
                    instance = new SpeSqliteRoomService(context);
                    instance.roomClass = roomDatabaseClass;
                    instance.context = context;
                    instance.db = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        roomdb = Room.databaseBuilder(context,
                instance.roomClass, kRoomDBName).addCallback(new RoomDatabase.Callback() {
            @Override
            public void onOpen(@NonNull @NotNull SupportSQLiteDatabase db) {
                super.onOpen(db);

                //测试插入代码
                ServerModel model = new ServerModel();
                model.setId("1");
                model.setHost("1");
                model.setLang("1");
                model.setName("1");
                model.setVersion("1");
                NoticeModel model2 = new NoticeModel();
                model2.setId("1");
                model2.setOrderId(2);
                model2.setTitle("1");
                insert(model,model2);
            }
        }).build();
        //必须分步骤执行，否则影响database的回调方法执行
        roomdb.getOpenHelper().getWritableDatabase();

        SpeSqliteUpdateManager.getInstance().create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        Migration newMigration = new Migration(oldVersion,newVersion) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                Log.e("SpeSqliteUpdateManager","migrate");
                SpeSqliteUpdateManager.getInstance().upgrade(db,database);
            }
        };
        roomdb = Room.databaseBuilder(context,instance.roomClass, kRoomDBName).addMigrations(newMigration).build();
        //必须分步骤执行，否则影响database的回调方法执行
        roomdb.getOpenHelper().getWritableDatabase();

        SpeSqliteUpdateManager.getInstance().upgrade(db,null);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        roomdb = Room.databaseBuilder(context,
                instance.roomClass, kRoomDBName).build();
        //必须分步骤执行，否则影响database的回调方法执行
        roomdb.getOpenHelper().getWritableDatabase();
    }

    /**
     * 临时测试方法
     * @param model
     * @param model2
     */
    private void insert(ServerModel model, NoticeModel model2) {
        new Thread(() -> {
            ((AppDatabase)roomdb).serverDao().insert(model);
            ((AppDatabase)roomdb).noticeDao().insert(model2);
        }).start();
    }

}
