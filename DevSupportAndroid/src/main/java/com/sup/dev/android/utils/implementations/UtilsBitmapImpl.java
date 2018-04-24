package com.sup.dev.android.utils.implementations;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsBitmap;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsColor;
import com.sup.dev.java.tools.ToolsMath;
import com.sup.dev.java.tools.ToolsText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UtilsBitmapImpl implements UtilsBitmap {

    //
    //  Filters
    //

    public Bitmap filter(@DrawableRes int resId, int color) {
        return filter(getFromResources(resId), color);
    }

    public Bitmap filter(@DrawableRes int resId, int color, boolean reduceAlpha) {
        return filter(getFromResources(resId), color, reduceAlpha);
    }

    public Bitmap filter(Bitmap bitmap, int color) {
        return filter(bitmap, color, false);
    }

    public Bitmap filter(Bitmap bitmap, int color, boolean reduceAlpha) {

        if (bitmap == null) return null;

        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(w, h, bitmap.getConfig());
        for (int xx = 0; xx < bitmap.getWidth(); xx++)
            for (int yy = 0; yy < bitmap.getHeight(); yy++) {

                int aa = Color.alpha(bitmap.getPixel(xx, yy));
                if (reduceAlpha) aa -= a;
                if (aa < 0) aa = 0;

                output.setPixel(xx, yy, Color.argb(aa, r, g, b));
            }

        return output;
    }

    public Bitmap filterBlackAndWhite(Bitmap bitmap) {

        if (bitmap == null) return null;

        int[] pixels = new int[bitmap.getHeight() * bitmap.getWidth()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            int n = (ToolsColor.red(pixels[i]) + ToolsColor.green(pixels[i]) + ToolsColor.blue(pixels[i])) / 3;
            pixels[i] = ToolsColor.argb(ToolsColor.alpha(pixels[i]), n, n, n);
        }

        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap;
    }

    public Bitmap filterCircle(Bitmap bitmap) {

        if (bitmap == null) return null;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        float r = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public Bitmap filterShadow(Bitmap bitmap, boolean l, boolean t, boolean r, boolean b) {

        if (bitmap == null) return null;

        int shadowWhite = 0x50202020;
        int ca = Color.alpha(shadowWhite);
        int cr = Color.red(shadowWhite);
        int cg = Color.green(shadowWhite);
        int cb = Color.blue(shadowWhite);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        for (int xx = 0; xx < w; xx++)
            for (int yy = 0; yy < h; yy++) {

                int aP = Color.alpha(bitmap.getPixel(xx, yy)) - ca;
                if (aP <= 0) continue;
                int c = Color.argb(aP, cr, cg, cb);

                if (l && t && xx != 0 && yy != 0) output.setPixel(xx - 1, yy - 1, c);
                if (r && t && xx != w - 1 && yy != 0) output.setPixel(xx + 1, yy - 1, c);
                if (t && yy != 0) output.setPixel(xx, yy - 1, c);
                if (l && b && xx != 0 && yy != h - 1) output.setPixel(xx - 1, yy + 1, c);
                if (r && b && xx != w - 1 && yy != h - 1) output.setPixel(xx + 1, yy + 1, c);
                if (b && yy != h - 1) output.setPixel(xx, yy + 1, c);
                if (l && xx != 0) output.setPixel(xx - 1, yy, c);
                if (r && xx != w - 1) output.setPixel(xx + 1, yy, c);
            }

        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());

        return output;
    }

    public Bitmap filterHalo(Bitmap bitmap) {
        return filterShadow(bitmap, true, true, true, true);
    }

    public Bitmap filterAlphaIncrement(@DrawableRes int resId, int alphaIncrement) {
        return filterAlphaIncrement(getFromResources(resId), alphaIncrement);
    }

    public Bitmap filterAlphaIncrement(Bitmap bitmap, int alphaIncrement) {

        if (bitmap == null) return null;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        for (int xx = 0; xx < bitmap.getWidth(); xx++)
            for (int yy = 0; yy < bitmap.getHeight(); yy++) {
                int pixel = bitmap.getPixel(xx, yy);

                int a = Color.alpha(pixel);
                a += alphaIncrement;
                if (a < 0) a = 0;

                output.setPixel(xx, yy, Color.argb(a, Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
            }

        return output;
    }

    //
    //  Get
    //

    public Bitmap decode(byte[] bytes) {
        if (bytes == null) return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public Bitmap decode(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes == null) return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    }

    public void getFromGallery(Callback1<Bitmap> onLoad, Callback onError, Callback onPermissionPermissionRestriction) {
        SupAndroid.di.mvpActivity(
                mvpActivity -> SupAndroid.di.utilsIntent().getGalleryImage(uri -> getFromUri((Activity) mvpActivity, uri, bitmap -> {
                    if (bitmap == null)
                        onError.callback();
                    else
                        onLoad.callback(bitmap);
                }, onPermissionPermissionRestriction), onError));
    }

    public Bitmap getFromDrawable(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        w = w < 1 ? 1 : 0;
        w = w < 1 ? 1 : 0;

        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;

    }

    public Bitmap getFromResources(@DrawableRes int res) {
        return SupAndroid.di.utilsResources().getBitmap(res);
    }

    public Bitmap getFromURL(final String src) throws IOException {
        Debug.log("b1");
        URL url = ToolsText.makeUrl(src);
        Debug.log("b2");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(4000);
        Debug.log("b3");
        connection.setDoInput(true);
        Debug.log("b4");
        connection.connect();
        Debug.log("b5");
        InputStream input = connection.getInputStream();
        Debug.log("b6");
        return BitmapFactory.decodeStream(input);
    }

    public void getFromURL(final String src, final Callback1<Bitmap> bitmapListener) {
        SupAndroid.di.utilsThreads().thread(() -> {
            Bitmap bitmap = null;
            try {
                bitmap = getFromURL(src);
            } catch (IOException ex) {
                Debug.log(ex);
            }
            final Bitmap fBitmap = bitmap;
            SupAndroid.di.utilsThreads().main(() -> bitmapListener.callback(fBitmap));
        });
    }

    public void getFromUri(Activity activity, final Uri uri, final Callback1<Bitmap> callbackResult, Callback onPermissionPermissionRestriction) {
        SupAndroid.di.utilsPermission().requestReadPermission(activity, () -> {
            try {
                callbackResult.callback(MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri));
            } catch (IOException ex) {
                Debug.log(ex);
                callbackResult.callback(null);
            }
        }, onPermissionPermissionRestriction);
    }

    //
    //  To
    //

    public byte[] toBytes(Bitmap bitmap, int maxBytesSize) {
        boolean containsAlpha = false;
        for (int x = 0; x < bitmap.getWidth(); x++)
            for (int y = 0; y < bitmap.getHeight(); y++)
                if (ToolsColor.alpha(bitmap.getPixel(x, y)) != 255) {
                    containsAlpha = true;
                    break;
                }

        if (containsAlpha) {
            byte[] bytes = toPNGBytes(bitmap);
            if (bytes.length <= maxBytesSize)
                return bytes;
        }

        int q = 100;
        byte[] bytes = toJPGBytes(bitmap, 100);
        while (bytes.length > maxBytesSize) {
            if (q == 2) throw new IllegalArgumentException("Can't convert to JPG");
            q -= 2;
            bytes = toJPGBytes(bitmap, q);
        }
        return bytes;
    }

    public byte[] toJPGBytes(Bitmap bitmap, int q) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, q, stream);
        return stream.toByteArray();
    }

    public byte[] toPNGBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public byte[] drawableToBytes(@DrawableRes int resId) {
        Drawable d = SupAndroid.di.appContext().getResources().getDrawable(resId);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    //
    //  Sizes
    //

    public Bitmap keepMaxSides(Bitmap bitmap, int maxSideSize) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w <= maxSideSize && h <= maxSideSize)
            return bitmap;
        float arg = ToolsMath.max(w, h) / (float) maxSideSize;
        return Bitmap.createScaledBitmap(bitmap, (int) (w / arg), (int) (h / arg), true);
    }


}