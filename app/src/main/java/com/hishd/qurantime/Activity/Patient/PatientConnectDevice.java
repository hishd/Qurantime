package com.hishd.qurantime.Activity.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityPatientConnectDeviceBinding;

public class PatientConnectDevice extends AppCompatActivity {

    ActivityPatientConnectDeviceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientConnectDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}