package com.sup.dev.android.utils.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.MainThread;

import com.sup.dev.java.classes.callbacks.simple.Callback;

public interface UtilsStorage {

    boolean contains(String key);

    //
    //  Get
    //

    boolean getBoolean(String key, boolean def);

    int getInt(String key, int def);

    long getLong(String key, long def);

    float getFloat(String key, float def);

    String getString(String key, String string);

    byte[] getBytes(String key);

    //
    //  Put
    //

    void put(String key, boolean v);

    void put(String key, int v);

    void put(String key, long v);

    void put(String key, float v);

    void put(String key, String v);

    void put(String key, byte[] v);

    //
    //  Remove
    //

    void remove(String key);

    //
    //  String array
    //

    @MainThread
    String[] getStringArray(String key, String[] def);

    @MainThread
    void put(String key, String[] v);

    @MainThread
    void addToStringArray(String key, String v);

    @MainThread
    void removeFromStringArray(String key, String v);

    @MainThread
    void removeFromStringArray(String key, int index);

    //
    //  Files
    //

    void saveImage(Activity activity, Bitmap bitmap, int messageRes, Callback onPermissionPermissionRestriction);

    void saveFile(Activity activity, byte[] bytes, String ex, int messageRes, Callback onPermissionPermissionRestriction);

}
