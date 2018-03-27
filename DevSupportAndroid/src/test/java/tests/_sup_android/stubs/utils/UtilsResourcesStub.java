package tests._sup_android.stubs.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.sup.dev.android.utils.interfaces.UtilsResources;

public class UtilsResourcesStub implements UtilsResources {

    public String string = "test";

    @Override
    public String getString(int r) {
        return string;
    }

    @Override
    public String getPlural(int r, int value) {
        return null;
    }

    @Override
    public String[] getStringArray(int r) {
        return new String[0];
    }

    @Override
    public Drawable getDrawable(int r) {
        return null;
    }

    @Override
    public int getColor(int r) {
        return 0xF0F0F0F0;
    }

    @Override
    public int getAccentColor(Context context) {
        return 0;
    }

    @Override
    public int getBackgroundColor(Context context) {
        return 0xF0F0F0Ff;
    }

    @Override
    public int getAccentAlphaColor(Context context) {
        return 0;
    }

    @Override
    public Bitmap getBitmap(int res) {
        return null;
    }

    @Override
    public Drawable getDrawable(String name) {
        return null;
    }

    @Override
    public int getDrawableId(String name) {
        return 0;
    }

    @Override
    public Bitmap getBitmap(String name) {
        return null;
    }
}
