package com.sup.dev.android.tools;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.MainThread;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.json.Json;
import com.sup.dev.java.libs.json.JsonArray;
import com.sup.dev.java.tools.ToolsThreads;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ToolsStorage {

    public static String externalFileNamePrefix = "f";
    public static SharedPreferences preferences;

    public static void init() {
        init("android_app_pref");
    }

    public static void init(String storageKey) {
        preferences = SupAndroid.appContext.getSharedPreferences(storageKey, Activity.MODE_PRIVATE);
    }

    public static boolean contains(String key) {
        return preferences.contains(key);
    }

    //
    //  Get
    //

    public static boolean getBoolean(String key, boolean def) {
        if (preferences == null) init();
        return preferences.getBoolean(key, def);
    }

    public static int getInt(String key, int def) {
        if (preferences == null) init();
        return preferences.getInt(key, def);
    }

    public static long getLong(String key, long def) {
        if (preferences == null) init();
        return preferences.getLong(key, def);
    }

    public static float getFloat(String key, float def) {
        if (preferences == null) init();
        return preferences.getFloat(key, def);
    }

    public static String getString(String key, String string) {
        if (preferences == null) init();
        return preferences.getString(key, string);
    }

    public static byte[] getBytes(String key) {
        String s = preferences.getString(key, null);
        if (s == null) return null;
        return s.getBytes();
    }

    public static Json getJson(String key) {
        String s = getString(key, null);
        if (s == null) return null;
        return new Json(s);
    }

    public static JsonArray getJsonArray(String key, JsonArray def) {
        String string = getString(key, null);
        if (string == null || string.isEmpty())
            return def;

        return new JsonArray(string);
    }

    //
    //  Put
    //

    public static void put(String key, boolean v) {
        if (preferences == null) init();
        preferences.edit().putBoolean(key, v).apply();
    }

    public static void put(String key, int v) {
        if (preferences == null) init();
        preferences.edit().putInt(key, v).apply();
    }

    public static void put(String key, long v) {
        if (preferences == null) init();
        preferences.edit().putLong(key, v).apply();
    }

    public static void put(String key, float v) {
        if (preferences == null) init();
        preferences.edit().putFloat(key, v).apply();
    }

    public static void put(String key, String v) {
        if (preferences == null) init();
        preferences.edit().putString(key, v).apply();
    }

    public static void put(String key, byte[] value) {
        if (preferences == null) init();
        preferences.edit().putString(key, new String(value)).apply();
    }

    public static void put(String key, Json v) {
        put(key, v.toString());
    }

    public static void put(String key, JsonArray v){
        if (preferences == null) init();
        preferences.edit().putString(key, v.toString()).apply();
    }

    //
    //  Remove
    //

    public static void remove(String key) {
        if (preferences == null) init();
        preferences.edit().remove(key).apply();
    }


    //
    //  Files
    //

    public static void saveImageInDownloadFolder(Bitmap bitmap, Callback1<File> onComplete) {
        if (preferences == null) init();
        ToolsPermission.requestWritePermission(() -> {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
            final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + externalFileNamePrefix + "_" + System.currentTimeMillis() + ".png");
            try {
                FileOutputStream out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                ToolsThreads.main(() -> {
                    if (onComplete != null) onComplete.callback(f);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void saveFileInDownloadFolder(byte[] bytes, String ex, Callback1<File> onComplete, Callback onPermissionPermissionRestriction) {
        if (preferences == null) init();
        saveFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + externalFileNamePrefix + "_" + System.currentTimeMillis() + "." + ex).getAbsolutePath(),
                bytes, onComplete, onPermissionPermissionRestriction);
    }

    public static void saveFile(String patch, byte[] bytes, Callback1<File> onComplete, Callback onPermissionPermissionRestriction) {
        if (preferences == null) init();
        ToolsPermission.requestWritePermission(() -> {
            final File f = new File(patch);
            f.delete();
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
            try {
                FileOutputStream out = new FileOutputStream(f);
                out.write(bytes);
                out.close();
                ToolsThreads.main(() -> onComplete.callback(f));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, onPermissionPermissionRestriction);
    }
}