package com.hishd.qurantime.Activity.Patient;

import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hishd.qurantime.APIService.APIModel.HealthStatusModel;
import com.hishd.qurantime.APIService.APIModel.UpdateHealthStatusModel;
import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Model.CachedHealthStatusModel;
import com.hishd.qurantime.Model.CurrentConditionModel;
import com.hishd.qurantime.APIService.APIModel.SymptomModel;
import com.hishd.qurantime.APIService.APIModel.UpdateSymptomModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.databinding.ActivityPatientHomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import spencerstudios.com.bungeelib.Bungee;

public class PatientHomeActivity extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityPatientHomeBinding binding;
    private Dialog progressDialog;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    ArrayList<SymptomModel> symptoms = new ArrayList<>();

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
        uploadCachedData();
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
        binding.containerSpo2.startAnimation(animEnterFromLeft);
        binding.containerStatus.startAnimation(animEnterFromRight);
        binding.containerBpm.startAnimation(animFadeEnter);
        binding.containerSymptoms.startAnimation(animFadeEnter);
        binding.containerEmergencyServices.startAnimation(animEnterFromLeft);
        binding.containerMeasureNow.startAnimation(animEnterFromRight);
        binding.containerHistory.startAnimation(animEnterFromRight);
    }

    @Override
    protected void setListeners() {
        //Symptom Container Actions
        binding.containerHeadache.setOnClickListener(v -> {
            this.isHeadAche = !this.isHeadAche;
            this.currentCondition.setHeadache(this.isHeadAche);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerHeadache.setBackground(
                    !currentCondition.isHeadache() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.containerCough.setOnClickListener(v -> {
            this.isCough = !this.isCough;
            this.currentCondition.setCough(this.isCough);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerCough.setBackground(
                    !currentCondition.isCough() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.containerShortness.setOnClickListener(v -> {
            this.isShortness = !this.isShortness;
            this.currentCondition.setShortnessOfBreath(this.isShortness);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerShortness.setBackground(
                    !currentCondition.isShortnessOfBreath() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.containerSoreTroat.setOnClickListener(v -> {
            this.isSoreTorat = !this.isSoreTorat;
            this.currentCondition.setSoreTroat(this.isSoreTorat);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerSoreTroat.setBackground(
                    !currentCondition.isSoreTroat() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            sendCurrentSymptomData();
        });
        binding.containerFever.setOnClickListener(v -> {
            this.isFever = !this.isFever;
            this.currentCondition.setFever(this.isFever);
            appConfig.saveCurrentCondition(currentCondition);
            binding.containerFever.setBackground(
                    !currentCondition.isFever() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
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
            if(appConfig.getLastMeasurements().getHealthStatus().equals("Normal")) {
                binding.containerStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.container_orange));
            } else {
                binding.containerStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.container_red));
            }
        }
        if(appConfig.getLastCurrentCondition() != null) {
            currentCondition = appConfig.getLastCurrentCondition();
            binding.containerHeadache.setBackground(
                    !currentCondition.isHeadache() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
                    );
            binding.containerCough.setBackground(
                    !currentCondition.isCough() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            binding.containerShortness.setBackground(
                    !currentCondition.isShortnessOfBreath() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            binding.containerSoreTroat.setBackground(
                    !currentCondition.isSoreTroat() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
                            ContextCompat.getDrawable(this, R.drawable.container_symptom)
            );
            binding.containerFever.setBackground(
                    !currentCondition.isFever() ? ContextCompat.getDrawable(this, R.drawable.container_no_symptom) :
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
        symptoms.clear();
        if(isHeadAche) {
            symptoms.add(new SymptomModel("Headache"));
            Log.e("Result", "Headache");
        }
        if(isCough) {
            symptoms.add(new SymptomModel("Cough"));
            Log.e("Result", "Cough");
        }
        if(isShortness) {
            symptoms.add(new SymptomModel("Shortness of Breath"));
            Log.e("Result", "Shortness of Breath");
        }
        if(isSoreTorat) {
            symptoms.add(new SymptomModel("Sore Troat"));
            Log.e("Result", "Sore Troat");
        }
        if(isFever) {
            symptoms.add(new SymptomModel("Fever"));
            Log.e("Result", "Fever");
        }
        progressDialog.show();
        apiOperation.patientUpdateSymptoms(new UpdateSymptomModel(appConfig.getUserConfig().getNicNo(), symptoms), this);
    }

    private void uploadCachedData() {
        final List<CachedHealthStatusModel> cachedHealthStatus = qurantimeDB.getCachedHealthStatus();
        if(cachedHealthStatus!=null && cachedHealthStatus.size() > 0) {
            final ArrayList<HealthStatusModel> healthMeasurements = new ArrayList<>();
            for(CachedHealthStatusModel model : cachedHealthStatus) {
                healthMeasurements.add(new HealthStatusModel(model.getSpo2Level(), model.getBpmLevel()));
            }
            apiOperation.patientUpdateHealthStatus(new UpdateHealthStatusModel(appConfig.getUserConfig().getNicNo(), healthMeasurements), this);
        }
    }

    @Override
    public void onHealthStatusUpdated(String message) {
        Toast.makeText(this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
        qurantimeDB.clearCachedHealthStatus();
    }

    @Override
    public void onSymptomsUpdated(String message) {
        progressDialog.dismiss();
        displayAlert(this, AlertType.SUCCESS, getString(R.string.symptoms_updated), getString(R.string.symptoms_updated_caption));
    }

    @Override
    public void onOperationFailed(String error) {
        progressDialog.dismiss();
        displayAlert(this, AlertType.ERROR, getString(R.string.operation_failed), error);
    }

    @Override
    public void onConnectionLost(String message) {
        progressDialog.dismiss();
        displayAlert(this, AlertType.WARNING, getString(R.string.connection_error), message);
    }
}