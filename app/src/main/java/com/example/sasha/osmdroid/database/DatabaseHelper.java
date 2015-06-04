package com.example.sasha.osmdroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sasha.osmdroid.types.GeoPoint;
import com.example.sasha.osmdroid.types.Guide;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by sasha on 1/26/15.
 */
public class DatabaseHelper  extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "myappname.db";

    private static final int DATABASE_VERSION = 1;

    private GuideDAO cityGuideDAO = null;
    private GeoPointDAO customGeoPointDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Guide.class);
            TableUtils.createTable(connectionSource, GeoPoint.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer){

        try{
            TableUtils.dropTable(connectionSource, Guide.class, true);
            onCreate(db, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
            throw new RuntimeException(e);
        }
    }

    public GuideDAO getGuideDAO() throws SQLException{
        if(cityGuideDAO == null){
            cityGuideDAO = new GuideDAO(getConnectionSource(), Guide.class);
        }
        return cityGuideDAO;
    }
    public GeoPointDAO getGeoPointDAO() throws SQLException{
        if(customGeoPointDAO == null){
            customGeoPointDAO = new GeoPointDAO(getConnectionSource(), GeoPoint.class);
        }
        return customGeoPointDAO;
    }

    @Override
    public void close(){
        super.close();
        cityGuideDAO = null;
    }
}
