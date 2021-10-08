package com.hishd.qurantime.Model;

import io.realm.RealmObject;

public class CachedHealthStatusModel extends RealmObject {
    private String spo2Level;
    private String bpmLevel;

    public CachedHealthStatusModel() {
    }

    public CachedHealthStatusModel(String spo2Level, String bpmLevel) {
        this.spo2Level = spo2Level;
        this.bpmLevel = bpmLevel;
    }

    public String getSpo2Level() {
        return spo2Level;
    }

    public void setSpo2Level(String spo2Level) {
        this.spo2Level = spo2Level;
    }

    public String getBpmLevel() {
        return bpmLevel;
    }

    public void setBpmLevel(String bpmLevel) {
        this.bpmLevel = bpmLevel;
    }
}
