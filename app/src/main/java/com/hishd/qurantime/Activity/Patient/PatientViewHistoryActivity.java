package com.hishd.qurantime.Activity.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityPatientViewHistoryBinding;

public class PatientViewHistoryActivity extends AppCompatActivity {

    ActivityPatientViewHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientViewHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}