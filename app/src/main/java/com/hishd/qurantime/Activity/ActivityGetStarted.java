package com.hishd.qurantime.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.hishd.qurantime.Activity.Auth.SignInActivity;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.LocaleHelper;
import com.hishd.qurantime.databinding.ActivityGetStartedBinding;

import spencerstudios.com.bungeelib.Bungee;

public class ActivityGetStarted extends BaseActivity {

    ActivityGetStartedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetStartedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void setListeners() {
        binding.switchLanguage.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {
                LocaleHelper.setLocale(this, "en");
            }
            else {
                LocaleHelper.setLocale(this, "si");
            }
            updateView();
        });
        binding.btnGetStarted.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            Bungee.fade(this);
        });
    }

    @Override
    protected void setupResources() {
        if(appConfig.getLanguage() == null) {
            binding.switchLanguage.setOn(true);
        } else {
            binding.switchLanguage.setOn(appConfig.getLanguage().equals("en"));
        }
        Log.e("LANG", appConfig.getLanguage());
    }

    private void updateView() {
        binding.textSelectLanguage.setText(getResources().getString(R.string.select_preferred_language));
        binding.btnGetStarted.setText(getResources().getString(R.string.get_started));
    }
}