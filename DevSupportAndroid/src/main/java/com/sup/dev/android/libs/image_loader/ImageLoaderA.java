package com.sup.dev.android.libs.image_loader;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.sup.dev.java.classes.callbacks.simple.Callback1;

public abstract class ImageLoaderA {

    ImageView vImage;
    Object key;
    Callback1<byte[]> onLoaded;
    boolean cropSquareCenter;
    BitmapFactory.Options options;
    int w;
    int h;
    int holder;
    boolean fade = true;
    boolean cashScaledBytes;

    public ImageLoaderA setImage(ImageView vImage) {
        this.vImage = vImage;
        if (vImage != null) vImage.setTag(key);
        return this;
    }

    public ImageLoaderA setSizes(int w, int h) {
        this.w = w;
        this.h = h;
        return this;
    }

    protected ImageLoaderA setKey(Object key) {
        this.key = key;
        return this;
    }

    public ImageLoaderA setOnLoaded(Callback1<byte[]> onLoaded) {
        this.onLoaded = onLoaded;
        return this;
    }

    public ImageLoaderA setCropSquareCenter(boolean cropSquareCenter) {
        this.cropSquareCenter = cropSquareCenter;
        return this;
    }

    public ImageLoaderA setOptions(BitmapFactory.Options options) {
        this.options = options;
        return this;
    }

    public ImageLoaderA setCashScaledBytes(boolean cashScaledBytes) {
        this.cashScaledBytes = cashScaledBytes;
        return this;
    }

    public ImageLoaderA setHolder(int holder) {
        this.holder = holder;
        return this;
    }

    public ImageLoaderA setFade(boolean fade) {
        this.fade = fade;
        return this;
    }

    boolean isKey(Object key) {
        return key == this.key || (key != null && key.equals(this.key));
    }

    abstract byte[] load();
}
