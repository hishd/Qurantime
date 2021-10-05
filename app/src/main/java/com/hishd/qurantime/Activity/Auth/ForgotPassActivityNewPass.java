package com.hishd.qurantime.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.databinding.ActivityForgotPassNewPassBinding;

public class ForgotPassActivityNewPass extends AppCompatActivity {

    ActivityForgotPassNewPassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassNewPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}