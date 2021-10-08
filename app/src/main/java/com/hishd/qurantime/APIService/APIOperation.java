package com.hishd.qurantime.APIService;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dasbikash.android_network_monitor.NetworkMonitor;
import com.google.gson.Gson;
import com.hishd.qurantime.APIService.APIModel.UpdateHealthStatusModel;
import com.hishd.qurantime.Application.MainApplication;
import com.hishd.qurantime.Model.AreaOverviewModel;
import com.hishd.qurantime.Model.HospitalModel;
import com.hishd.qurantime.Model.MeasurementHistoryRecordModel;
import com.hishd.qurantime.Model.PatientModel;
import com.hishd.qurantime.APIService.APIModel.PatientRegistrationModel;
import com.hishd.qurantime.APIService.APIModel.UpdateSymptomModel;
import com.hishd.qurantime.Model.UserModel;
import com.hishd.qurantime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class APIOperation {
    //Singleton Instance
    private static final APIOperation instance = new APIOperation();
    private final String TAG = "API_Operation";
    private RequestQueue requestQueue;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private Gson gson;
    private final Context context;

    //Singleton Constructor
    private APIOperation() {
        this.context = MainApplication.getContext();
        this.gson = new Gson();
    }

    //Singleton request instance
    public static APIOperation getInstance() {
        return instance;
    }

    /**
     * Checks for a available internet connection
     * @return returns the connected status
     * @param callback
     */
    private boolean checkConnection(OnAPIResultCallback callback) {
        if(!NetworkMonitor.isConnected())
            callback.onConnectionLost(context.getResources().getString(R.string.connection_lost));
        return NetworkMonitor.isConnected();
    }

    public void getAvailableHospitals(String cityID, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URLEndpoints.getAllHospitals, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final ArrayList<HospitalModel> hospitals = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        hospitals.add(gson.fromJson(jsonObject.toString(), HospitalModel.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                    }
                }
                callback.onHospitalsLoaded(hospitals);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.cityID, cityID);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonArrayRequest);
    }

    public void userSignIn(USER_TYPE type, String emailAddress, String password, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        final String requestURL = type == USER_TYPE.OFFICER ? URLEndpoints.officerSignIn : URLEndpoints.patientSignIn;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final UserModel user = gson.fromJson(response.toString(), UserModel.class);
                callback.onSignInSuccessful(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.user_not_found_error));
                else if (error.networkResponse.statusCode == 401)
                    callback.onOperationFailed(context.getResources().getString(R.string.invalid_credentials_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.email, emailAddress);
                params.put(Keys.OfficerKeys.password, password);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void getOTP(USER_TYPE type, String emailAddress, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        final String requestURL = type == USER_TYPE.OFFICER ? URLEndpoints.officerGetOTP : URLEndpoints.patientGetOTP;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onOTPReceived(response.getString("otp"), response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.user_not_found_error));
                else if (error.networkResponse.statusCode == 401)
                    callback.onOperationFailed(context.getResources().getString(R.string.invalid_credentials_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.email, emailAddress);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void updateProfile(USER_TYPE type, String nicNo, String fullName, String contactNo, String emailAddress, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        final String requestURL = type == USER_TYPE.OFFICER ? URLEndpoints.officerUpdateProfile : URLEndpoints.patientUpdateProfile;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onProfileUpdated(response.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.user_not_found_error));
                else if (error.networkResponse.statusCode == 401)
                    callback.onOperationFailed(context.getResources().getString(R.string.invalid_credentials_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.nicNo, nicNo);
                params.put(Keys.OfficerKeys.emailAddress, emailAddress);
                params.put(Keys.OfficerKeys.fullName, fullName);
                params.put(Keys.OfficerKeys.contactNo, contactNo);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void updatePassword(USER_TYPE type, String emailAddress, String password, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        final String requestURL = type == USER_TYPE.OFFICER ? URLEndpoints.officerUpdatePassword : URLEndpoints.patientUpdatePassword;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onPasswordUpdated(response.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.user_not_found_error));
                else if (error.networkResponse.statusCode == 401)
                    callback.onOperationFailed(context.getResources().getString(R.string.invalid_credentials_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.email, emailAddress);
                params.put(Keys.OfficerKeys.password, password);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void getPatientMeasurementHistory(String nicNo, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URLEndpoints.patientGetMeasurementHistory, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final ArrayList<MeasurementHistoryRecordModel> records = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        records.add(gson.fromJson(jsonObject.toString(), MeasurementHistoryRecordModel.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                    }
                }
                callback.onPatientHistoryLoaded(records);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.PatientKeys.nicNo, nicNo);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonArrayRequest);
    }

    public void patientUpdateSymptoms(UpdateSymptomModel symptoms, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URLEndpoints.patientUpdateSymptoms, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSymptomsUpdated(response.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                return gson.toJson(symptoms).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void patientUpdateHealthStatus(UpdateHealthStatusModel updateHealthStatusModel, OnAPIResultCallback callback) {
        if (!NetworkMonitor.isConnected()) {
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URLEndpoints.patientUpdateHealthStatus, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onHealthStatusUpdated(response.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                return gson.toJson(updateHealthStatusModel).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void officerGetAreaOverview(String cityID, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLEndpoints.officerGetAreaOverview, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final AreaOverviewModel overviewModel = gson.fromJson(response.toString(), AreaOverviewModel.class);
                callback.onAreaOverviewLoaded(overviewModel);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.cityID, cityID);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void officerRegisterPatient(PatientRegistrationModel registrationModel, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLEndpoints.officerRegisterPatient, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onPatientRegistered(response.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));

            }
        }) {
            @Override
            public byte[] getBody() {
                return gson.toJson(registrationModel).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void officerSearchPatients(String cityID, String nicNo, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URLEndpoints.officerSearchPatient, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final ArrayList<PatientModel> patients = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        patients.add(gson.fromJson(jsonObject.toString(), PatientModel.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                    }
                }
                callback.onPatientInfoLoaded(patients);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.nicNo, nicNo);
                params.put(Keys.OfficerKeys.cityID, cityID);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonArrayRequest);
    }

    public void officerDeletePatient(String nicNo, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLEndpoints.officerDeletePatient, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onPatientRemoved(response.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.nicNo, nicNo);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonObjectRequest);
    }

    public void officerFilterPatient(String cityID, String healthStatus, String currentCondition, OnAPIResultCallback callback) {
        if (!checkConnection(callback)) {
            return;
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URLEndpoints.officerFilterPatient, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final ArrayList<PatientModel> patients = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        patients.add(gson.fromJson(jsonObject.toString(), PatientModel.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onOperationFailed(context.getResources().getString(R.string.application_error));
                    }
                }
                callback.onPatientInfoLoaded(patients);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    callback.onOperationFailed(context.getResources().getString(R.string.operation_timeout_error));
                    return;
                }
                Log.e(TAG, String.format(Locale.ENGLISH, "Response code : %d", error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 404)
                    callback.onOperationFailed(context.getResources().getString(R.string.no_records_error));
                else if (error.networkResponse.statusCode == 400)
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
                else
                    callback.onOperationFailed(context.getResources().getString(R.string.server_error));
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put(Keys.OfficerKeys.healthStatus, healthStatus);
                params.put(Keys.OfficerKeys.currentCondition, currentCondition);
                params.put(Keys.OfficerKeys.cityID, cityID);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(jsonArrayRequest);
    }

    public interface OnAPIResultCallback {
        default void onSignInSuccessful(UserModel userModel) {}
        default void onOTPReceived(String otp, String email){}
        default void onPasswordUpdated(String message){}
        default void onAreaOverviewLoaded(AreaOverviewModel areaOverview){}
        default void onPatientRegistered(String message){}
        default void onPatientRemoved(String message){}
        default void onPatientInfoLoaded(ArrayList<PatientModel> patientList){}
        default void onProfileUpdated(String message){}
        default void onSymptomsUpdated(String message){}
        default void onHealthStatusUpdated(String message){}
        default void onPatientHistoryLoaded(ArrayList<MeasurementHistoryRecordModel> lastMeasurements){}
        default void onHospitalsLoaded(ArrayList<HospitalModel> hospitals) {}
        //Non exceptional override methods
        void onOperationFailed(String error);
        void onConnectionLost(String message);
    }

    public enum USER_TYPE {
        OFFICER, PATIENT
    }
}
