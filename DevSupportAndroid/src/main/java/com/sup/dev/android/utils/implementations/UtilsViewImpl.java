package com.sup.dev.android.utils.implementations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.AnyThread;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.magic_box.AndroidBug5497Workaround;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UtilsViewImpl implements UtilsView {

    public int[] viewPointAsScreenPoint(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        location[0] = location[0] + x;
        location[1] = location[1] + y;
        return location;
    }

    public boolean checkHit(View view, float x, float y) {

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int l = location[0];
        int t = location[1];
        int r = l + view.getWidth();
        int b = t + view.getHeight();

        return x >= l && y >= t && x <= r && y <= b;
    }

    public <K extends View> K inflate(Context viewContext, @LayoutRes int res) {
        return (K) LayoutInflater.from(viewContext).inflate(res, null, false);
    }

    public View inflate(@NonNull ViewGroup parent, @LayoutRes int res) {
        return LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
    }

    public ViewGroup getRootParent(View v) {
        if (v.getParent() == null || !(v.getParent() instanceof View))
            return v instanceof ViewGroup ? (ViewGroup) v : null;
        else
            return getRootParent((View) v.getParent());
    }

    public <K extends View> K findViewOnParents(View v, int viewId) {

        K fView = v.findViewById(viewId);
        if (fView != null) return fView;

        if (v.getParent() == null || !(v.getParent() instanceof View))
            return null;
        else
            return findViewOnParents((View) v.getParent(), viewId);
    }

    public void makeTransparentAppBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            AndroidBug5497Workaround.assistActivity(activity);
        }

    }

    @Override
    public int pxToDp(float px) {
        return (int) (px*(px/TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().getDisplayMetrics())));
    }

    public int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

    //
    //  Keyboard
    //

    @MainThread
    public void hideKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @MainThread
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @AnyThread
    public void showKeyboard(final View view) {
        SupAndroid.di.utilsThreads().thread(() -> {
            SupAndroid.di.utilsThreads().sleep(350);
            SupAndroid.di.utilsThreads().main(() -> {
                view.requestFocus();
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            });
        });
    }


    //
    //  Animation
    //

    public void setTextAnimate(TextView v, String text) {
        setTextAnimate(v, text, null);
    }

    public void setTextAnimate(TextView v, String text, Callback onAlpha) {

        if (v.getText().equals(text)) {
            fromAlpha(v);
            return;
        }

        toAlpha(v, () -> {
            v.setText(text);
            if (onAlpha != null) onAlpha.callback();
            fromAlpha(v);
        });
    }


    public void crossfade(View in, View out) {
        fromAlpha(in);
        toAlpha(out);
    }

    public void clearAnimation(View v) {
        v.animate().setListener(null);
        if (v.getAnimation() != null)
            v.getAnimation().cancel();
    }

    public void alpha(View v, boolean toAlpha) {
        alpha(v, toAlpha, null);
    }

    public void alpha(View v, boolean toAlpha, Callback onAlpha) {
        if (toAlpha)
            toAlpha(v, onAlpha);
        else
            fromAlpha(v, onAlpha);
    }

    //
    //  From Alpha
    //

    public void fromAlpha(View v) {
        fromAlpha(v, ANIMATION_TIME);
    }

    public void fromAlpha(View v, int time) {
        fromAlpha(v, time, null);
    }

    public void fromAlpha(View v, Callback onFinish) {
        fromAlpha(v, ANIMATION_TIME, onFinish);
    }

    public void fromAlpha(View v, int time, Callback onFinish) {

        clearAnimation(v);

        if (v.getAlpha() == 1 && (v.getVisibility() == GONE || v.getVisibility() == View.INVISIBLE))
            v.setAlpha(0);

        v.setVisibility(VISIBLE);

        v.animate()
                .alpha(1f)
                .setDuration(time)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setListener(new AnimatorListenerAdapter() {

                    public void onAnimationEnd(Animator animation) {
                        if (onFinish != null)
                            onFinish.callback();
                    }
                });

    }

    //
    //  To Alpha
    //

    public void toAlpha(View v) {
        toAlpha(v, ANIMATION_TIME);
    }

    public void toAlpha(View v, int time) {
        toAlpha(v, time, null);
    }

    public void toAlpha(View v, Callback onFinish) {
        toAlpha(v, ANIMATION_TIME, onFinish);
    }

    public void toAlpha(View v, int time, Callback onFinish) {

        clearAnimation(v);

        v.animate()
                .alpha(0f)
                .setDuration(time)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setListener(new AnimatorListenerAdapter() {

                    public void onAnimationEnd(Animator animation) {
                        v.animate().setListener(null);
                        v.setAlpha(1);
                        v.setVisibility(View.INVISIBLE);
                        if (onFinish != null)
                            onFinish.callback();
                    }
                });
    }

    //
    //  Target alpha
    //

    public void targetAlpha(View v, float alpha) {

        clearAnimation(v);

        long time = (long) ((255 / ANIMATION_TIME) * Math.abs(v.getAlpha() - alpha));

        v.animate()
                .alpha(alpha)
                .setDuration(time)
                .setInterpolator(new LinearOutSlowInInterpolator());
    }

}
