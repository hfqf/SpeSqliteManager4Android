package com.points.spesqlitemanager.spesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import org.jetbrains.annotations.NotNull;

/**
 * @author hfqf123@126.com
 * @brief 专门针对Room数据库的升级服务类,需借助SQLiteOpenHelper的生命周期方法+dbupdate.json来触发room数据库的周期管理
 * @date 2023-02-23
 */
public class SpeSqliteRoomService extends  SpeSqliteAbstractDBService  {
    private static RoomDatabase roomdb;
    /**
     * room数据库名
     */
    private static final String kRoomDBName  = "localdb2";
    public SpeSqliteRoomService(Context context) {
        super(context);
    }

    /**
     * 返回SpeSqliteRoomService单例
     * @param context context
     * @param roomDatabaseClass room数据的抽象类
     * @param listener 当调用者需要db的生命周期函数就要传值，否则直接传null
     * @return SpeSqliteRoomService
     */
    public  static SpeSqliteAbstractDBService getInstance(Context context,Class roomDatabaseClass,SpeSqliteBaseInterface listener) {
        if (instance == null){
            synchronized (SpeSqliteAbstractDBService.class){
                if (instance == null){
                    instance = new SpeSqliteRoomService(context);
                    instance.listener = listener;
                    instance.roomClass = roomDatabaseClass;
                    instance.context = context;
                    instance.db = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    /**
     * 返回SpeSqliteRoomService单例
     * @param context context
     * @param roomDatabaseClass room数据的抽象类
     * @return SpeSqliteRoomService
     */
    public  static SpeSqliteRoomService getInstance(Context context,Class roomDatabaseClass) {
        return SpeSqliteRoomService.getInstance(context,roomDatabaseClass);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        roomdb = Room.databaseBuilder(context,
                instance.roomClass, kRoomDBName).addCallback(new RoomDatabase.Callback() {
            @Override
            public void onOpen(@NonNull @NotNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        }).build();
        //必须分步骤执行，否则影响database的回调方法执行
        roomdb.getOpenHelper().getWritableDatabase();
        if(instance.listener!=null){
            instance.listener.onCreate(db,roomdb);
        }
        SpeSqliteUpdateManager.getInstance().create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        Migration newMigration = new Migration(oldVersion,newVersion) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                SpeSqliteUpdateManager.getInstance().upgrade(db,database);
                if(instance.listener!=null){
                    instance.listener.onUpgrade(database,oldVersion,newVersion,roomdb);
                }
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
        if(instance.listener!=null){
            instance.listener.onOpen(db,roomdb);
        }
    }
}
