package com.hishd.qurantime.Activity.Officer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Model.AreaOverviewModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.databinding.ActivityOfficerHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import spencerstudios.com.bungeelib.Bungee;

public class OfficerHomeActivity extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityOfficerHomeBinding binding;
    private Dialog progressDialog;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    boolean isDataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDataLoaded)
            refreshAreaOverview(true);
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
        refreshAreaOverview(false);
    }

    @Override
    protected void setListeners() {
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefresh.setRefreshing(false);
                refreshAreaOverview(false);
            }
        });

        binding.imgProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, OfficerProfileActivity.class));
            Bungee.fade(this);
        });

        binding.containerRegisterPatients.setOnClickListener(v -> {
            startActivity(new Intent(this, OfficerAddPatientActivity.class));
            Bungee.fade(this);
        });

        binding.containerViewPatients.setOnClickListener(v -> {
            startActivity(new Intent(this, OfficerViewPatientsActivity.class));
            Bungee.fade(this);
        });

        binding.containerManagePatients.setOnClickListener(v -> {
            startActivity(new Intent(this, OfficerManagePatientsActivity.class));
            Bungee.fade(this);
        });
    }

    private void refreshAreaOverview(boolean silentLoading) {
        if (!silentLoading)
            progressDialog.show();
        apiOperation.officerGetAreaOverview(appConfig.getUserConfig().getCityID(), this);
    }

    @Override
    public void onAreaOverviewLoaded(AreaOverviewModel areaOverview) {
        isDataLoaded = true;
        progressDialog.dismiss();
        binding.txtPatientCount.setText(String.valueOf(areaOverview.getActivePatients()));
        binding.txtCriticalCount.setText(String.valueOf(areaOverview.getCriticalCount()));
        binding.txtNormalCount.setText(String.valueOf(areaOverview.getNormalCount()));
        binding.txtArea.setText(appConfig.getUserConfig().getCityName());
        binding.txtDate.setText(simpleDateFormat.format(new Date()));
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