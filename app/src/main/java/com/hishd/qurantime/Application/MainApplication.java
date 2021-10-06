package com.hishd.qurantime.Application;

import android.app.Application;
import android.content.Context;

import com.dasbikash.android_network_monitor.NetworkMonitor;
import com.hishd.qurantime.DB.RealmUtility;
import com.hishd.qurantime.Util.AppConfig;
import com.hishd.qurantime.Util.LocaleHelper;

import io.realm.Realm;

public class MainApplication extends Application {

    static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        NetworkMonitor.init(this);
        Realm.init(this);
        Realm.setDefaultConfiguration(RealmUtility.getDefaultConfig());
    }

    public static Context getContext() {
        return applicationContext;
    }
}
