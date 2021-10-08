package com.hishd.qurantime.Activity.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Model.CachedHealthStatusModel;
import com.hishd.qurantime.Model.LastMeasurementsModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.databinding.ActivityPatientMeasureDataBinding;

import java.util.Date;
import java.util.Locale;

public class PatientMeasureDataActivity extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityPatientMeasureDataBinding binding;
    double currentSpO2 = 0;
    double currentBPM = 0;
    CountDownTimer timer;
    boolean pulseDetected = false;

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
        timer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                Log.d("Measure in : ", String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                apiOperation.pulseOXGetData(PatientMeasureDataActivity.this);
                timer.start();
                if(pulseDetected) {
                    binding.txtOperationStatus.setText(getResources().getString(R.string.monitoring));
                } else {
                    binding.txtOperationStatus.setText(R.string.waiting);
                }
            }
        };
        startMeasuring();
    }

    private void startMeasuring() {
        apiOperation.pulseOXGetData(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    protected void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnDone.setOnClickListener(v -> {
            if(this.currentSpO2 != 0) {
                appConfig.saveLastMeasurements(new LastMeasurementsModel(currentSpO2, currentBPM, currentSpO2 > 94 ? "Normal" : "Critical", new Date()));
                qurantimeDB.cacheHealthStatus(
                        new CachedHealthStatusModel(
                                String.format(Locale.ENGLISH, "%.1f", this.currentSpO2),
                                String.format(Locale.ENGLISH, "%.0f", this.currentBPM)
                        )
                );
            }
            finish();
        });
    }

    @Override
    public void onOperationFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionLost(String message) {

    }

    @Override
    public void onPulseOXDataLoaded(double spO2, double hr) {
        binding.txtSPO2.setText(String.format(Locale.ENGLISH, "%.1f%%", spO2));
        binding.txtBPM.setText(String.format(Locale.ENGLISH, "%.0f", hr));
        this.currentSpO2 = spO2;
        this.currentBPM = hr;
        if(spO2 == 0 || hr == 0) {
            pulseDetected = false;
            binding.txtSPO2Result.setText(R.string.finger_out);
            binding.txtHRResult.setText(R.string.finger_out);
            return;
        }
        pulseDetected = true;
        if(spO2 < 94) {
            binding.txtSPO2Result.setText(getResources().getString(R.string.severe));
        } else {
            binding.txtSPO2Result.setText(getResources().getString(R.string.normal));
        }
        if(hr > 90) {
            binding.txtHRResult.setText(getResources().getString(R.string.severe));
        } else {
            binding.txtHRResult.setText(getResources().getString(R.string.normal));
        }
    }
}