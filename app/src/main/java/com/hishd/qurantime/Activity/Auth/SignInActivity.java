package com.hishd.qurantime.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Activity.Officer.OfficerHomeActivity;
import com.hishd.qurantime.Activity.Patient.PatientHomeActivity;
import com.hishd.qurantime.Model.UserModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.Validator;
import com.hishd.qurantime.databinding.ActivitySignInBinding;

import spencerstudios.com.bungeelib.Bungee;

public class SignInActivity extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivitySignInBinding binding;
    private Dialog progressDialog;
    private boolean visibilityToggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void setupResources() {
        progressDialog = UIUtil.getProgress(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListeners() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            hideSoftKeyboard(this, v);
            return true;
        });

        binding.imgToggle.setOnClickListener(v -> {
            if(this.visibilityToggle) {
                binding.txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imgToggle.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off));
            } else {
                binding.txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imgToggle.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_on));
            }

            binding.txtPassword.setSelection(binding.txtPassword.getText().length());
            this.visibilityToggle = !this.visibilityToggle;
        });

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnSignIn.setOnClickListener(v -> {
            if (!Validator.isValidEmail(binding.txtEmail.getText().toString())) {
                binding.txtEmail.setError(getString(R.string.enter_valid_email));
                vibrateDevice();
                return;
            }

            if (!Validator.isValidPassword(binding.txtPassword.getText().toString())) {
                binding.txtPassword.setError(getString(R.string.invalid_password));
                vibrateDevice();
                return;
            }

            progressDialog.show();
            apiOperation.userSignIn(
                    binding.switchUserType.isOn() ? APIOperation.USER_TYPE.OFFICER : APIOperation.USER_TYPE.PATIENT,
                    binding.txtEmail.getText().toString().toLowerCase(),
                    binding.txtPassword.getText().toString(),
                    this
            );
        });

        binding.btnForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPassActivityEnterEmail.class));
            Bungee.fade(this);
        });
    }

    @Override
    public void onSignInSuccessful(UserModel userModel) {
        progressDialog.dismiss();
        appConfig.saveUserConfig(userModel, binding.switchUserType.isOn());
        if(binding.switchUserType.isOn()) {
            startActivity(new Intent(this, OfficerHomeActivity.class));
        } else {
            startActivity(new Intent(this, PatientHomeActivity.class));
        }
        Bungee.fade(this);
        finishAffinity();
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