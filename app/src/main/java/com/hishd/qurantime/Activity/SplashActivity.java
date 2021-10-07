package com.hishd.qurantime.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hishd.qurantime.Activity.Officer.OfficerHomeActivity;
import com.hishd.qurantime.Activity.Patient.PatientApplicationIntroActivity;
import com.hishd.qurantime.Activity.Patient.PatientHomeActivity;
import com.hishd.qurantime.R;
import com.hishd.qurantime.databinding.ActivitySplashBinding;

import spencerstudios.com.bungeelib.Bungee;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
    }

    @Override
    protected void setupResources() {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_enter);
        binding.imgLogo.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            if (appConfig.getUserConfig() != null && appConfig.getUserLogged()) {
                if (appConfig.isOfficerType())
                    startActivity(new Intent(SplashActivity.this, OfficerHomeActivity.class));
                else {
                    if (appConfig.getIntroStatus())
                        startActivity(new Intent(SplashActivity.this, PatientHomeActivity.class));
                    else
                        startActivity(new Intent(SplashActivity.this, PatientApplicationIntroActivity.class));
                }
            } else
                startActivity(new Intent(SplashActivity.this, ActivityGetStarted.class));
            Bungee.fade(this);
            finish();
        }, 2000);
    }

    @Override
    protected void setListeners() {

    }
}