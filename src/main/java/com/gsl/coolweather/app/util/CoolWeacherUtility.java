package com.gsl.coolweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gsl.coolweather.app.db.CoolWeatherDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by guosenlin on 16-8-10.
 */

public class CoolWeacherUtility {
    public static boolean parseProvinceDataIntoDatabase(CoolWeatherDB db, String data) {
        if(null!=data&&!"".equals(data)) {
            String[] plist = data.split(",", -1);
            if(null!=plist) {
                for(String pv : plist) {
                    String[] p = pv.split("\\|", -1);
                    if(null!=p&&p.length==2) {
                        db.saveProvince(p[1], p[0]);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean parseCityDataIntoDatabase(CoolWeatherDB db, String data, int provinceId) {
        if(null!=data&&!"".equals(data)) {
            String[] plist = data.split(",", -1);
            if(null!=plist) {
                for(String pv : plist) {
                    String[] p = pv.split("\\|", -1);
                    if(null!=p&&p.length==2) {
                        db.saveCity(p[1], p[0], provinceId);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean parseCountyDataIntoDatabase(CoolWeatherDB db, String data, int cityId) {
        if(null!=data&&!"".equals(data)) {
            String[] plist = data.split(",", -1);
            if(null!=plist) {
                for(String pv : plist) {
                    String[] p = pv.split("\\|", -1);
                    if(null!=p&&p.length==2) {
                        db.saveCounty(p[1], p[0], cityId);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jObj = new JSONObject(response);
            JSONObject weather = jObj.getJSONObject("weatherinfo");
            String cityName = weather.getString("city");
            String weatherCode = weather.getString("cityid");
            String temp1 = weather.getString("temp1");
            String temp2 = weather.getString("temp2");
            String weatherDesc = weather.getString("weather");
            String publishTime = weather.getString("ptime");
            saveWeatherInfoIntoSharedPreference(context, cityName, weatherCode, temp1, temp2, weatherDesc, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfoIntoSharedPreference(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesc, String publishTime) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desc", weatherDesc);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA).format(new Date()));
        editor.commit();
    }
}
