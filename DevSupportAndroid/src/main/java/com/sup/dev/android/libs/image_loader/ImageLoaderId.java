package com.sup.dev.android.libs.image_loader;

import android.widget.ImageView;

import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.providers.Provider1;

public class ImageLoaderId extends ImageLoaderA {

    public static Provider1<Long, byte[]> loader;

    private final long imageId;

    public ImageLoaderId(long imageId) {
        this.imageId = imageId;
        setKey(asKey(imageId));
    }

    byte[] load() {
        return loader.provide(imageId);
    }

    public static void load(long imageId) {
        load(imageId, null, null);
    }

    public static void load(long imageId, Callback1<byte[]> onLoaded) {
        load(imageId, null, onLoaded);
    }

    public static void load(long imageId, ImageView vImage) {
        load(imageId, vImage, null);
    }

    public static void load(long imageId, ImageView vImage, Callback1<byte[]> onLoaded) {
        ImageLoader.load(new ImageLoaderId(imageId).setImage(vImage).onLoaded(onLoaded));
    }

    public static void clearCash(long imageId) {
        ImageLoader.bitmapCash.remove(asKey(imageId));
    }

    private static String asKey(long imageId) {
        return "imgId_" + imageId;
    }

    public static void replace(long imageId, byte[] bytes) {
        ImageLoader.bitmapCash.replace(asKey(imageId), bytes);
    }


}