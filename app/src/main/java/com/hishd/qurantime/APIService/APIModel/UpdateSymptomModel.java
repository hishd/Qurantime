package com.hishd.qurantime.APIService.APIModel;

import java.util.ArrayList;

public class UpdateSymptomModel {
    private String nicNo;
    private ArrayList<SymptomModel> symptoms;

    public UpdateSymptomModel(String nicNo, ArrayList<SymptomModel> symptoms) {
        this.nicNo = nicNo;
        this.symptoms = symptoms;
    }

    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    public ArrayList<SymptomModel> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<SymptomModel> symptoms) {
        this.symptoms = symptoms;
    }
}
