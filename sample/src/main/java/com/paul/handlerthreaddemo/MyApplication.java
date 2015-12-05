package com.paul.handlerthreaddemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Paul Chang on 2015/12/4.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
