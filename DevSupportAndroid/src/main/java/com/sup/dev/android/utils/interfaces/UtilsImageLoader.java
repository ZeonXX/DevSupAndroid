package com.sup.dev.android.utils.interfaces;

import android.widget.ImageView;

import com.sup.dev.java.classes.callbacks.simple.Callback1;

public interface UtilsImageLoader {


    void unsubscribe(ImageView imageView);

    void clearCash(long imageId);

    void clearCash(String url);

    void replace(long imageId, byte[] bytes);

    void load(long imageId);

    void load(long imageId, Callback1<byte[]> onLoaded);

    void load(long imageId, ImageView imageView);

    void load(long imageId, ImageView imageView, Callback1<byte[]> onLoaded);

    void load(String url);

    void load(String url, Callback1<byte[]> onLoaded);

    void load(String url, ImageView imageView);

    void load(String url, ImageView imageView, Callback1<byte[]> onLoaded);

}
