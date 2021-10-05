package com.hishd.qurantime.Activity.Officer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityOfficerProfileBinding;

public class OfficerProfileActivity extends AppCompatActivity {

    ActivityOfficerProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}