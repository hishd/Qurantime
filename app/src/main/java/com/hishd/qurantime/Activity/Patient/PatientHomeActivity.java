package com.hishd.qurantime.Activity.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityPatientHomeBinding;

public class PatientHomeActivity extends AppCompatActivity {

    ActivityPatientHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}