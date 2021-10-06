package com.hishd.qurantime.Activity.Officer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hishd.animdialog.AnimDialog;
import com.hishd.lightpopup.LightPopup;
import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.Auth.SignInActivity;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Model.UserModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.LocaleHelper;
import com.hishd.qurantime.Util.Validator;
import com.hishd.qurantime.databinding.ActivityOfficerProfileBinding;

import org.json.JSONException;

import spencerstudios.com.bungeelib.Bungee;

public class OfficerProfileActivity extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityOfficerProfileBinding binding;
    private Dialog progressDialog;
    private AnimDialog signOutDialog;
    private LightPopup lightPopupPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void setupResources() {
        progressDialog = UIUtil.getProgress(this);
        signOutDialog = new AnimDialog(this)
                .createAnimatedDualDialog()
                .setAnimation("sign_out.json")
                .setBackgroundColor(R.color.white)
                .setButton1BackgroundColor(R.color.bg_error)
                .setButton2BackgroundColor(R.color.blue)
                .setTitle(getResources().getString(R.string.sign_out))
                .setTitleColor(R.color.black)
                .setContentColor(R.color.gray)
                .setContent(getString(R.string.sign_out_caption))
                .setButton1(getResources().getString(R.string.sign_out), R.color.white, false, v -> {
                    v.dismiss();
                    appConfig.clearUserConfig();
                    qurantimeDB.clearAll();
                    startActivity(new Intent(this, SignInActivity.class));
                    Bungee.fade(this);
                    finishAffinity();
                })
                .setButton2(getResources().getString(R.string.cancel), R.color.white, false, Dialog::dismiss);
        lightPopupPassword = new LightPopup(this)
                .createChangePasswordDialog()
                .setCancelledOnOutside(true)
                .setTitle(getString(R.string.change_password))
                .setBtn1Caption(getString(R.string.change))
                .setBtn1Color(R.color.blue)
                .setPwdDialogBtn1Action((password, dialog) -> {
                            dialog.dismiss();
                            progressDialog.show();
                            apiOperation.updatePassword(
                                    APIOperation.USER_TYPE.OFFICER,
                                    appConfig.getUserConfig().getEmailAddress(),
                                    password,
                                    OfficerProfileActivity.this
                            );
                        }, "^[a-zA-Z0-9@_*]{6,20}$",
                        getString(R.string.password_validation));
        if(appConfig.getLanguage() == null) {
            binding.switchLanguage.setOn(true);
        } else {
            binding.switchLanguage.setOn(appConfig.getLanguage().equals("en"));
        }
        Log.e("LANG", appConfig.getLanguage());
        refreshData();
    }

    private void refreshData() {
        binding.txtOfficerName.setText(appConfig.getUserConfig().getFullName());
        binding.txtNIC.setText(String.format("NIC : %s", appConfig.getUserConfig().getNicNo()));
        binding.txtContactNumber.setText(String.format("Contact : %s", appConfig.getUserConfig().getContactNo()));
        binding.txtFullName.setText(appConfig.getUserConfig().getFullName());
        binding.txtContactNo.setText(appConfig.getUserConfig().getContactNo());
        binding.txtEmail.setText(appConfig.getUserConfig().getEmailAddress());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListeners() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            hideSoftKeyboard(this, v);
            return true;
        });
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.containerSignOut.setOnClickListener(v -> {
            signOutDialog.show();
        });
        binding.btnUpdate.setOnClickListener(v -> {
            if (!Validator.isValidName(binding.txtFullName.getText().toString())) {
                binding.txtFullName.setError(getString(R.string.enter_valid_name));
                vibrateDevice();
                return;
            }
            if (!Validator.isValidPhone(binding.txtContactNo.getText().toString(), true)) {
                binding.txtContactNo.setError(getString(R.string.enter_valid_contact_no));
                vibrateDevice();
                return;
            }
            if (!Validator.isValidEmail(binding.txtEmail.getText().toString())) {
                binding.txtEmail.setError(getString(R.string.enter_valid_email));
                vibrateDevice();
                return;
            }

            progressDialog.show();
            apiOperation.updateProfile(
                    APIOperation.USER_TYPE.OFFICER,
                    appConfig.getUserConfig().getNicNo(),
                    binding.textFullName.getText().toString(),
                    binding.txtContactNo.getText().toString(),
                    binding.txtEmail.getText().toString(),
                    this
                    );
        });
        binding.btnChangePassword.setOnClickListener(v -> {
            lightPopupPassword.show();
        });
        binding.switchLanguage.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {
                LocaleHelper.setLocale(this, "en");
            } else {
                LocaleHelper.setLocale(this, "si");
            }
//            updateView();
            startActivity(new Intent(this,OfficerHomeActivity.class));
            finishAffinity();
        });
    }

//    private void updateView() {
//        binding.textViewProfile.setText(getString(R.string.officer_profile));
//        binding.textEditInformation.setText(getString(R.string.edit_information));
//        binding.textFullName.setText(getString(R.string.full_name));
//        binding.textContactNo.setText(getString(R.string.contact_no));
//        binding.textEmailAddress.setText(getString(R.string.email_address));
//        binding.btnChangePassword.setText(getString(R.string.change_password));
//        binding.btnUpdate.setText(getString(R.string.update));
//        binding.textSelectLanguage.setText(getString(R.string.select_preferred_language));
//        binding.textSelectLanguage.setText(getString(R.string.select_preferred_language));
//        binding.textSignOut.setText(getString(R.string.sign_out));
//    }

    @Override
    public void onPasswordUpdated(String message) {
        progressDialog.dismiss();
        displayAlert(this, AlertType.SUCCESS, getString(R.string.password_changed), getString(R.string.password_changed_message));
    }

    @Override
    public void onProfileUpdated(String message) {
        progressDialog.dismiss();
        displayAlert(this, AlertType.SUCCESS, getString(R.string.profile_updated), getString(R.string.profile_updated_caption));
        UserModel userModel = appConfig.getUserConfig();
        userModel.setContactNo(binding.txtContactNo.getText().toString());
        userModel.setFullName(binding.txtFullName.getText().toString());
        userModel.setEmailAddress(binding.txtEmail.getText().toString());
        appConfig.saveUserConfig(userModel, true);
        refreshData();
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