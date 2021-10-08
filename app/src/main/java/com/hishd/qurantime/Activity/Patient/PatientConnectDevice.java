package com.hishd.qurantime.Activity.Patient;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hishd.lightpopup.LightPopup;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Activity.Officer.OfficerManagePatientsActivity;
import com.hishd.qurantime.R;
import com.hishd.qurantime.databinding.ActivityPatientConnectDeviceBinding;

import spencerstudios.com.bungeelib.Bungee;

public class PatientConnectDevice extends BaseActivity {

    ActivityPatientConnectDeviceBinding binding;
    WifiManager mWifiManager;
    private final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientConnectDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void setupResources() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            displayAlert(this, AlertType.WARNING, getString(R.string.location_off), getString(R.string.location_off_caption));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    @Override
    protected void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnNext.setOnClickListener(v -> {
            WifiInfo info = mWifiManager.getConnectionInfo();
            Log.e("SSID", info.getSSID());
            if(info.getSSID().toLowerCase().contains("pulseox")) {
                startActivity(new Intent(this, PatientMeasureDataActivity.class));
                Bungee.fade(this);
                finish();
            } else {
                new LightPopup(this)
                        .createDualActionDialog()
                        .setCancelledOnOutside(false)
                        .setTitle(getResources().getString(R.string.unknown_device))
                        .setMessage(getString(R.string.unknown_device_caption))
                        .setBtn1Caption(getResources().getString(R.string.cancel))
                        .setBtn1Color(R.color.blue)
                        .setBtn1Action(Dialog::dismiss)
                        .setBtn2Caption(getResources().getString(R.string.continue_operation))
                        .setBtn2Color(R.color.red)
                        .setBtn2Action(dialog -> {
                            dialog.dismiss();
                            startActivity(new Intent(this, PatientMeasureDataActivity.class));
                            Bungee.fade(this);
                            finish();
                        }).show();
            }
        });
        binding.btnOpenWifi.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                startActivity(new Intent(Settings.Panel.ACTION_WIFI));
//            } else {
//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//            }
        });
    }
}