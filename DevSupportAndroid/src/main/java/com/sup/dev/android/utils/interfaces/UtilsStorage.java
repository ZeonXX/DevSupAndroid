package com.sup.dev.android.utils.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.MainThread;

import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.libs.json.Json;

import java.io.File;

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

    Json getJson(String key);

    //
    //  Put
    //

    void put(String key, boolean v);

    void put(String key, int v);

    void put(String key, long v);

    void put(String key, float v);

    void put(String key, String v);

    void put(String key, byte[] v);

    void put(String key, Json v);

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

    void saveImageInDownloadFolder(Activity activity, Bitmap bitmap, CallbackSource<File> onComplete, Callback onPermissionPermissionRestriction);

    void saveFileInDownloadFolder(Activity activity, byte[] bytes, String ex, CallbackSource<File> onComplete, Callback onPermissionPermissionRestriction);

}
