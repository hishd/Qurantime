package com.hishd.qurantime.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityForgotPassEnterOtpBinding;

public class ForgotPassActivityEnterOTP extends AppCompatActivity {

    ActivityForgotPassEnterOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassEnterOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}