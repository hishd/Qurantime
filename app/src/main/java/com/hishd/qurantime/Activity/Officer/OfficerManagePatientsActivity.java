package com.hishd.qurantime.Activity.Officer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityOfficerManagePatientsBinding;

public class OfficerManagePatientsActivity extends AppCompatActivity {

    ActivityOfficerManagePatientsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerManagePatientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}