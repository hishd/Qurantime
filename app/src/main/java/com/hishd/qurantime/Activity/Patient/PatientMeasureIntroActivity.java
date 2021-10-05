package com.hishd.qurantime.Activity.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hishd.qurantime.Activity.BaseActivity;
import com.hishd.qurantime.Adapters.IntroSliderAdapter;
import com.hishd.qurantime.Adapters.MeasureIntroSliderAdapter;
import com.hishd.qurantime.R;
import com.hishd.qurantime.databinding.ActivityPatientMeasureIntroBinding;

import spencerstudios.com.bungeelib.Bungee;

public class PatientMeasureIntroActivity extends BaseActivity {

    ActivityPatientMeasureIntroBinding binding;

    Animation shrinkEnter;
    Animation animateCaption;

    MeasureIntroSliderAdapter sliderAdapter;

    private TextView[] mDots;

    private int mCurrentPage;

    private String TAG = "PatientMeasureIntroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientMeasureIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupResources();
        setListeners();
    }

    @Override
    protected void setupResources() {
        shrinkEnter = AnimationUtils.loadAnimation(this, R.anim.shrink_enter);
        animateCaption = AnimationUtils.loadAnimation(this, R.anim.zoom_enter);

        binding.btnPrev.setVisibility(View.INVISIBLE);
        binding.btnNextFinish.startAnimation(shrinkEnter);

        sliderAdapter = new MeasureIntroSliderAdapter(this);
        binding.viewPagerIntro.setAdapter(sliderAdapter);
        binding.viewPagerIntro.startAnimation(animateCaption);
        addDotIndicators(0);
        binding.viewPagerIntro.addOnPageChangeListener(viewListener);
    }

    @Override
    protected void setListeners() {
        binding.btnNextFinish.setOnClickListener(v -> {
            if (binding.btnNextFinish.getText().equals("Finish")) {
                Log.d(TAG, "Saving App Intro Measure status");
                appConfig.setInto(true);
                startActivity(new Intent(this, PatientConnectDevice.class));
                Bungee.fade(this);
                finish();
                return;
            }

            binding.viewPagerIntro.setCurrentItem(mCurrentPage + 1);
        });

        binding.btnPrev.setOnClickListener(v -> {
            binding.viewPagerIntro.setCurrentItem(mCurrentPage - 1);
        });

        binding.btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(this, PatientConnectDevice.class));
            Bungee.fade(this);
            finish();
        });
    }

    void addDotIndicators(int position) {
        mDots = new TextView[3];
        binding.linearDots.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.extra_light_blue));

            binding.linearDots.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextSize(50);
            mDots[position].setTextColor(getResources().getColor(R.color.blue));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicators(position);
            mCurrentPage = position;

            if (position == 0) {
                binding.btnNextFinish.setEnabled(true);
                binding.btnPrev.setEnabled(false);
                binding.btnPrev.setVisibility(View.INVISIBLE);

                binding.btnNextFinish.setText("Next");
                binding.btnPrev.setText("");
            } else if (position == mDots.length - 1) {
                binding.btnNextFinish.setEnabled(true);
                binding.btnPrev.setEnabled(true);
                binding.btnPrev.setVisibility(View.VISIBLE);

                binding.btnNextFinish.setText("Finish");
                binding.btnPrev.setText("Prev");
            } else {
                binding.btnNextFinish.setEnabled(true);
                binding.btnPrev.setEnabled(true);
                binding.btnPrev.setVisibility(View.VISIBLE);

                binding.btnNextFinish.setText("Next");
                binding.btnPrev.setText("Prev");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}