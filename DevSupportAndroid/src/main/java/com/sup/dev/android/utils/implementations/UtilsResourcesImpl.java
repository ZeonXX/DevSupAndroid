package com.sup.dev.android.utils.implementations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.util.TypedValue;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsColor;

public class UtilsResourcesImpl implements UtilsResources {

    public String getString(@StringRes int r) {
        if (r < 0)
            return null;
        else
            return SupAndroid.di.appContext().getResources().getString(r);
    }

    public String getPlural(@PluralsRes int r, int value) {
        if (r < 0)
            return null;
        else
            return SupAndroid.di.appContext().getResources().getQuantityString(r, value);
    }

    public String[] getStringArray(@ArrayRes int r) {
        if (r < 0)
            return null;
        else
            return SupAndroid.di.appContext().getResources().getStringArray(r);
    }

    public Drawable getDrawable(@DrawableRes int r) {
        if (r < 0)
            return null;
        else
            return SupAndroid.di.appContext().getResources().getDrawable(r);
    }

    public int getColor(@ColorRes int r) {
        if (r < 0)
            return 0;
        else
            return SupAndroid.di.appContext().getResources().getColor(r);
    }

    public int getAccentColor(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    public int getPrimaryColor(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public int getPrimaryDarkColor(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);
        return value.data;
    }

    public int getAccentAlphaColor(Context context) {
        return ToolsColor.setAlpha(106, getAccentColor(context));
    }

    public int getBackgroundColor(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.windowBackground, value, true);
        return value.data;
    }

    public Bitmap getBitmap(@DrawableRes int res) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(SupAndroid.di.appContext().getResources(), res, options);

        if (bitmap != null) return bitmap;

        Bitmap fromDrawable = SupAndroid.di.utilsBitmap().getFromDrawable(getDrawable(res));

        return fromDrawable;
    }


    public Drawable getDrawable(String name) {
        return getDrawable(getDrawableId(name));
    }

    public int getDrawableId(String name) {
        return SupAndroid.di.appContext().getResources().getIdentifier(name, "drawable", SupAndroid.di.appContext().getPackageName());
    }

    public Bitmap getBitmap(String name) {
        return getBitmap(getDrawableId(name));
    }

}
