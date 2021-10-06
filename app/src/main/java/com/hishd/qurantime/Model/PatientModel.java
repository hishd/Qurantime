package com.hishd.qurantime.Model;

public class PatientModel {
    private String nicNo;
    private String fullName;
    private String contactNo;
    private String cityID;
    private String hospitalID;
    private String healthStatus;
    private String latestCondition;
    private String lastUpdate;

    public PatientModel(String nicNo, String fullName, String contactNo, String cityID, String hospitalID, String healthStatus, String latestCondition, String lastUpdate) {
        this.nicNo = nicNo;
        this.fullName = fullName;
        this.contactNo = contactNo;
        this.cityID = cityID;
        this.hospitalID = hospitalID;
        this.healthStatus = healthStatus;
        this.latestCondition = latestCondition;
        this.lastUpdate = lastUpdate;
    }

    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getLatestCondition() {
        return latestCondition;
    }

    public void setLatestCondition(String latestCondition) {
        this.latestCondition = latestCondition;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
