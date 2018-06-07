package com.sup.dev.android.libs.image_loader;

public class ImageLoaderBytes extends ImageLoaderA {

    private final byte[] bytes;

    public ImageLoaderBytes(Object key, byte[] bytes) {
        this.bytes = bytes;
        setKey("bytes_" + key);
    }

    byte[] load() {
        return bytes;
    }
}