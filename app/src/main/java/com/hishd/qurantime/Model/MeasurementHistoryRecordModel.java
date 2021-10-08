package com.hishd.qurantime.Model;

public class MeasurementHistoryRecordModel {
    private double spo2Level;
    private double bpmLevel;
    private String time;
    private String result;

    public double getSpo2Level() {
        return spo2Level;
    }

    public void setSpo2Level(double spo2Level) {
        this.spo2Level = spo2Level;
    }

    public double getBpmLevel() {
        return bpmLevel;
    }

    public void setBpmLevel(double bpmLevel) {
        this.bpmLevel = bpmLevel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
