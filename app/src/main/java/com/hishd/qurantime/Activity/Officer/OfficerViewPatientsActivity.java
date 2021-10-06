package com.hishd.qurantime.Activity.Officer;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Adapters.FilterPatientsAdapter;
import com.hishd.qurantime.Model.PatientModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.ItemOffsetDecoration;
import com.hishd.qurantime.databinding.ActivityOfficerViewPatientsBinding;

import java.util.ArrayList;

public class OfficerViewPatientsActivity extends BaseActivity implements FilterPatientsAdapter.OnFilterPatientActionsListener, APIOperation.OnAPIResultCallback {

    ActivityOfficerViewPatientsBinding binding;
    ArrayList<PatientModel> patientList = new ArrayList<>();
    FilterPatientsAdapter filterPatientsAdapter;
    private Animation recyclerAnimation;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerViewPatientsBinding.inflate(getLayoutInflater());
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

        filterPatientsAdapter = new FilterPatientsAdapter(this, patientList, this);
        binding.listPatients.setAdapter(filterPatientsAdapter);
        progressDialog = UIUtil.getProgress(this);

        progressDialog.show();
        apiOperation.officerFilterPatient(appConfig.getUserConfig().getCityID(), "Critical Condition", null, this);
    }

    @Override
    protected void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.switchHealthStatus.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (binding.radioHealthStatus.isChecked()) {
                    progressDialog.show();
                    apiOperation.officerFilterPatient(
                            appConfig.getUserConfig().getCityID(),
                            isOn ? "Critical Condition" : "Normal Condition",
                            null,
                            OfficerViewPatientsActivity.this
                    );
                }
            }
        });
        binding.switchCurrentCondition.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (binding.radioCondition.isChecked()) {
                    progressDialog.show();
                    apiOperation.officerFilterPatient(
                            appConfig.getUserConfig().getCityID(),
                            null,
                            isOn ? "Severe" : "Normal",
                            OfficerViewPatientsActivity.this
                    );
                }
            }
        });
    }

    @Override
    public void onCallClicked(int position) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (patientList.get(position).getContactNo() != null)
            intent.setData(Uri.parse("tel:" + patientList.get(position).getContactNo()));
        startActivity(intent);
    }

    @Override
    public void onPatientInfoLoaded(ArrayList<PatientModel> patientList) {
        progressDialog.dismiss();
        this.patientList.clear();
        this.patientList.addAll(patientList);
        filterPatientsAdapter.notifyDataSetChanged();
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