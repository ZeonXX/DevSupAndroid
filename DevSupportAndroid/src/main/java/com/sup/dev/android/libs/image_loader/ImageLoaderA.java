package com.sup.dev.android.libs.image_loader;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.sup.dev.java.classes.callbacks.simple.Callback1;

public abstract class ImageLoaderA {

    private String key;

    ImageView vImage;
    Callback1<byte[]> onLoaded;
    boolean cropSquareCenter;
    BitmapFactory.Options options;
    int w;
    int h;
    int holder;
    boolean fade = true;
    boolean cashScaledBytes;
    boolean noCash;
    boolean noLoadFromCash;
    String keyPrefix;

    public ImageLoaderA setImage(ImageView vImage) {
        this.vImage = vImage;
        if (vImage != null) vImage.setTag(key);
        return this;
    }

    public ImageLoaderA keyPrefix(String keyPrefix) {
        if(this.keyPrefix != null) key = key.substring(keyPrefix.length());
        this.keyPrefix = keyPrefix;
        setKey(key);
        return this;
    }

    public ImageLoaderA sizes(int w, int h) {
        this.w = w;
        this.h = h;
        return this;
    }

    protected ImageLoaderA setKey(Object key) {
        this.key = keyPrefix + key.toString();
        return this;
    }

    public ImageLoaderA onLoaded(Callback1<byte[]> onLoaded) {
        this.onLoaded = onLoaded;
        return this;
    }

    public ImageLoaderA cropSquare() {
        this.cropSquareCenter = true;
        return this;
    }

    public ImageLoaderA options(BitmapFactory.Options options) {
        this.options = options;
        return this;
    }

    public ImageLoaderA cashScaledBytes() {
        this.cashScaledBytes = true;
        return this;
    }

    public ImageLoaderA holder(int holder) {
        this.holder = holder;
        return this;
    }

    public ImageLoaderA noFade() {
        this.fade = false;
        return this;
    }

    public ImageLoaderA noCash() {
        this.noCash = false;
        return this;
    }

    public ImageLoaderA noLoadFromCash() {
        this.noLoadFromCash = true;
        return this;
    }

    boolean isKey(Object key) {
        return key == this.key || (key != null && key.equals(this.key));
    }

    abstract byte[] load();

    public Object getKey() {
        return key;
    }

    public void clearCash() {
        ImageLoader.bitmapCash.remove(key);
    }

    public void replace(byte[] bytes) {
        ImageLoader.bitmapCash.replace(key, bytes);
    }


}
