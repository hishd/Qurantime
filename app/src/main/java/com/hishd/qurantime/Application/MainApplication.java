package com.hishd.qurantime.Application;

import android.app.Application;
import android.content.Context;

import com.dasbikash.android_network_monitor.NetworkMonitor;
import com.hishd.qurantime.Util.AppConfig;
import com.hishd.qurantime.Util.LocaleHelper;

public class MainApplication extends Application {

    static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        NetworkMonitor.init(this);
        if(AppConfig.getInstance().getLanguage() == null) {
            LocaleHelper.setLocale(this, "en");
        } else {
            LocaleHelper.setLocale(this, AppConfig.getInstance().getLanguage());
        }
    }

    public static Context getContext() {
        return applicationContext;
    }
}
