package com.sup.dev.android.views.animations;

import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.Subscription;
import com.sup.dev.java.classes.animation.AnimationSpring;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.tools.ToolsThreads;

public class AnimationViewHideVertical {

    private final AnimationSpring spring;
    private final View view;

    private Callback1<Boolean> onVisibleChange;
    private Callback1<Boolean> onVisibleStartChange;
    private long animationTime = ToolsView.ANIMATION_TIME;
    private long autoHideMs = 0;

    private boolean shoved = true;
    private Subscription subscriptionAutoHide;
    private Subscription subscriptionAnimation;
    private int lastH = 0;

    public AnimationViewHideVertical(View view) {
        this.view = view;

        spring = new AnimationSpring(0, AnimationSpring.SpeedType.TIME_MS, animationTime);

        view.addOnLayoutChangeListener((view1, i, i1, i2, i3, i4, i5, i6, i7) -> {
            if(lastH == view.getHeight())return;
            lastH = view.getHeight();
            setShoved(shoved);
        });
    }

    public void switchShow() {
        switchShow(true);
    }

    public void switchShow(boolean animated) {
        if (shoved)
            hide(animated);
        else
            show(animated);
    }

    public void show() {
        show(true);
    }

    public void show(boolean animated) {

        if (shoved) return;

        shoved = true;
        spring.setSpeed(AnimationSpring.SpeedType.TIME_MS, animated ? animationTime : 0);
        updateAutoHide();
        setShoved(true);
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean animated) {

        if (!shoved) return;

        shoved = false;
        spring.setSpeed(AnimationSpring.SpeedType.TIME_MS, animated ? animationTime : 0);
        updateAutoHide();
        setShoved(false);
    }

    private void setShoved(boolean b) {

        if (onVisibleStartChange != null) onVisibleStartChange.callback(b);
        if (subscriptionAnimation != null) subscriptionAnimation.unsubscribe();

        spring.to(b ? 0 : view.getHeight());

        subscriptionAnimation = ToolsThreads.timerMain(17, subscription -> {
           spring.update();
            view.setY(spring.getValue());
            if (!spring.isNeedUpdate()) {
                subscription.unsubscribe();
                if (onVisibleChange != null) onVisibleChange.callback(shoved);
            }
        });
    }

    private void updateAutoHide() {

        if (subscriptionAutoHide != null) subscriptionAutoHide.unsubscribe();

        if (autoHideMs > 0) subscriptionAutoHide = ToolsThreads.main(autoHideMs, () -> hide());

    }

    //
    //  Setters
    //

    public void setOnVisibleStartChange(Callback1<Boolean> onVisibleStartChange) {
        this.onVisibleStartChange = onVisibleStartChange;
    }

    public void setOnVisibleChange(Callback1<Boolean> onVisibleChange) {
        this.onVisibleChange = onVisibleChange;
    }

    public void setAutoHide(long autoHideMs) {
        this.autoHideMs = autoHideMs;
        updateAutoHide();
    }

    public void setAnimationTime(long animationTime) {
        this.animationTime = animationTime;
    }

    //
    //  Getters
    //

    public boolean isShoved() {
        return shoved;
    }
}
