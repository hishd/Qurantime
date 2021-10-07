package com.hishd.qurantime.Activity.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Activity.Officer.OfficerProfileActivity;
import com.hishd.qurantime.Model.CurrentConditionModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.databinding.ActivityPatientHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import spencerstudios.com.bungeelib.Bungee;

public class PatientHomeActivity extends BaseActivity {

    ActivityPatientHomeBinding binding;
    private Dialog progressDialog;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    //Symptoms
    boolean isHeadAche = false;
    boolean isCough = false;
    boolean isShortness = false;
    boolean isSoreTorat = false;
    boolean isFever = false;
    CurrentConditionModel currentCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMeasurementData();
        if (Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM) {
            binding.txtGreeting.setText(getResources().getString(R.string.good_morning));
        } else {
            binding.txtGreeting.setText(getResources().getString(R.string.good_evening));
        }
        binding.txtName.setText(appConfig.getUserConfig().getFullName());
    }

    @Override
    protected void setupResources() {
        progressDialog = UIUtil.getProgress(this);
    }

    @Override
    protected void setListeners() {
        //Symptom Container Actions
        binding.containerHeadache.setOnClickListener(v -> {
            this.isHeadAche = !this.isHeadAche;
            this.currentCondition.setHeadache(this.isHeadAche);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerHeadache.setBackground(
                    currentCondition.isHeadache() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.containerCough.setOnClickListener(v -> {
            this.isCough = !this.isCough;
            this.currentCondition.setCough(this.isCough);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerCough.setBackground(
                    currentCondition.isCough() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.containerShortness.setOnClickListener(v -> {
            this.isShortness = !this.isShortness;
            this.currentCondition.setShortnessOfBreath(this.isShortness);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerShortness.setBackground(
                    currentCondition.isShortnessOfBreath() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.containerSoreTroat.setOnClickListener(v -> {
            this.isSoreTorat = !this.isSoreTorat;
            this.currentCondition.setSoreTroat(this.isSoreTorat);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerSoreTroat.setBackground(
                    currentCondition.isSoreTroat() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.containerFever.setOnClickListener(v -> {
            this.isFever = !this.isFever;
            this.currentCondition.setFever(this.isFever);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerFever.setBackground(
                    currentCondition.isFever() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.imgProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, PatientProfileActivity.class));
            Bungee.fade(this);
        });
        binding.containerEmergencyServices.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            if (appConfig.getUserConfig().getHospitalContact() != null)
                intent.setData(Uri.parse("tel:" + appConfig.getUserConfig().getHospitalContact()));
            startActivity(intent);
        });
        binding.containerMeasureNow.setOnClickListener(v -> {
            Log.d("Measure_Intro", String.valueOf(appConfig.getMeasureIntroStatus()));
            if(appConfig.getMeasureIntroStatus())
                startActivity(new Intent(this, PatientConnectDevice.class));
            else
                startActivity(new Intent(this, PatientMeasureIntroActivity.class));
            Bungee.fade(this);
        });
        binding.containerHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, PatientViewHistoryActivity.class));
            Bungee.fade(this);
        });
    }

    private void refreshMeasurementData() {
        if(appConfig.getLastMeasurements() != null) {
            binding.txtSPO2.setText(String.format(Locale.ENGLISH,"%.1f%%", appConfig.getLastMeasurements().getLastSPo2()));
            binding.txtBPM.setText(String.format(Locale.ENGLISH, "%.0f BPM", appConfig.getLastMeasurements().getLastHR()));
            binding.txtStatus.setText(appConfig.getLastMeasurements().getHealthStatus());
            binding.txtTime.setText(String.format("%s : %s", getResources().getString(R.string.time), simpleDateFormat.format(appConfig.getLastMeasurements().getLastUpdate())));
        }
        if(appConfig.getLastCurrentCondition() != null) {
            currentCondition = appConfig.getLastCurrentCondition();
            binding.containerHeadache.setBackground(
                    currentCondition.isHeadache() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
                    );
            binding.containerCough.setBackground(
                    currentCondition.isCough() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            binding.containerShortness.setBackground(
                    currentCondition.isShortnessOfBreath() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            binding.containerSoreTroat.setBackground(
                    currentCondition.isSoreTroat() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            binding.containerFever.setBackground(
                    currentCondition.isFever() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );

            this.isHeadAche = currentCondition.isHeadache();
            this.isCough = currentCondition.isCough();
            this.isShortness = currentCondition.isShortnessOfBreath();
            this.isSoreTorat = currentCondition.isSoreTroat();
            this.isFever = currentCondition.isFever();
        } else {
            currentCondition = new CurrentConditionModel();
        }
    }

    private void sendCurrentSymptomData() {

    }
}