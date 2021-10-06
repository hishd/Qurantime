package com.hishd.qurantime.DB;

import com.hishd.qurantime.Model.HospitalModel;

import java.util.List;

import io.realm.Realm;

public class QurantimeDB {
    private final Realm realm;
    private final String TAG = "QurantimeDB";

    public QurantimeDB() {
        this.realm = Realm.getDefaultInstance();
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    public void syncHospitals(List<HospitalModel> hospitalList) {
        realm.beginTransaction();
        realm.delete(HospitalModel.class);
        realm.insert(hospitalList);
        realm.commitTransaction();
    }

    public List<HospitalModel> getHospitalList() {
        return realm.where(HospitalModel.class).findAll();
    }
}
