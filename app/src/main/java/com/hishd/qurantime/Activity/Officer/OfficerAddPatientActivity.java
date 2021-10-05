package com.hishd.qurantime.Activity.Officer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityOfficerAddPatientBinding;

public class OfficerAddPatientActivity extends AppCompatActivity {

    ActivityOfficerAddPatientBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerAddPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}