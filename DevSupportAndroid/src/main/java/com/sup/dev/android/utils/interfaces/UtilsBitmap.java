package com.sup.dev.android.utils.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

import java.io.IOException;

public interface UtilsBitmap {

    //
    //  Filters
    //

    Bitmap filter(@DrawableRes int resId, int color);

    Bitmap filter(@DrawableRes int resId, int color, boolean reduceAlpha);

    Bitmap filter(Bitmap bitmap, int color);

    Bitmap filter(Bitmap bitmap, int color, boolean reduceAlpha);

    Bitmap filterBlackAndWhite(Bitmap bitmap);

    Bitmap filterCircle(Bitmap bitmap);

    Bitmap filterShadow(Bitmap bitmap, boolean l, boolean t, boolean r, boolean b);

    Bitmap filterHalo(Bitmap bitmap);

    Bitmap filterAlphaIncrement(@DrawableRes int resId, int alphaIncrement);

    Bitmap filterAlphaIncrement(Bitmap bitmap, int alphaIncrement);

    //
    //  Get
    //

    Bitmap decode(byte[] bytes);

    Bitmap decode(byte[] bytes, BitmapFactory.Options opts);

    void getFromGallery(Callback1<Bitmap> onLoad, Callback onError, Callback onPermissionPermissionRestriction);

    Bitmap getFromDrawable(Drawable drawable);

    Bitmap getFromResources(@DrawableRes int res);

    Bitmap getFromURL(final String src) throws IOException;

    void getFromURL(final String src, final Callback1<Bitmap> bitmapListener);

    void getFromUri(Activity activity, final Uri uri, final Callback1<Bitmap> callbackResult, Callback onPermissionPermissionRestriction);

    //
    //  To
    //

    byte[] toBytes(Bitmap bitmap, int maxBytesSize);

    byte[] toJPGBytes(Bitmap bitmap, int q);

    byte[] toPNGBytes(Bitmap bitmap);

    byte[] drawableToBytes(@DrawableRes int resId);

    //
    //  Sizes
    //

    Bitmap keepMaxSides(Bitmap bitmap, int maxSideSize);

}
