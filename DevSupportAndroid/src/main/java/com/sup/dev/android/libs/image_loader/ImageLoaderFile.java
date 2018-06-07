package com.sup.dev.android.libs.image_loader;

import android.widget.ImageView;

import com.sup.dev.android.tools.ToolsFiles;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;

import java.io.File;
import java.io.IOException;

public class ImageLoaderFile extends ImageLoaderA {

    private final File file;

    public ImageLoaderFile(File file) {
        this.file = file;
        setKey(asKey(file));
    }

    byte[] load() {
        try {
            byte[] bytes = ToolsFiles.readFile(file);
            return bytes;
        } catch (IOException e) {
            Debug.log(e);
            return null;
        }
    }

    public static void load(File file) {
        load(file, null, null);
    }

    public static void load(File file, Callback1<byte[]> onLoaded) {
        load(file, null, onLoaded);
    }

    public static void load(File file, ImageView vImage) {
        load(file, vImage, null);
    }

    public static void load(File file, ImageView vImage, Callback1<byte[]> onLoaded) {
        ImageLoader.load(new ImageLoaderFile(file).setImage(vImage).onLoaded(onLoaded));
    }

    public static void clearCash(File file) {
        ImageLoader.bitmapCash.remove(asKey(file));
    }

    private static String asKey(File file) {
        return "file_" +  file.getAbsolutePath();
    }

    public static void replace(File file, byte[] bytes) {
        ImageLoader.bitmapCash.replace(asKey(file), bytes);
    }
}