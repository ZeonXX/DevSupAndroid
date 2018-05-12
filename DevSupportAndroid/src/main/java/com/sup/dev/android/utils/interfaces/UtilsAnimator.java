package com.sup.dev.android.utils.interfaces;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;

public interface UtilsAnimator {


    ValueAnimator flicker(View view, int color1, int color2, int duration);

    ValueAnimator flickerFilter(ImageView view, int color1, int color2, int duration);


}
