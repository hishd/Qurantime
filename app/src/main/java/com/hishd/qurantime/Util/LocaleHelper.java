package com.hishd.qurantime.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {
    // the method is used to set the language at runtime
    public static void setLocale(Context context, String language) {
        persist(language);
        updateResources(context, language);
    }

    private static void persist(String language) {
        AppConfig.getInstance().setLanguage(language);
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        configuration.setLayoutDirection(locale);
    }

}
