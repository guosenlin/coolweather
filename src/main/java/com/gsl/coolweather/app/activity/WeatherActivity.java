package com.gsl.coolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsl.coolweather.app.R;
import com.gsl.coolweather.app.util.CoolWeacherUtility;
import com.gsl.coolweather.app.util.HttpCallbackListener;
import com.gsl.coolweather.app.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class WeatherActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "WeatherActivity";
    private LinearLayout weatherInfoLayout;
    private TextView tvCityName;
    private  TextView tvPublish;
    private  TextView tvCurrentDate;
    private  TextView tvCurrentDesc;
    private  TextView tvTemp1;
    private  TextView tvTemp2;
    private Button btSwitchCity;
    private  TextView btRefreshWeather;

    private final String WEATHER_URL_BASE = "http://www.weather.com.cn/data/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout = (LinearLayout) findViewById(R.id.ll_weather_info);
        tvCityName = (TextView) findViewById(R.id.tv_city_name);
        tvPublish = (TextView) findViewById(R.id.tv_publish);
        tvCurrentDate = (TextView) findViewById(R.id.tv_current_date);
        tvCurrentDesc = (TextView) findViewById(R.id.tv_current_desc);
        tvTemp1 = (TextView) findViewById(R.id.tv_temp1);
        tvTemp2 = (TextView) findViewById(R.id.tv_temp2);
        btSwitchCity = (Button) findViewById(R.id.bt_switch_city);
        btRefreshWeather = (Button) findViewById(R.id.bt_refresh);

        btSwitchCity.setOnClickListener(this);
        btRefreshWeather.setOnClickListener(this);

        String countyCode = getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)) {
            tvPublish.setText("loading...");
            tvCityName.setVisibility(View.INVISIBLE);
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            showWeather();
        }
    }

    private void showWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        tvCityName.setText(sp.getString("city_name", ""));
        tvPublish.setText("发布于　" + sp.getString("publish_time", ""));
        tvCurrentDate.setText(sp.getString("current_date", ""));
        tvCurrentDesc.setText(sp.getString("weather_desc", ""));
        tvTemp1.setText(sp.getString("temp1", ""));
        tvTemp2.setText(sp.getString("temp2", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        tvCityName.setVisibility(View.VISIBLE);
    }

    private void queryWeatherCode(String countyCode) {
        String url = WEATHER_URL_BASE + "list3/city" + countyCode + ".xml";
        queryFromServer(url, "countyCode");
    }

    private void queryWeatherInfo(String weatherCode) {
        String url = WEATHER_URL_BASE + "cityinfo/" + weatherCode + ".html";
        queryFromServer(url, "weatherCode");
    }

    private void queryFromServer(final String url, final String codeType) {
        Log.d(TAG, codeType + "---->>>" + url);

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Encoding", "");

        HttpUtil.get(url, null, headers, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d(TAG, "------------onFinish---------->>"+response);
                if(TextUtils.equals("countyCode", codeType)) {
                    if(!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|", -1);
                        if(null!=array && array.length==2) {
                            queryWeatherInfo(array[1]);
                        }
                    }
                } else if(TextUtils.equals("weatherCode", codeType)) {
                    CoolWeacherUtility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "------------onError---------->>"+e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvPublish.setText("failed to load");
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_refresh :
                tvPublish.setText("loading....");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = sp.getString("weather_code", "");
                if(!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherInfo(weatherCode);
                }
                break;
            case R.id.bt_switch_city :
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
