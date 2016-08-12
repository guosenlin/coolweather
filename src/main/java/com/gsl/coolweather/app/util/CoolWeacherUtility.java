package com.gsl.coolweather.app.util;

import com.gsl.coolweather.app.db.CoolWeatherDB;

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
}
