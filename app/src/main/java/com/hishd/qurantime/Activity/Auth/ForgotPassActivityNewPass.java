package com.hishd.qurantime.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Model.UserModel;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.Validator;
import com.hishd.qurantime.databinding.ActivityForgotPassNewPassBinding;

import org.json.JSONException;

public class ForgotPassActivityNewPass extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityForgotPassNewPassBinding binding;
    private String email = "";
    private boolean isOfficer = false;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassNewPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.animationView.playAnimation();
    }

    @Override
    protected void setupResources() {
        progressDialog = UIUtil.getProgress(this);
        if(getIntent().hasExtra("EMAIL") && getIntent().hasExtra("IS_OFFICER")) {
            this.email = getIntent().getStringExtra("EMAIL");
            this.isOfficer = getIntent().getBooleanExtra("IS_OFFICER", false);
        } else {
            displayAlert(this, AlertType.ERROR, getString(R.string.operation_failed), getString(R.string.application_error));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListeners() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            hideSoftKeyboard(this, v);
            return true;
        });

        binding.btnChange.setOnClickListener(v -> {
            if(this.email.length() == 0) {
                displayAlert(this, AlertType.ERROR, getString(R.string.operation_failed), getString(R.string.cant_change_password));
                return;
            }

            if (!Validator.isValidPassword(binding.txtPassword.getText().toString())) {
                binding.txtPassword.setError(getString(R.string.password_validation));
                vibrateDevice();
                return;
            }

            if (!binding.txtPassword.getText().toString().equals(binding.txtConfirmPassword.getText().toString())) {
                binding.txtPassword.setError(getString(R.string.password_no_match));
                binding.txtConfirmPassword.setError(getString(R.string.password_no_match));
                vibrateDevice();
                return;
            }

            progressDialog.show();
            apiOperation.updatePassword(
                    this.isOfficer ? APIOperation.USER_TYPE.OFFICER : APIOperation.USER_TYPE.PATIENT,
                    this.email,
                    binding.txtPassword.getText().toString(),
                    this
            );
        });
    }

    @Override
    public void onPasswordUpdated(String message) {
        progressDialog.dismiss();
        displayAlert(this, AlertType.SUCCESS, getString(R.string.password_changed), getString(R.string.password_changed_message), dismissType -> {
            startActivity(new Intent(this, SignInActivity.class));
            finishAffinity();
        });
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