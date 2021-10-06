package com.hishd.qurantime.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.Validator;
import com.hishd.qurantime.databinding.ActivityForgotPassEnterOtpBinding;

import java.util.Locale;

import spencerstudios.com.bungeelib.Bungee;

public class ForgotPassActivityEnterOTP extends BaseActivity implements APIOperation.OnAPIResultCallback {

    ActivityForgotPassEnterOtpBinding binding;
    private CountDownTimer countDownTimer;
    private String otp = "0000";
    private String email = "";
    private boolean isOfficer = false;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassEnterOtpBinding.inflate(getLayoutInflater());
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
        countDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtCountSecond.setText(String.format(Locale.ENGLISH, "%d Sec", millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                stopCountDown();
            }
        };

        if(getIntent().hasExtra("EMAIL") && getIntent().hasExtra("OTP") && getIntent().hasExtra("IS_OFFICER")) {
            this.otp = getIntent().getStringExtra("OTP");
            this.email = getIntent().getStringExtra("EMAIL");
            this.isOfficer = getIntent().getBooleanExtra("IS_OFFICER", false);
            binding.txtEmail.setText(this.email);
            startCountDown();
        } else {
            displayAlert(this, AlertType.ERROR, getString(R.string.otp_failed), getString(R.string.could_not_send_otp));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListeners() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            hideSoftKeyboard(this, v);
            return true;
        });

        binding.btnVerify.setOnClickListener(v -> {

            //Check if the OTP does not contain in extras
            if(this.otp.equals("0000")) {
                displayAlert(this, AlertType.ERROR, getString(R.string.otp_failed), getString(R.string.could_not_send_otp));
                return;
            }

            if (!Validator.isValidOTP(binding.txtOTP.getText().toString())) {
                binding.txtOTP.setError("Enter a valid OTP");
                vibrateDevice();
                return;
            }

            if(!this.otp.equals(binding.txtOTP.getText().toString())) {
                displayAlert(this, AlertType.ERROR, getString(R.string.invalid_otp), getString(R.string.otp_no_match));
            } else {
                binding.animationView.cancelAnimation();
                startActivity(new Intent(this, ForgotPassActivityNewPass.class)
                        .putExtra("EMAIL", this.email)
                        .putExtra("IS_OFFICER", this.isOfficer)
                );
                Bungee.fade(this);
                finish();
            }
        });

        binding.btnResendOTP.setOnClickListener(v -> {
            progressDialog.show();
            apiOperation.getOTP(
                    this.isOfficer ? APIOperation.USER_TYPE.OFFICER : APIOperation.USER_TYPE.PATIENT,
                    binding.txtEmail.getText().toString().toLowerCase(),
                    this
            );
        });
    }

    private void startCountDown() {
        binding.btnResendOTP.setVisibility(View.INVISIBLE);
        binding.containerCountdown.setVisibility(View.VISIBLE);
        countDownTimer.start();

        displayAlert(this, AlertType.INFO, getString(R.string.otp_sent), getString(R.string.otp_sent_message));
    }

    private void stopCountDown() {
        binding.btnResendOTP.setVisibility(View.VISIBLE);
        binding.containerCountdown.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onOTPReceived(String otp, String email) {
        progressDialog.dismiss();
        this.otp = otp;
        startCountDown();
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