package com.hishd.qurantime.Model;

public class LastMeasurementsModel {
    private double lastSPo2;
    private double lastHR;
    private double healthStatus;

    public LastMeasurementsModel(double lastSPo2, double lastHR, double healthStatus) {
        this.lastSPo2 = lastSPo2;
        this.lastHR = lastHR;
        this.healthStatus = healthStatus;
    }

    public double getLastSPo2() {
        return lastSPo2;
    }

    public void setLastSPo2(double lastSPo2) {
        this.lastSPo2 = lastSPo2;
    }

    public double getLastHR() {
        return lastHR;
    }

    public void setLastHR(double lastHR) {
        this.lastHR = lastHR;
    }

    public double getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(double healthStatus) {
        this.healthStatus = healthStatus;
    }
}
