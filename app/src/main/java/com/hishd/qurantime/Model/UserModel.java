package com.hishd.qurantime.Model;

public class UserModel {
    private String fullName;
    private String nicNo;
    private String contactNo;
    private String emailAddress;
    private String districtID;
    private String districtName;
    private String cityID;
    private String cityName;
    //Patient only resources
    private String address;
    private String hospitalID;
    private String hospitalName;

    public UserModel(String fullName, String nicNo, String contactNo, String emailAddress, String districtID, String districtName, String cityID, String cityName, String address, String hospitalID, String hospitalName) {
        this.fullName = fullName;
        this.nicNo = nicNo;
        this.contactNo = contactNo;
        this.emailAddress = emailAddress;
        this.districtID = districtID;
        this.districtName = districtName;
        this.cityID = cityID;
        this.cityName = cityName;
        this.address = address;
        this.hospitalID = hospitalID;
        this.hospitalName = hospitalName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
