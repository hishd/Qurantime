package com.hishd.qurantime.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hishd.qurantime.APIService.APIOperation;
import com.hishd.qurantime.R;
import com.hishd.qurantime.Util.AppConfig;
import com.hishd.qurantime.Util.LocaleHelper;
import com.hishd.qurantime.Util.UIUtil;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.CookieBarDismissListener;

public abstract class BaseActivity extends AppCompatActivity {

    protected Vibrator vibrator;
    protected APIOperation apiOperation;
    protected AppConfig appConfig;
    protected final UIUtil UIUtil = new UIUtil();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        apiOperation = APIOperation.getInstance();
        appConfig = AppConfig.getInstance();
    }

    protected abstract void setupResources();
    protected abstract void setListeners();

    protected static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    protected void vibrateDevice(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(50);
        }
    }

    protected void displayAlert(Activity activity, AlertType type, String title, String message) {
        switch (type) {
            case SUCCESS:
                CookieBar.build(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .setBackgroundColor(R.color.bg_success)
                        .setIconAnimation(R.animator.fade)
                        .setIcon(R.drawable.ic_success)
                        .setDuration(3000)
                        .show();
                break;
            case WARNING:
                CookieBar.build(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .setBackgroundColor(R.color.bg_warning)
                        .setIconAnimation(R.animator.fade)
                        .setIcon(R.drawable.ic_warning)
                        .setDuration(3000)
                        .show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(50);
                }
                break;
            case ERROR:
                CookieBar.build(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .setBackgroundColor(R.color.bg_error)
                        .setIcon(R.drawable.ic_error)
                        .setIconAnimation(R.animator.fade)
                        .setDuration(3000)
                        .show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }
                break;
            case INFO:
                CookieBar.build(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .setBackgroundColor(R.color.bg_warning)
                        .setIcon(R.drawable.ic_error)
                        .setIconAnimation(R.animator.fade)
                        .setDuration(3000)
                        .show();
                break;
        }
    }

    protected void displayAlert(Activity activity, AlertType type, String title, String message, CookieBarDismissListener callback) {
        switch (type) {
            case SUCCESS:
                CookieBar.build(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .setBackgroundColor(R.color.bg_success)
                        .setIconAnimation(R.animator.fade)
                        .setIcon(R.drawable.ic_success)
                        .setDuration(3000)
                        .setCookieListener(callback)
                        .show();
                break;
            case WARNING:
                CookieBar.build(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .setBackgroundColor(R.color.bg_warning)
                        .setIconAnimation(R.animator.fade)
                        .setIcon(R.drawable.ic_warning)
                        .setDuration(3000)
                        .setCookieListener(callback)
                        .show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(50);
                }
                break;
            case ERROR:
                CookieBar.build(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .setBackgroundColor(R.color.bg_error)
                        .setIcon(R.drawable.ic_error)
                        .setIconAnimation(R.animator.fade)
                        .setDuration(3000)
                        .setCookieListener(callback)
                        .show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }
                break;
            case INFO:
                CookieBar.build(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .setBackgroundColor(R.color.bg_warning)
                        .setIcon(R.drawable.ic_error)
                        .setIconAnimation(R.animator.fade)
                        .setDuration(3000)
                        .setCookieListener(callback)
                        .show();
                break;
        }
    }

    public enum AlertType {
        SUCCESS,
        WARNING,
        ERROR,
        INFO
    }
}
