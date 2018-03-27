package tests._sup_android.stubs.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.sup.dev.android.utils.interfaces.UtilsBitmap;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.classes.items.Pair;

import java.io.IOException;
import java.util.ArrayList;

public class UtilsBitmapStub implements UtilsBitmap {

    public ArrayList<Pair<Integer, Integer>> filterResCalls = new ArrayList<>();
    public ArrayList<Pair<Bitmap, Integer>> filterCalls = new ArrayList<>();

    @Override
    public Bitmap filter(int resId, int color) {
        filterResCalls.add(new Pair<>(resId, color));
        return Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);
    }

    @Override
    public Bitmap filter(Bitmap source, int color) {
        filterCalls.add(new Pair<>(source, color));
        return Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);
    }

    @Override
    public Bitmap filter(Bitmap source, int color, boolean reduceAlpha) {
        return null;
    }

    @Override
    public Bitmap filterBlackAndWhite(Bitmap bitmap) {
        return null;
    }

    @Override
    public Bitmap filterCircle(Bitmap source) {
        return null;
    }

    @Override
    public Bitmap filterShadow(Bitmap source, boolean l, boolean t, boolean r, boolean b) {
        return null;
    }

    @Override
    public Bitmap filterHalo(Bitmap source) {
        return null;
    }

    @Override
    public Bitmap decode(byte[] bytes) {
        return null;
    }

    @Override
    public Bitmap decode(byte[] bytes, BitmapFactory.Options opts) {
        return null;
    }

    @Override
    public void getFromGallery(CallbackSource<Bitmap> onLoad, Callback onError, Callback onPermissionPermissionRestriction) {

    }

    @Override
    public void getFromGalleryCropped(int ratioW, int ratioH, CallbackSource<Bitmap> onComplete, Callback onError, Callback onPermissionPermissionRestriction) {

    }

    @Override
    public void getFromGalleryCroppedAndScaled(int w, int h, CallbackSource<Bitmap> onComplete, Callback onError, Callback onPermissionPermissionRestriction) {

    }

    @Override
    public Bitmap getFromDrawable(Drawable drawable) {
        return null;
    }

    @Override
    public Bitmap getFromURL(String src) throws IOException {
        return null;
    }

    @Override
    public void getFromURL(String src, CallbackSource<Bitmap> bitmapListener) {

    }

    @Override
    public void getFromUri(Activity activity, Uri uri, CallbackSource<Bitmap> callbackResult, Callback onPermissionPermissionRestriction) {

    }

    @Override
    public byte[] toBytes(Bitmap bitmap, int maxBytesSize) {
        return new byte[0];
    }

    @Override
    public byte[] toJPGBytes(Bitmap bitmap, int q) {
        return new byte[0];
    }

    @Override
    public byte[] toPNGBytes(Bitmap bitmap) {
        return new byte[0];
    }

    @Override
    public byte[] drawableToBytes(int resId) {
        return new byte[0];
    }

    @Override
    public Bitmap keepMaxSides(Bitmap bitmap, int maxSideSize) {
        return null;
    }
}
