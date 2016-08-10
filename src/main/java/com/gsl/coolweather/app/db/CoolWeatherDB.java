package com.gsl.coolweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gsl.coolweather.app.model.City;
import com.gsl.coolweather.app.model.County;
import com.gsl.coolweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guosenlin on 16-8-10.
 */
public class CoolWeatherDB {
    private static final String DATABASE_NAME = "coolweather_database_name";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private static CoolWeatherDB ourInstance;

    public static synchronized CoolWeatherDB getInstance(Context context) {
        if(null==ourInstance) {
            ourInstance = new CoolWeatherDB(context);
        }
        return ourInstance;
    }

    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper helper = new CoolWeatherOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = helper.getWritableDatabase();
    }

    public boolean saveProvince(String name, String code) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("code", code);
        return db.insert("province", null, values)==1;
    }

    public boolean saveCity(String name, String code, int province_id) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("code", code);
        values.put("province_id", province_id);
        return db.insert("city", null, values)==1;
    }

    public boolean saveCounty(String name, String code, int city_id) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("code", code);
        values.put("city_id", city_id);
        return db.insert("county", null, values)==1;
    }

    public Province findProvinceById(String id) {
        String sql = "SELECT * FROM province WHERE id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});

        Province p = null;
        if(cursor.moveToFirst()) {
            p = new Province();
            while (cursor.moveToNext()) {
                p.setId(cursor.getInt(cursor.getColumnIndex("id")));
                p.setName(cursor.getString(cursor.getColumnIndex("province＿name")));
                p.setCode(cursor.getString(cursor.getColumnIndex("province＿code")));
            }
        }
        return p;
    }

    public City findCityById(String id) {
        String sql = "SELECT * FROM city WHERE id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});

        City city = null;
        if(cursor.moveToFirst()) {
            city = new City();
            while (cursor.moveToNext()) {
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setName(cursor.getString(cursor.getColumnIndex("city＿name")));
                city.setCode(cursor.getString(cursor.getColumnIndex("city＿code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
            }
        }
        return city;
    }

    public County findCountyById(String id) {
        String sql = "SELECT * FROM county WHERE id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});

        County county = null;
        if(cursor.moveToFirst()) {
            county = new County();
            while (cursor.moveToNext()) {
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setName(cursor.getString(cursor.getColumnIndex("county＿name")));
                county.setCode(cursor.getString(cursor.getColumnIndex("county＿code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
            }
        }
        return county;
    }

    public List<City> findListCityByProvinceId(String id) {
        List<City> list = new ArrayList<>();
        String sql = "SELECT * FROM city WHERE province_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()) {
            while(cursor.moveToNext()) {
                list.add(new City(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("city_name")),
                    cursor.getString(cursor.getColumnIndex("city_code")),
                    cursor.getInt(cursor.getColumnIndex("province_id"))
                ));
            }
        }

        return list;
    }

    public List<County> findListCountyByCityId(String id) {
        List<County> list = new ArrayList<>();
        String sql = "SELECT * FROM county WHERE city_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()) {
            while(cursor.moveToNext()) {
                list.add(new County(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("county_name")),
                        cursor.getString(cursor.getColumnIndex("county_code")),
                        cursor.getInt(cursor.getColumnIndex("city_id"))
                ));
            }
        }

        return list;
    }

}
