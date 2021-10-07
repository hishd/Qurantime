package com.hishd.qurantime.Model;

public class WIfiScanModel {
    private String ssid;
    private boolean isPulseOx = false;

    public WIfiScanModel(String ssid, boolean isPulseOx) {
        this.ssid = ssid;
        this.isPulseOx = isPulseOx;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public boolean isPulseOx() {
        return isPulseOx;
    }

    public void setPulseOx(boolean pulseOx) {
        isPulseOx = pulseOx;
    }
}
