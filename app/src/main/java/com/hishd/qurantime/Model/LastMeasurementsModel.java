package com.hishd.qurantime.Model;

import java.util.Date;

public class LastMeasurementsModel {
    private double lastSPo2;
    private double lastHR;
    private String healthStatus;
    private Date lastUpdate;

    public LastMeasurementsModel(double lastSPo2, double lastHR, String healthStatus, Date lastUpdate) {
        this.lastSPo2 = lastSPo2;
        this.lastHR = lastHR;
        this.healthStatus = healthStatus;
        this.lastUpdate = lastUpdate;
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

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
