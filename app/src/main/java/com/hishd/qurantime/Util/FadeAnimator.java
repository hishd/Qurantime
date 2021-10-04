package com.hishd.qurantime.Util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

public class FadeAnimator {
    public static void fadeOutAnimation(View viewToFadeOut) {
        if(viewToFadeOut.getVisibility() == View.GONE) {
            return;
        }
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(viewToFadeOut, "alpha", 1f, 0f);

        fadeOut.setDuration(800);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // We wanna set the view to GONE, after it's fade out. so it actually disappear from the layout & don't take up space.
                viewToFadeOut.setVisibility(View.GONE);
            }
        });

        fadeOut.start();
    }

    public static void fadeInAnimation(View viewToFadeIn) {
        if(viewToFadeIn.getVisibility() == View.VISIBLE) {
            return;
        }
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(viewToFadeIn, "alpha", 0f, 1f);
        fadeIn.setDuration(800);

        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // We wanna set the view to VISIBLE, but with alpha 0. So it appear invisible in the layout.
                viewToFadeIn.setVisibility(View.VISIBLE);
                viewToFadeIn.setAlpha(0);
            }
        });

        fadeIn.start();
    }
}
