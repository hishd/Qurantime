package com.hishd.qurantime.Util;

import com.google.gson.Gson;
import com.hishd.lightdb.LightDB;
import com.hishd.qurantime.Application.MainApplication;
import com.hishd.qurantime.Model.CurrentConditionModel;
import com.hishd.qurantime.Model.LastMeasurementsModel;
import com.hishd.qurantime.Model.UserModel;

public class AppConfig {
    private Gson gson;
    private LightDB lightDB;

    private static final AppConfig instance = new AppConfig();
    private final String TAG = "APP_CONFIG";

    private AppConfig() {
    }

    //Initialize the lightDB and GSON
    private void initResources() {
        if(lightDB == null) {
            lightDB = LightDB.getInstance(Constraints.DB_NAME, MainApplication.getContext());
        }
        if(gson == null) {
            gson = new Gson();
        }
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public void saveLastMeasurements(LastMeasurementsModel measurements) {
        initResources();
        lightDB.removeValue(Constraints.LAST_MEASUREMENTS);
        lightDB.saveString(Constraints.LAST_MEASUREMENTS, gson.toJson(measurements));
    }

    public LastMeasurementsModel getLastMeasurements() {
        initResources();
        if(lightDB.getString(Constraints.LAST_MEASUREMENTS) == null) {
            return null;
        }

        return gson.fromJson(lightDB.getString(Constraints.LAST_MEASUREMENTS), LastMeasurementsModel.class);
    }

    public void saveCurrentCondition(CurrentConditionModel currentCondition) {
        initResources();
        lightDB.removeValue(Constraints.CURRENT_CONDITION);
        lightDB.saveString(Constraints.CURRENT_CONDITION, gson.toJson(currentCondition));
    }

    public CurrentConditionModel getLastCurrentCondition() {
        initResources();
        if(lightDB.getString(Constraints.CURRENT_CONDITION) == null) {
            return null;
        }

        return gson.fromJson(lightDB.getString(Constraints.CURRENT_CONDITION), CurrentConditionModel.class);
    }

    public void saveUserConfig(UserModel user, boolean isOfficer) {
        initResources();
        lightDB.removeValue(Constraints.USER_CONFIG);
        lightDB.saveString(Constraints.USER_CONFIG, gson.toJson(user));
        lightDB.saveBoolean(Constraints.USERTYPE_OFFICER, isOfficer);
        setUserLogged(true);
    }

    public UserModel getUserConfig() {
        initResources();
        if(lightDB.getString(Constraints.USER_CONFIG) == null) {
            return null;
        }

        return gson.fromJson(lightDB.getString(Constraints.USER_CONFIG), UserModel.class);
    }

    public void clearUserConfig() {
        initResources();
        lightDB.removeAll();
    }

    public void setUserLogged(boolean status) {
        initResources();
        lightDB.saveBoolean(Constraints.USER_LOGGED, status);
    }

    public boolean getUserLogged() {
        initResources();
        return lightDB.getBoolean(Constraints.USER_LOGGED);
    }

    public void setInto(boolean status) {
        initResources();
        lightDB.saveBoolean(Constraints.SHOW_APP_INTRO, status);
    }

    public boolean getIntroStatus() {
        initResources();
        return lightDB.getBoolean(Constraints.SHOW_APP_INTRO);
    }

    public void setMeasureInto(boolean status) {
        initResources();
        lightDB.saveBoolean(Constraints.SHOW_MEASURE_INTRO, status);
    }

    public boolean getMeasureIntroStatus() {
        initResources();
        return lightDB.getBoolean(Constraints.SHOW_MEASURE_INTRO);
    }

    public boolean isOfficerType() {
        initResources();
        return lightDB.getBoolean(Constraints.USERTYPE_OFFICER);
    }

    public void setLanguage(String language) {
        initResources();
        lightDB.saveString(Constraints.APPLICATION_LANGUAGE, language);
    }

    public String getLanguage(){
        initResources();
        return lightDB.getString(Constraints.APPLICATION_LANGUAGE);
    }

}
