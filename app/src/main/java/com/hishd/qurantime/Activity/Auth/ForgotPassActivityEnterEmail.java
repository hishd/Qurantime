package com.hishd.qurantime.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityForgotPassEnterEmailBinding;

public class ForgotPassActivityEnterEmail extends AppCompatActivity {

    ActivityForgotPassEnterEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassEnterEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}