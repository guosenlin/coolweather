package com.gsl.coolweather.app.util;

/**
 * Created by guosenlin on 16-8-12.
 */

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
