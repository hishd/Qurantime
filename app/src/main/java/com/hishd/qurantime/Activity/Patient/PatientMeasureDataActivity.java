package com.hishd.qurantime.Activity.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityPatientMeasureDataBinding;

public class PatientMeasureDataActivity extends AppCompatActivity {

    ActivityPatientMeasureDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientMeasureDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}