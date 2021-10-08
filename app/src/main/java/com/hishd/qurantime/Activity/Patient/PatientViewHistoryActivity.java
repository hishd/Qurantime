package com.hishd.qurantime.Activity.Patient;

import android.app.Dialog;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Adapters.RecordHistoryAdapter;
import com.hishd.qurantime.Model.MeasurementHistoryRecordModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.ItemOffsetDecoration;
import com.hishd.qurantime.databinding.ActivityPatientViewHistoryBinding;

import java.util.ArrayList;
import java.util.Locale;

public class PatientViewHistoryActivity extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityPatientViewHistoryBinding binding;
    ArrayList<MeasurementHistoryRecordModel> measurementRecordList = new ArrayList<>();
    RecordHistoryAdapter recordHistoryAdapter;
    private Animation recyclerAnimation;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientViewHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void setupResources() {
        recyclerAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_enter);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        final ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(this, R.dimen._5sdp, R.dimen._8sdp, R.dimen._1sdp, R.dimen._1sdp);
        binding.listMeasurements.setLayoutManager(new LinearLayoutManager(this));
        binding.listMeasurements.addItemDecoration(itemOffsetDecoration);
        binding.listMeasurements.setLayoutAnimation(controller);

        recordHistoryAdapter = new RecordHistoryAdapter(this, measurementRecordList);
        binding.listMeasurements.setAdapter(recordHistoryAdapter);
        progressDialog = UIUtil.getProgress(this);
        refreshData();
    }

    @Override
    protected void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    void refreshData() {
        progressDialog.show();
        apiOperation.getPatientMeasurementHistory(appConfig.getUserConfig().getNicNo(), this);
    }

    @Override
    public void onPatientHistoryLoaded(ArrayList<MeasurementHistoryRecordModel> lastMeasurements) {
        progressDialog.dismiss();
        this.measurementRecordList.clear();
        this.measurementRecordList.addAll(lastMeasurements);
        this.recordHistoryAdapter.notifyDataSetChanged();
        binding.listMeasurements.startAnimation(recyclerAnimation);

        double avgSpO2 = 0;
        double avgBPM = 0;
        for (MeasurementHistoryRecordModel measurementRecord : lastMeasurements) {
            avgSpO2 += measurementRecord.getSpo2Level();
            avgBPM += measurementRecord.getBpmLevel();
        }
        binding.txtSPO2Result.setText(String.format(Locale.ENGLISH, "%.1f%%", avgSpO2/lastMeasurements.size()));
        binding.txtHRResult.setText(String.format(Locale.ENGLISH, "%.0f BPM", avgBPM/lastMeasurements.size()));
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