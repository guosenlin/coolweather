package com.gsl.coolweather.app;

import com.gsl.coolweather.app.util.CoolWeacherUtility;
import com.gsl.coolweather.app.util.HttpCallbackListener;
import com.gsl.coolweather.app.util.HttpUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getProvinceFromServer() {
        //System.out.println(HttpUtil.get("http://www.weather.com.cn/data/list3/city.xml"));
        HttpUtil.get("http://www.weather.com.cn/data/list3/city.xml", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                System.out.println("----------------------------------onFinish------------------------------");
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("----------------------------------onError------------------------------");
                System.err.println(e.getMessage());
            }
        });

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseProvinceData() {
        String data = "01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,11|陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳门,34|台湾";
        CoolWeacherUtility.parseProvinceDataIntoDatabase(null, data);
    }
}