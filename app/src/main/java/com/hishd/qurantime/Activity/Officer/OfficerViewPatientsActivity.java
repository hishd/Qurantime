package com.hishd.qurantime.Activity.Officer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityOfficerViewPatientsBinding;

public class OfficerViewPatientsActivity extends AppCompatActivity {

    ActivityOfficerViewPatientsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerViewPatientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}