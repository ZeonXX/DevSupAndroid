package com.sup.dev.android.tools;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.magic_box.AndroidBug5497Workaround;
import com.sup.dev.android.views.widgets.WidgetProgressTransparent;
import com.sup.dev.android.views.widgets.WidgetProgressWithTitle;
import com.sup.dev.android.views.dialogs.Dialog;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback3;
import com.sup.dev.java.classes.items.Item;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ToolsView {

    public static final int ANIMATION_TIME = 300;

    public static void recyclerHideFabWhenScrollEnd(RecyclerView vRecycler, FloatingActionButton vFab){
        vRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (vRecycler.computeVerticalScrollOffset() != 0
                        && vRecycler.computeVerticalScrollOffset() + 50 >= vRecycler.computeVerticalScrollRange() - vRecycler.computeVerticalScrollExtent())
                    vFab.hide();
                else vFab.show();
            }

        });
    }

    public static View removeFromParent(View view) {
        if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
        return view;
    }

    public static Dialog showProgressDialog() {
        return new WidgetProgressTransparent()
                .setCancelable(false)
                .asDialogShow();
    }

    public static Dialog showProgressDialog(int title) {
        return showProgressDialog(ToolsResources.getString(title));
    }

    public static Dialog showProgressDialog(String title) {
        return new WidgetProgressWithTitle()
                .setTitle(title)
                .setCancelable(false)
                .asDialogShow();
    }


    public static void setTextOrGone(TextView vText, CharSequence text) {
        vText.setText(text);
        vText.setVisibility(ToolsText.empty(text) ? GONE : VISIBLE);
    }

    public static void setOnClickCoordinates(View v, Callback3<View, Integer, Integer> onClick) {

        Item<Integer> clickScreenX = new Item<>();
        Item<Integer> clickScreenY = new Item<>();

        v.setOnTouchListener((v1, event) -> {
            clickScreenX.a = (int) event.getX();
            clickScreenY.a = (int) event.getY();
            return false;
        });

        v.setOnClickListener(v1 -> {
            if (onClick != null) onClick.callback(v, clickScreenX.a == null ? 0 : clickScreenX.a, clickScreenY.a == null ? 0 : clickScreenY.a);
        });



    }

    public static void setOnLongClickCoordinates(View v, Callback3<View, Integer, Integer> onClick) {

        Item<Integer> clickScreenX = new Item<>();
        Item<Integer> clickScreenY = new Item<>();

        v.setOnTouchListener((v1, event) -> {
            clickScreenX.a = (int) event.getX();
            clickScreenY.a = (int) (event.getY());
            return false;
        });

        v.setOnLongClickListener(v1 -> {
            if (onClick != null) onClick.callback(v, clickScreenX.a, clickScreenY.a);
            return true;
        });
    }

    public static int[] viewPointAsScreenPoint(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        location[0] = location[0] + x;
        location[1] = location[1] + y;
        return location;
    }

    public static boolean checkHit(View view, float x, float y) {

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int l = location[0];
        int t = location[1];
        int r = l + view.getWidth();
        int b = t + view.getHeight();

        return x >= l && y >= t && x <= r && y <= b;
    }

    public static <K extends View> K inflate(@LayoutRes int res) {
        return (K) LayoutInflater.from(SupAndroid.activity).inflate(res, null, false);
    }

    public static <K extends View> K inflate(Context viewContext, @LayoutRes int res) {
        return (K) LayoutInflater.from(viewContext).inflate(res, null, false);
    }

    public static View inflate(@NonNull ViewGroup parent, @LayoutRes int res) {
        return LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
    }

    public static ViewGroup getRootParent(View v) {
        if (v.getParent() == null || !(v.getParent() instanceof View))
            return v instanceof ViewGroup ? (ViewGroup) v : null;
        else
            return getRootParent((View) v.getParent());
    }

    public static int getRootBackground(View v) {
        if (v.getBackground() instanceof ColorDrawable) return ((ColorDrawable) v.getBackground()).getColor();
        if (v.getParent() == null || !(v.getParent() instanceof View))
            return 0;
        else
            return getRootBackground((View) v.getParent());
    }


    public static <K extends View> K findViewOnParents(View v, int viewId) {

        K fView = v.findViewById(viewId);
        if (fView != null) return fView;

        if (v.getParent() == null || !(v.getParent() instanceof View))
            return null;
        else
            return findViewOnParents((View) v.getParent(), viewId);
    }

    public static void makeTransparentAppBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            AndroidBug5497Workaround.assistActivity(activity);
        }
    }

    public static int pxToDp(float px) {
        return (int) (px * (px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().getDisplayMetrics())));
    }

    public static int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

    //
    //  Keyboard
    //

    public static void hideKeyboard() {
        View currentFocus = SupAndroid.activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) SupAndroid.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(final View view) {
        ToolsThreads.main(350, () -> {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        });
    }


    //
    //  Animation
    //

    public static void setTextAnimate(TextView v, String text) {
        setTextAnimate(v, text, null);
    }

    public static void setTextAnimate(TextView v, String text, Callback onAlpha) {

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


    public static void crossfade(View in, View out) {
        fromAlpha(in);
        toAlpha(out);
    }

    public static void clearAnimation(View v) {
        v.animate().setListener(null);
        if (v.getAnimation() != null)
            v.getAnimation().cancel();
    }

    public static void alpha(View v, boolean toAlpha) {
        alpha(v, toAlpha, null);
    }

    public static void alpha(View v, boolean toAlpha, Callback onAlpha) {
        if (toAlpha)
            toAlpha(v, onAlpha);
        else
            fromAlpha(v, onAlpha);
    }

    //
    //  From Alpha
    //

    public static void fromAlpha(View v) {
        fromAlpha(v, ANIMATION_TIME);
    }

    public static void fromAlpha(View v, int time) {
        fromAlpha(v, time, null);
    }

    public static void fromAlpha(View v, Callback onFinish) {
        fromAlpha(v, ANIMATION_TIME, onFinish);
    }

    public static void fromAlpha(View v, int time, Callback onFinish) {

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

    public static void toAlpha(View v) {
        toAlpha(v, ANIMATION_TIME);
    }

    public static void toAlpha(View v, int time) {
        toAlpha(v, time, null);
    }

    public static void toAlpha(View v, Callback onFinish) {
        toAlpha(v, ANIMATION_TIME, onFinish);
    }

    public static void toAlpha(View v, int time, Callback onFinish) {

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

    public static void targetAlpha(View v, float alpha) {

        clearAnimation(v);

        long time = (long) ((255 / ANIMATION_TIME) * Math.abs(v.getAlpha() - alpha));

        v.animate()
                .alpha(alpha)
                .setDuration(time)
                .setInterpolator(new LinearOutSlowInInterpolator());
    }

}
