package com.sup.dev.android.tools;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

public class ToolsAnimator{

    public static ValueAnimator flicker(View view, int color1, int color2, int duration) {

        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);
        anim.addUpdateListener(animator -> view.setBackgroundColor((int) animator.getAnimatedValue()));
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setDuration(duration);
        anim.start();

        return anim;
    }

    public static ValueAnimator flickerFilter(ImageView view, int color1, int color2, int duration) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);
        anim.addUpdateListener(animator -> view.setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.SRC_IN));
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setDuration(duration);
        anim.start();

        return anim;
    }
}
