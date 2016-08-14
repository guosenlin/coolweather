package com.gsl.coolweather.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsl.coolweather.app.R;
import com.gsl.coolweather.app.db.CoolWeatherDB;
import com.gsl.coolweather.app.model.City;
import com.gsl.coolweather.app.model.County;
import com.gsl.coolweather.app.model.Province;
import com.gsl.coolweather.app.util.CoolWeacherUtility;
import com.gsl.coolweather.app.util.HttpCallbackListener;
import com.gsl.coolweather.app.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends Activity implements AdapterView.OnItemClickListener {
    private  static final String TAG = "ChooseAreaActivity";

    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView tvTitle;
    private ListView lvAreaChoose;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<String>();
    private CoolWeatherDB db;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    private static final String WEATHER_URL_BASE = "http://www.weather.com.cn/data/list3/";
    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getBoolean("city_selected", false) && !isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        lvAreaChoose = (ListView) findViewById(R.id.lv_choose_area);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        lvAreaChoose.setAdapter(adapter);
        db = CoolWeatherDB.getInstance(this);

        lvAreaChoose.setOnItemClickListener(this);
        queryProvince();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(currentLevel == LEVEL_PROVINCE) {
            selectedProvince = provinceList.get(i);
            queryCity();
        } else if(currentLevel == LEVEL_CITY) {
            selectedCity = cityList.get(i);
            queryCounty();
        } else if(currentLevel == LEVEL_COUNTY) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("county_code", countyList.get(i).getCode());
            startActivity(intent);
            finish();
        }
    }

    private void queryProvince() {
        provinceList = db.findListProvince();
        if(provinceList.size()>0) {
            dataList.clear();
            for(Province p : provinceList) {
                dataList.add(p.getName());
            }

            lvAreaChoose.setSelection(0);
            adapter.notifyDataSetChanged();

            tvTitle.setText("中国");
            currentLevel = LEVEL_PROVINCE;

        } else {
            queryFromServer(null, "province");
        }
    }

    private void queryCity() {
        cityList = db.findListCityByProvinceId(String.valueOf(selectedProvince.getId()));
        if(cityList.size()>0) {
            dataList.clear();
            for(City c : cityList) {
                dataList.add(c.getName());
            }

            lvAreaChoose.setSelection(0);
            adapter.notifyDataSetChanged();

            tvTitle.setText(selectedProvince.getName());
            currentLevel = LEVEL_CITY;

        } else {
            queryFromServer(selectedProvince.getCode(), "city");
        }
    }

    private void queryCounty() {
        countyList = db.findListCountyByCityId(String.valueOf(selectedCity.getId()));
        if(countyList.size()>0) {
            dataList.clear();
            for(County c : countyList) {
                dataList.add(c.getName());
            }

            lvAreaChoose.setSelection(0);
            adapter.notifyDataSetChanged();

            tvTitle.setText(selectedCity.getName());
            currentLevel = LEVEL_COUNTY;

        } else {
            queryFromServer(selectedCity.getCode(), "county");
        }
    }

    private void queryFromServer(String code, final String type) {
        String url = WEATHER_URL_BASE;
        if(!TextUtils.isEmpty(code)) {
            url += "city" + code + ".xml";
        } else {
            url += "city.xml";
        }

        showProgressDialog();
        Log.d(TAG, "URL to get weather data:"+url);

        HttpUtil.get(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d(TAG, "----------------------------------onFinish------------------------------");
                Log.d(TAG, response);
                boolean success = false;
                if("province".equals(type)) {
                    success = CoolWeacherUtility.parseProvinceDataIntoDatabase(db, response);
                } else if("city".equals(type)) {
                    success = CoolWeacherUtility.parseCityDataIntoDatabase(db, response, selectedProvince.getId());
                } else if("county".equals(type)) {
                    success = CoolWeacherUtility.parseCountyDataIntoDatabase(db, response, selectedCity.getId());
                }
                if(success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)) {
                                queryProvince();
                            } else if("city".equals(type)) {
                                queryCity();
                            } else if("county".equals(type)) {
                                queryCounty();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "----------------------------------onError------------------------------");
                Log.e(TAG, e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "failed to load data", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if(null==progressDialog) {
            progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if(null!=progressDialog) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_PROVINCE) {
            if(isFromWeatherActivity) {
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        } else if(currentLevel == LEVEL_CITY) {
            queryProvince();
        } else if(currentLevel == LEVEL_COUNTY) {
            queryCity();
        }
    }
}
