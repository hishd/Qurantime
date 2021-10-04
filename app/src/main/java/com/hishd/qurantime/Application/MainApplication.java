package com.hishd.qurantime.Application;

import android.app.Application;
import android.content.Context;

import com.dasbikash.android_network_monitor.NetworkMonitor;

public class MainApplication extends Application {

    static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        NetworkMonitor.init(this);
    }

    public static Context getContext() {
        return applicationContext;
    }
}
