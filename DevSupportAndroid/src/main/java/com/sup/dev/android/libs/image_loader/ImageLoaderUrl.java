package com.sup.dev.android.libs.image_loader;

import android.widget.ImageView;

import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsNetwork;

import java.io.IOException;

public class ImageLoaderUrl extends ImageLoaderA {

    private final String url;

    public ImageLoaderUrl(String url) {
        this.url = url;
        setKey(asKey(url));
    }

    byte[] load() {
        try {
            return ToolsNetwork.INSTANCE.getBytesFromURL(url);
        } catch (IOException e) {
            Debug.log(e);
            return null;
        }
    }

    public static void load(String url) {
        load(url, null, null);
    }

    public static void load(String url, Callback1<byte[]> onLoaded) {
        load(url, null, onLoaded);
    }

    public static void load(String url, ImageView vImage) {
        load(url, vImage, null);
    }

    public static void load(String url, ImageView vImage, Callback1<byte[]> onLoaded) {
        ImageLoader.load(new ImageLoaderUrl(url).setImage(vImage).onLoaded(onLoaded));
    }

    public static void clearCash(String url) {
        ImageLoader.bitmapCash.remove(asKey(url));
    }

    public static void replace(String url, byte[] bytes) {
        ImageLoader.bitmapCash.replace(asKey(url), bytes);
    }

    private static String asKey(String url) {
        return "url_" + url;
    }


}