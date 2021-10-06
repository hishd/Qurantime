package com.hishd.qurantime.Activity.Officer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.hishd.multiselectpopup.MultiSelectDialog;
import com.hishd.multiselectpopup.MultiSelectModel;
import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Model.ComorbidityModel;
import com.hishd.qurantime.Model.HospitalModel;
import com.hishd.qurantime.Model.PatientRegistrationModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.Validator;
import com.hishd.qurantime.databinding.ActivityOfficerAddPatientBinding;

import org.aviran.cookiebar2.CookieBarDismissListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OfficerAddPatientActivity extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityOfficerAddPatientBinding binding;
    ArrayList<String> hospitalNameList = new ArrayList<>();
    ArrayAdapter<String> hospitalAdapter;
    List<HospitalModel> hospitalList;
    ArrayList<Integer> selectedComorbiditiesID = new ArrayList<>();
    ArrayList<String> comorbidities = new ArrayList<>();
    private Dialog progressDialog;
    MultiSelectDialog selectComorbiditiesDialog;
    private String TAG = "OfficerAddPatientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerAddPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void setupResources() {
        progressDialog = UIUtil.getProgress(this);
        hospitalAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, hospitalNameList);
        hospitalAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.spinnerHospital.setAdapter(hospitalAdapter);
        this.comorbidities.addAll(Arrays.asList(getResources().getStringArray(R.array.comorbidities)));
        loadHospitalData();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListeners() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            hideSoftKeyboard(this, v);
            return true;
        });
        binding.btnSelectComorbidities.setOnClickListener(v -> {
            prepareComorbidities();
        });
        binding.btnRegister.setOnClickListener(v -> {
            registerPatient();
        });
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnGeneratePassword.setOnClickListener(v -> {
            binding.txtPassword.setText(generatePassword(8));
        });
    }

    private void registerPatient() {
        if (!Validator.isValidNIC(binding.txtNIC.getText().toString())) {
            binding.txtNIC.setError(getString(R.string.invalid_nic));
            vibrateDevice();
            return;
        }
        if (!Validator.isValidName(binding.txtName.getText().toString())) {
            binding.txtName.setError(getString(R.string.enter_valid_name));
            vibrateDevice();
            return;
        }
        if (binding.txtAddress.getText().length() < 5) {
            binding.txtAddress.setError(getString(R.string.enter_valid_address));
            vibrateDevice();
            return;
        }
        if (!Validator.isValidPhone(binding.txtContactNo.getText().toString(), true)) {
            binding.txtContactNo.setError(getString(R.string.enter_valid_contact_no));
            vibrateDevice();
            return;
        }
        if(binding.spinnerHospital.getSelectedItem() == null) {
            displayAlert(this, AlertType.INFO, "Select Hospital", "Please select a hospital");
            vibrateDevice();
            return;
        }
        if (!Validator.isValidPassword(binding.txtPassword.getText().toString())) {
            binding.txtPassword.setError("Generate a password");
            vibrateDevice();
            return;
        }

        final ArrayList<ComorbidityModel> comorbidityList = new ArrayList<>();
        for(int id : this.selectedComorbiditiesID) {
            comorbidityList.add(new ComorbidityModel(this.comorbidities.get(id)));
        }

        final PatientRegistrationModel registrationModel = new PatientRegistrationModel();
        registrationModel.setNicNo(binding.txtNIC.getText().toString().toLowerCase());
        registrationModel.setFullName(binding.txtName.getText().toString());
        registrationModel.setAddress(binding.txtAddress.getText().toString());
        registrationModel.setContactNo(binding.txtContactNo.getText().toString());
        registrationModel.setEmailAddress(binding.txtEmail.getText().toString());
        registrationModel.setPassword(binding.txtPassword.getText().toString());
        registrationModel.setLocationLat("0");
        registrationModel.setLocationLon("0");
        registrationModel.setVaccinated(binding.switchVaccinated.isOn());
        registrationModel.setHospitalID(this.hospitalList.get(binding.spinnerHospital.getSelectedItemPosition()).get_id());
        registrationModel.setHospitalName(this.hospitalList.get(binding.spinnerHospital.getSelectedItemPosition()).getHospitalName());
        registrationModel.setDistrictID(appConfig.getUserConfig().getDistrictID());
        registrationModel.setDistrictName(appConfig.getUserConfig().getDistrictName());
        registrationModel.setCityID(appConfig.getUserConfig().getCityID());
        registrationModel.setCityName(appConfig.getUserConfig().getCityName());
        registrationModel.setComorbidities(comorbidityList);

        progressDialog.show();
        apiOperation.officerRegisterPatient(registrationModel, this);
    }

    void loadHospitalData() {
        hospitalList = qurantimeDB.getHospitalList();
        if(hospitalList.size() > 0) {
            for (HospitalModel hospital : hospitalList)
                this.hospitalNameList.add(hospital.getHospitalName());
            hospitalAdapter.notifyDataSetChanged();
        } else {
            progressDialog.show();
            apiOperation.getAvailableHospitals(appConfig.getUserConfig().getCityID(), this);
        }
    }

    public String generatePassword(int length){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();

        Random rand = new Random();

        for(int i = 0; i < length; i++){
            char c = chars[rand.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    void prepareComorbidities() {
        final ArrayList<MultiSelectModel> listOfComorbidities = new ArrayList<>();
        final ArrayList<Integer> selectedID = new ArrayList<>();
        for (int i = 0; i < this.comorbidities.size(); i++)
            listOfComorbidities.add(new MultiSelectModel(i, this.comorbidities.get(i)));
        if(selectedComorbiditiesID.size() > 0) {
            selectedID.addAll(this.selectedComorbiditiesID);
        }

        selectComorbiditiesDialog = new MultiSelectDialog(this)
                .setTitle(getString(R.string.select_comorbidities))
                .setTitleTypeface(ResourcesCompat.getFont(this, R.font.gilroy_bold))
                .setPositiveText(getResources().getString(R.string.done))
                .setNegativeText(getString(R.string.cancel))
                .setMinSelectionLimit(0)
                .setMaxSelectionLimit(listOfComorbidities.size())
                .preSelectIDsList(selectedID)
                .multiSelectList(listOfComorbidities)
                .build()
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String s) {
                        selectedComorbiditiesID.clear();
                        selectedComorbiditiesID.addAll(selectedIds);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Category Selection cancelled");
                    }
                });
        try {
            selectComorbiditiesDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPatientRegistered(String message) {
        progressDialog.dismiss();
        displayAlert(this, AlertType.SUCCESS, "Patient Registered", "Patient registration successful!", new CookieBarDismissListener() {
            @Override
            public void onDismiss(int dismissType) {
                finish();
            }
        });
    }

    @Override
    public void onHospitalsLoaded(ArrayList<HospitalModel> hospitals) {
        progressDialog.dismiss();
        qurantimeDB.syncHospitals(hospitals);
        this.hospitalList = hospitals;
        this.hospitalNameList.clear();
        for (HospitalModel hospital : hospitals)
            this.hospitalNameList.add(hospital.getHospitalName());
        hospitalAdapter.notifyDataSetChanged();
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