package com.hishd.qurantime.APIService;

import com.hishd.qurantime.BuildConfig;

public final class URLEndpoints {
    //Patient Endpoints
    public static final String patientSignIn = BuildConfig.API_URL + "/patient/signIn";
    public static final String patientGetMeasurementHistory = BuildConfig.API_URL + "/patient/health";
    public static final String patientGetOTP = BuildConfig.API_URL + "/patient/getOtp";
    public static final String patientUpdatePassword = BuildConfig.API_URL + "/patient/password";
    public static final String patientUpdateProfile= BuildConfig.API_URL + "/patient/profile";
    public static final String patientUpdateSymptoms= BuildConfig.API_URL + "/patient/symptoms";
    public static final String patientUpdateHealthStatus= BuildConfig.API_URL + "/patient/health";

    //Officer Endpoints
    public static final String officerSignIn = BuildConfig.API_URL + "/officer/signIn";
    public static final String officerGetOTP = BuildConfig.API_URL + "/officer/getOtp";
    public static final String officerUpdatePassword = BuildConfig.API_URL + "/officer/password";
    public static final String officerGetAreaOverview = BuildConfig.API_URL + "/officer/areaStat";
    public static final String officerRegisterPatient = BuildConfig.API_URL + "/officer/patient";
    public static final String officerSearchPatient = BuildConfig.API_URL + "/officer/patient";
    public static final String officerDeletePatient = BuildConfig.API_URL + "/officer/patient";
    public static final String officerFilterPatient = BuildConfig.API_URL + "/officer/filter";
    public static final String officerUpdateProfile= BuildConfig.API_URL + "/officer/profile";
}
