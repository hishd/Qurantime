package com.hishd.qurantime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.hishd.qurantime.R;

public class MeasureIntroSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    final String[] lottieAnimations = {"power_on.json", "search_device.json", "measure.json"};

    final String[] captions = {"", "", ""};

    final String[] contents = {"", "", ""};

    public MeasureIntroSliderAdapter(Context context) {
        this.context = context;
        this.captions[0] = context.getResources().getString(R.string.measure_caption_1);
        this.captions[1] = context.getResources().getString(R.string.measure_caption_2);
        this.captions[2] = context.getResources().getString(R.string.measure_caption_3);

        this.contents[0] = context.getResources().getString(R.string.measure_content_1);
        this.contents[1] = context.getResources().getString(R.string.measure_content_2);
        this.contents[2] = context.getResources().getString(R.string.measure_content_3);
    }

    @Override
    public int getCount() {
        return captions.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater != null ? layoutInflater.inflate(R.layout.layout_intro_measure_slider, container, false) : null;

        LottieAnimationView animationViewIntro = view.findViewById(R.id.animationViewIntro);
        TextView txtCaption = view.findViewById(R.id.txtCaption);
        TextView txtContent = view.findViewById(R.id.txtContent);

        animationViewIntro.enableMergePathsForKitKatAndAbove(true);
        animationViewIntro.setAnimation(lottieAnimations[position]);
        animationViewIntro.playAnimation();
        txtCaption.setText(captions[position]);
        txtContent.setText(contents[position]);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
