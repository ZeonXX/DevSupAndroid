package com.sup.dev.android.utils.interfaces;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

public interface UtilsResources {

    String getString(@StringRes int r);

    String getPlural(@PluralsRes int r, int value);

    String[] getStringArray(@ArrayRes int r);

    Drawable getDrawable(@DrawableRes int r);

    int getColor(@ColorRes int r);

    int getAccentColor(Context context);

    int getBackgroundColor(Context context);

    int getAccentAlphaColor(Context context);

    Bitmap getBitmap(@DrawableRes int res);

    Drawable getDrawable(String name);

    int getDrawableId(String name);

    Bitmap getBitmap(String name);


}
