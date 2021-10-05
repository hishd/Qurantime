package com.hishd.qurantime.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.hishd.qurantime.R;

import org.jetbrains.annotations.NotNull;

public class IntroSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    TypedArray introImages;

    final String[] captions = {"", "", ""};

    final String[] contents = {"", "", ""};

    public IntroSliderAdapter(Context context) {
        this.context = context;
        this.introImages = context.getResources().obtainTypedArray(R.array.intro_images);
        this.captions[0] = context.getResources().getString(R.string.intro_caption_1);
        this.captions[1] = context.getResources().getString(R.string.intro_caption_2);
        this.captions[2] = context.getResources().getString(R.string.intro_caption_3);

        this.contents[0] = context.getResources().getString(R.string.intro_content_1);
        this.contents[1] = context.getResources().getString(R.string.intro_content_2);
        this.contents[2] = context.getResources().getString(R.string.intro_content_3);
    }

    @Override
    public int getCount() {
        return captions.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater != null ? layoutInflater.inflate(R.layout.layout_intro_slider, container, false) : null;

        ImageView imageView = view.findViewById(R.id.imgViewIntro);
        TextView txtCaption = view.findViewById(R.id.txtCaption);
        TextView txtContent = view.findViewById(R.id.txtContent);

        if (introImages != null) {
            imageView.setImageResource(introImages.getResourceId(position, -1));
        }

        txtCaption.setText(captions[position]);
        txtContent.setText(contents[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
