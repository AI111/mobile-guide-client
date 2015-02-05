package com.example.sasha.osmdroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sasha.osmdroid.types.CityGuide;
import com.example.sasha.osmdroid.types.CustomGeoPoint;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by sasha on 1/26/15.
 */
public class DatabaseHelper  extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME = "myappname.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private CityGuideDAO cityGuideDAO = null;
    private CustomGeoPointDAO customGeoPointDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CityGuide.class);
            TableUtils.createTable(connectionSource, CustomGeoPoint.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer){

        try{
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, CityGuide.class, true);
            //TableUtils.dropTable(connectionSource, Role.class, true);
            onCreate(db, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
            throw new RuntimeException(e);
        }
    }

    //синглтон для GoalDAO
    public CityGuideDAO getCityGuideDAO() throws SQLException{
        if(cityGuideDAO == null){
            cityGuideDAO = new CityGuideDAO(getConnectionSource(), CityGuide.class);
        }
        return cityGuideDAO;
    }
   // /синглтон для RoleDAO
    public CustomGeoPointDAO getCustomGeoPointDAO() throws SQLException{
        if(customGeoPointDAO == null){
            customGeoPointDAO = new CustomGeoPointDAO(getConnectionSource(), CustomGeoPoint.class);
        }
        return customGeoPointDAO;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        cityGuideDAO = null;
        //roleDao = null;
    }
}
