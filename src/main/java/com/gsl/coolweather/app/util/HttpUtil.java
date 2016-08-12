package com.gsl.coolweather.app.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by guosenlin on 16-8-10.
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String ENCODE = "UTF-8";


    public static String get(String url, Map<String, String> data, Map<String, String> headers) {
        return sendHttpRequest(GET, url, data, headers);
    }

    public static String get(String url) {
        return sendHttpRequest(GET, url, null, null);
    }

    public static void get(final String url, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = HttpUtil.get(url);
                    if(null!=listener) {
                        listener.onFinish(response);
                    }
                } catch (Exception e) {
                    if(null!=listener) {
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }

    public static String post(String url, Map<String, String> data, Map<String, String> headers) {
        return sendHttpRequest(POST, url, data, headers);
    }

    public static String post(String url) {
        return sendHttpRequest(POST, url, null, null);
    }

    public static String sendHttpRequest(String method, String url, Map<String, String> data, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getConnection(parseDataToUrl(url, data), method, headers);

            return receiveData(conn.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if(null!=conn) {
                conn.disconnect();
            }
        }
    }

    private static String receiveData(InputStream inputStream){
        Log.d(TAG, "------------------------------------receiveData---------------------------------");
        if(null==inputStream) {
            throw new NullPointerException("inputStream is null");
        }
        BufferedReader br = null;
        StringBuffer response = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if(br!=null) {
                try {br.close();} catch (Exception e) {e.printStackTrace();}
            }
        }

        return response.toString();
    }

    private static HttpURLConnection getConnection(String url, String method, Map<String, String> headers) throws Exception {
        Log.d(TAG, "------------------------------------getConnection---------------------------------");
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setRequestMethod(method);

        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        conn.setDoInput(true);
        //conn.setDoOutput(true);

        //Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

        if (null != headers) {
            for (Map.Entry e : headers.entrySet()) {
                String k = (String) e.getKey();
                String v = (String) e.getValue();

                if (null != v) {
                    conn.setRequestProperty(k, v);
                }
            }
        }

        return conn;
    }

    private static String parseDataToUrl(String url, Map<String, String> data) {
        if(data==null || data.isEmpty()) {
            return url;
        }
        String append = "";
        for(String k : data.keySet()) {
            String v = data.get(k);
            if(null!=v) {
                try{append += "&" + k + "=" + URLEncoder.encode(v, ENCODE);} catch (UnsupportedEncodingException e) {e.printStackTrace();}
            }
        }

        if(null!=url) {
            if (url.indexOf("?") != -1) {
                return url + append;
            } else {
                return url + "?" + append.substring(1);
            }
        }

        return url;
    }
}
