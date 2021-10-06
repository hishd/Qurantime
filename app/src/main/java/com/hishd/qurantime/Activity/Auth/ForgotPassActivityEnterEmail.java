package com.hishd.qurantime.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.Validator;
import com.hishd.qurantime.databinding.ActivityForgotPassEnterEmailBinding;

import spencerstudios.com.bungeelib.Bungee;

public class ForgotPassActivityEnterEmail extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityForgotPassEnterEmailBinding binding;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassEnterEmailBinding.inflate(getLayoutInflater());
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
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListeners() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            hideSoftKeyboard(this, v);
            return true;
        });

        binding.btnSignIn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnReset.setOnClickListener(v -> {
            if (!Validator.isValidEmail(binding.txtEmail.getText().toString())) {
                binding.txtEmail.setError(getString(R.string.enter_valid_email));
                vibrateDevice();
                return;
            }

            progressDialog.show();
            apiOperation.getOTP(
                    binding.switchUserType.isOn() ? APIOperation.USER_TYPE.OFFICER : APIOperation.USER_TYPE.PATIENT,
                    binding.txtEmail.getText().toString().toLowerCase(),
                    this
            );
        });
    }

    @Override
    public void onOTPReceived(String otp, String email) {
        progressDialog.dismiss();
        binding.animationView.cancelAnimation();
        startActivity(new Intent(this, ForgotPassActivityEnterOTP.class)
                .putExtra("EMAIL", email)
                .putExtra("OTP", otp)
                .putExtra("IS_OFFICER", binding.switchUserType.isOn())
        );
        Bungee.fade(this);
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