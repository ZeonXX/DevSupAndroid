package com.sup.dev.android.utils.interfaces;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.java.classes.callbacks.simple.Callback;

public interface UtilsView {

    int ANIMATION_TIME = 300;

    int[] viewPointAsScreenPoint(View view, int x, int y);

    boolean checkHit(View view, float x, float y);

    <K extends View> K inflate(Context viewContext, @LayoutRes int res);

    View inflate(@NonNull ViewGroup parent, @LayoutRes int res);

    ViewGroup getRootParent(View v);

    <K extends View> K findViewOnParents(View v, int viewId) ;

    void makeTransparentAppBar(Activity activity);

    int pxToDp(float dp);

    int dpToPx(float dp);

    int spToPx(float sp);

    //
    //  Keyboard
    //

    void hideKeyboard(Activity activity);

    void hideKeyboard(View view);

    void showKeyboard(final View view);


    //
    //  Animation
    //

    void setTextAnimate(TextView v, String text);

    void setTextAnimate(TextView v, String text, Callback onAlpha);


    void crossfade(View in, View out);

    void clearAnimation(View v);

    void alpha(View v, boolean toAlpha);

    void alpha(View v, boolean toAlpha, Callback onAlpha);

    //
    //  From Alpha
    //

    void fromAlpha(View v);

    void fromAlpha(View v, int time);

    void fromAlpha(View v, Callback onFinish);

    void fromAlpha(View v, int time, Callback onFinish);

    //
    //  To Alpha
    //

    void toAlpha(View v);

    void toAlpha(View v, int time);

    void toAlpha(View v, Callback onFinish);

    void toAlpha(View v, int time, Callback onFinish) ;

    //
    //  Target alpha
    //

    void targetAlpha(View v, float alpha);

}
