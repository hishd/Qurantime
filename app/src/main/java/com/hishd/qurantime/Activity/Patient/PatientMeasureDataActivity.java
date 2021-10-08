package com.hishd.qurantime.Activity.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Model.CachedHealthStatusModel;
import com.hishd.qurantime.Model.LastMeasurementsModel;
import com.hishd.qurantime.databinding.ActivityPatientMeasureDataBinding;

import java.util.Date;
import java.util.Locale;

public class PatientMeasureDataActivity extends BaseActivity {

    ActivityPatientMeasureDataBinding binding;
    double currentSpO2 = 57;
    double currentBPM = 76;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientMeasureDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void setupResources() {
        binding.txtSPO2Result.setText("");
        binding.txtHRResult.setText("");
    }

    @Override
    protected void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnDone.setOnClickListener(v -> {
            appConfig.saveLastMeasurements(new LastMeasurementsModel(currentSpO2, currentBPM, currentSpO2 > 94 ? "Normal" : "Critical", new Date()));
            qurantimeDB.cacheHealthStatus(
                    new CachedHealthStatusModel(
                            String.format(Locale.ENGLISH, "%.1f", this.currentSpO2),
                            String.format(Locale.ENGLISH, "%.0f", this.currentBPM)
                            )
            );
            finish();
        });
    }
}