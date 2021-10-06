package com.hishd.qurantime.Activity.Officer;

import android.app.Dialog;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hishd.lightpopup.LightPopup;
import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Adapters.ManagePatientsAdapter;
import com.hishd.qurantime.Model.PatientModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.ItemOffsetDecoration;
import com.hishd.qurantime.databinding.ActivityOfficerManagePatientsBinding;

import java.util.ArrayList;

public class OfficerManagePatientsActivity extends BaseActivity implements ManagePatientsAdapter.OnPatientActionsListener, APIOperation.OnAPIResultCallback {

    ActivityOfficerManagePatientsBinding binding;
    ArrayList<PatientModel> patientList = new ArrayList<>();
    ManagePatientsAdapter managePatientsAdapter;
    private Animation recyclerAnimation;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerManagePatientsBinding.inflate(getLayoutInflater());
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
        binding.listPatients.setLayoutManager(new LinearLayoutManager(this));
        binding.listPatients.addItemDecoration(itemOffsetDecoration);
        binding.listPatients.setLayoutAnimation(controller);

        managePatientsAdapter = new ManagePatientsAdapter(this, patientList, this);
        binding.listPatients.setAdapter(managePatientsAdapter);
        progressDialog = UIUtil.getProgress(this);
        refreshData();
    }

    @Override
    protected void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnSearch.setOnClickListener(v -> {
            progressDialog.show();
            apiOperation.officerSearchPatients(appConfig.getUserConfig().getCityID(), binding.txtNIC.getText().toString().toLowerCase(), this);
        });
        binding.btnViewAll.setOnClickListener(v -> {
            refreshData();
        });
    }

    void refreshData() {
        progressDialog.show();
        apiOperation.officerSearchPatients(appConfig.getUserConfig().getCityID(), null, this);
    }

    @Override
    public void onRemovePressed(int position) {
        new LightPopup(this)
                .createDualActionDialog()
                .setCancelledOnOutside(false)
                .setTitle(getResources().getString(R.string.remove_patient))
                .setMessage(getResources().getString(R.string.remove_patient_caption))
                .setBtn1Caption(getResources().getString(R.string.cancel))
                .setBtn1Color(R.color.blue)
                .setBtn1Action(Dialog::dismiss)
                .setBtn2Caption(getString(R.string.remove))
                .setBtn2Color(R.color.red)
                .setBtn2Action(dialog -> {
                    dialog.dismiss();
                    progressDialog.show();
                    apiOperation.officerDeletePatient(patientList.get(position).getNicNo(), OfficerManagePatientsActivity.this);
                }).show();
    }

    @Override
    public void onPatientRemoved(String message) {
        progressDialog.dismiss();
        binding.txtNIC.setText(null);
        refreshData();
    }

    @Override
    public void onPatientInfoLoaded(ArrayList<PatientModel> patientList) {
        progressDialog.dismiss();
        this.patientList.clear();
        this.patientList.addAll(patientList);
        managePatientsAdapter.notifyDataSetChanged();
        binding.listPatients.startAnimation(recyclerAnimation);
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