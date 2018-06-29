package com.sup.dev.android.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.DrawableRes;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.Navigator;
import com.sup.dev.android.views.screens.SCrop;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.geometry.Dimensions;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsColor;
import com.sup.dev.java.tools.ToolsMath;
import com.sup.dev.java.tools.ToolsText;
import com.sup.dev.java.tools.ToolsThreads;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ToolsBitmap {

    private static String errorCantLoadImage = SupAndroid.TEXT_ERROR_CANT_LOAD_IMAGE;
    private static String errorPermissionFiles = SupAndroid.TEXT_ERROR_PERMISSION_READ_FILES;

    public static Bitmap cropCenterSquare(Bitmap srcBmp) {
        if (srcBmp.getWidth() == srcBmp.getHeight()) return srcBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()) return Bitmap.createBitmap(srcBmp, srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2, 0, srcBmp.getHeight(), srcBmp.getHeight());
        else return Bitmap.createBitmap(srcBmp, 0, srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2, srcBmp.getWidth(), srcBmp.getWidth());
    }


    //
    //  Filters
    //

    public static Bitmap filter(@DrawableRes int resId, int color) {
        return filter(getFromResources(resId), color);
    }

    public static Bitmap filter(@DrawableRes int resId, int color, boolean reduceAlpha) {
        return filter(getFromResources(resId), color, reduceAlpha);
    }

    public static Bitmap filter(Bitmap bitmap, int color) {
        return filter(bitmap, color, false);
    }

    public static Bitmap filter(Bitmap bitmap, int color, boolean reduceAlpha) {

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

    public static Bitmap filterBlur(Bitmap bitmap, float arg) {
        RenderScript rs = RenderScript.create(SupAndroid.appContext);
        final Allocation input = Allocation.createFromBitmap(rs, bitmap);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(arg);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;
    }

    public static Bitmap filterBlackAndWhite(Bitmap bitmap) {

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

    public static Bitmap filterCircle(Bitmap bitmap) {

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

    public static Bitmap filterShadow(Bitmap bitmap, boolean l, boolean t, boolean r, boolean b) {

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

    public static Bitmap filterHalo(Bitmap bitmap) {
        return filterShadow(bitmap, true, true, true, true);
    }

    public static Bitmap filterAlphaIncrement(@DrawableRes int resId, int alphaIncrement) {
        return filterAlphaIncrement(getFromResources(resId), alphaIncrement);
    }

    public static Bitmap filterAlphaIncrement(Bitmap bitmap, int alphaIncrement) {

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

    public static Bitmap decode(byte[] bytes) {
        if (bytes == null) return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap decode(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes == null) return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    }

    public static Bitmap decode(byte[] bytes, int w, int h, BitmapFactory.Options options, boolean minSizes) {

        if (bytes == null) return null;
        if (options == null) options = new BitmapFactory.Options();

        if (w != 0 || h != 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            options.inSampleSize = 1;
            while (options.outHeight / options.inSampleSize > h || options.outWidth / options.inSampleSize > w)
                options.inSampleSize *= 2;
        }

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        if (w != 0 || h != 0) {
            Dimensions inscribe = minSizes ? ToolsMath.inscribeMin(bitmap.getWidth(), bitmap.getHeight(), w, h) : ToolsMath.inscribe(bitmap.getWidth(), bitmap.getHeight(), w, h);
            if (bitmap.getWidth() != inscribe.w || bitmap.getHeight() != inscribe.h)
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) inscribe.w, (int) inscribe.h, true);
        }

        return bitmap;
    }

    public static void getFromGallery(Callback1<Bitmap> onLoad, Callback onError, Callback onPermissionPermissionRestriction) {
        ToolsIntent.getGalleryImage(uri -> getFromUri(uri, bitmap -> {
            if (bitmap == null)
                onError.callback();
            else
                onLoad.callback(bitmap);
        }, onPermissionPermissionRestriction), onError);
    }

    public static Bitmap getFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            else bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);


            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            Debug.log(e);
            return null;
        }

    }

    public static Bitmap getFromResources(@DrawableRes int res) {
        return ToolsResources.getBitmap(res);
    }

    public static Bitmap getFromURL(final String src) throws IOException {
        URL url = ToolsText.makeUrl(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(4000);
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

    public static void getFromURL(final String src, final Callback1<Bitmap> bitmapListener) {
        ToolsThreads.thread(() -> {
            Bitmap bitmap = null;
            try {
                bitmap = getFromURL(src);
            } catch (IOException ex) {
                Debug.log(ex);
            }
            final Bitmap fBitmap = bitmap;
            ToolsThreads.main(() -> bitmapListener.callback(fBitmap));
        });
    }

    public static void getFromUri(final Uri uri, final Callback1<Bitmap> callbackResult, Callback onPermissionPermissionRestriction) {
        ToolsPermission.requestReadPermission(() -> {
            try {
                callbackResult.callback(MediaStore.Images.Media.getBitmap(SupAndroid.activity.getContentResolver(), uri));
            } catch (IOException ex) {
                Debug.log(ex);
                callbackResult.callback(null);
            }
        }, onPermissionPermissionRestriction);
    }

    public static void getFromGalleryCropped(int ratioW, int ratioH, boolean autoBackOnCrop, Callback2<SCrop, Bitmap> onComplete) {
        if (errorCantLoadImage == null) throw new RuntimeException("You must call ToolsBitmap.init");
        getFromGallery(bitmap -> Navigator.to(new SCrop(bitmap, ratioW, ratioH, onComplete).setAutoBackOnCrop(autoBackOnCrop)),
                () -> ToolsToast.show(errorCantLoadImage),
                () -> ToolsToast.show(errorPermissionFiles));
    }

    public static void getFromGalleryCroppedAndScaled(int w, int h, boolean autoBackOnCrop, Callback2<SCrop, Bitmap> onComplete) {
        getFromGalleryCropped(w, h, autoBackOnCrop, (pCrop, bitmap) -> onComplete.callback(pCrop, Bitmap.createScaledBitmap(bitmap, w, h, true)));
    }


    //
    //  To
    //

    public static byte[] toBytes(Bitmap bitmap, int maxBytesSize) {
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

    public static byte[] toJPGBytes(Bitmap bitmap, int q) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, q, stream);
        return stream.toByteArray();
    }

    public static byte[] toPNGBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static byte[] drawableToBytes(@DrawableRes int resId) {
        Drawable d = SupAndroid.appContext.getResources().getDrawable(resId);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    //
    //  Sizes
    //

    public static Bitmap keepMaxSides(Bitmap bitmap, int maxSideSize) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w <= maxSideSize && h <= maxSideSize)
            return bitmap;
        float arg = ToolsMath.max(w, h) / (float) maxSideSize;
        return Bitmap.createScaledBitmap(bitmap, (int) (w / arg), (int) (h / arg), true);
    }


}
