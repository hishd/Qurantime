package com.hishd.qurantime.APIService.APIModel;

import com.hishd.qurantime.Model.CachedHealthStatusModel;

import java.util.ArrayList;
import java.util.List;

public class UpdateHealthStatusModel {
    private String nicNo;
    private List<HealthStatusModel> data;

    public UpdateHealthStatusModel(String nicNo, List<HealthStatusModel> data) {
        this.nicNo = nicNo;
        this.data = data;
    }

    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    public List<HealthStatusModel> getData() {
        return data;
    }

    public void setData(List<HealthStatusModel> data) {
        this.data = data;
    }
}
