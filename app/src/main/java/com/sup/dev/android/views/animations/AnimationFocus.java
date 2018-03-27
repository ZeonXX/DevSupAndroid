package com.sup.dev.android.views.animations;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.sup.dev.java.classes.animation.AnimationPendulum;
import com.sup.dev.java.classes.animation.AnimationPendulumColor;
import com.sup.dev.java.classes.animation.AnimationSpringColor;
import com.sup.dev.java.libs.debug.Debug;

public class AnimationFocus implements View.OnTouchListener {

    private final View view;
    private final AnimationSpringColor animationFocus;
    private final AnimationPendulumColor animationClick;
    private final int focusColor;
    private final int focusColorClick;
    private final int focusColorAlpha;

    public AnimationFocus(View view, int focusColor) {
        this.view = view;
        focusColorClick = focusColor;
        this.focusColor = Color.argb((int) (Color.alpha(focusColor) / 1.5f), Color.red(focusColor), Color.green(focusColor), Color.blue(focusColor));
        this.focusColorAlpha = Color.argb(0, Color.red(focusColor), Color.green(focusColor), Color.blue(focusColor));

        animationFocus = new AnimationSpringColor(focusColorAlpha, 200);
        animationClick = new AnimationPendulumColor(focusColorAlpha, focusColorAlpha, focusColorClick, 150, AnimationPendulum.AnimationType.TO_2_AND_BACK);

        resetOnFocusChangedListener();
        resetTouchListener();
    }

    public void resetTouchListener(){
        view.setOnTouchListener(this);
    }

    public void resetOnFocusChangedListener(){
        view.setOnFocusChangeListener((view1, b) -> updateFocusColor());
    }

    private boolean touched;


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!view.isEnabled() || !view.isClickable()) {
            touched = false;
            updateFocusColor();
            return view.onTouchEvent(event);
        }


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touched = true;
            updateFocusColor();
        }

        if (event.getAction() == MotionEvent.ACTION_UP && event.getX() >= 0 && event.getX() <= view.getWidth() && event.getY() >= 0 && event.getY() <= view.getHeight()) {
            animationClick.set(animationFocus.getColor());
            animationClick.to_2();
        }


        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL
                || event.getX() < 0 || event.getX() > view.getWidth() || event.getY() < 0 || event.getY() > view.getHeight()) {
            touched = false;
            updateFocusColor();
        }

        view.onTouchEvent(event);
        return true;
    }


    public void updateFocusColor() {
        animationFocus.to((view.isFocused() || touched) ? focusColor : focusColorAlpha);
        view.invalidate();
    }

    public int update() {

        animationFocus.update();
        animationClick.update();

        int color = animationClick.getA().getValue() > animationFocus.getA().getValue() ? animationClick.getColor() : animationFocus.getColor();

        if (animationFocus.isNeedUpdate() || animationClick.isNeedUpdate())
            view.invalidate();

        return color;
    }

    public void setClickAnimationEnabled(boolean b) {
        if (b)
            animationClick.set(focusColorAlpha, focusColorClick);
        else
            animationClick.set(0x00000000, 0x00000000);
    }

}
