package com.hishd.qurantime.Activity.Officer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityOfficerHomeBinding;

public class OfficerHomeActivity extends AppCompatActivity {

    ActivityOfficerHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}