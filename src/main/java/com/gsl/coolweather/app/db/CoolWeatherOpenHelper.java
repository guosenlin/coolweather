package com.gsl.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by guosenlin on 16-8-6.
 */

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "CoolWeatherOpenHelper";

    private static final String CRAETE_TABLE_PROVINCE = "create table province ("
            + "id integer primary key autoincrement,"
            + "province_name text,"
            + "province_code text"
            + ")";

    private static final String CRAETE_TABLE_CITY = "create table city ("
            + "id integer primary key autoincrement,"
            + "city_name text,"
            + "city_code text,"
            + "province_id integer"
            + ")";

    private static final String CRAETE_TABLE_COUNTY = "create table county ("
            + "id integer primary key autoincrement,"
            + "county_name text,"
            + "county_code text,"
            + "city_id integer"
            + ")";


    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CRAETE_TABLE_PROVINCE);
        sqLiteDatabase.execSQL(CRAETE_TABLE_CITY);
        sqLiteDatabase.execSQL(CRAETE_TABLE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "------------------------->>to upgrade " + newVersion);
    }
}
