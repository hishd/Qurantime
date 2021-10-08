package com.hishd.qurantime.APIService.APIModel;

public class HealthStatusModel {
    private String spo2Level;
    private String bpmLevel;

    public HealthStatusModel(String spo2Level, String bpmLevel) {
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
