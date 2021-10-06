package com.hishd.qurantime.Model;

import io.realm.RealmObject;

public class HospitalModel extends RealmObject {
    private String _id;
    private String hospitalName;

    public HospitalModel() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
