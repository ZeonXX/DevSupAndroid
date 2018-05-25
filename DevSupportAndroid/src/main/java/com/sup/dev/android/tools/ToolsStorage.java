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
        if(preferences == null) init();
        return preferences.getBoolean(key, def);
    }

    public static int getInt(String key, int def) {
        if(preferences == null) init();
        return preferences.getInt(key, def);
    }

    public static long getLong(String key, long def) {
        if(preferences == null) init();
        return preferences.getLong(key, def);
    }

    public static float getFloat(String key, float def) {
        if(preferences == null) init();
        return preferences.getFloat(key, def);
    }

    public static String getString(String key, String string) {
        if(preferences == null) init();
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

    //
    //  Put
    //

    public static void put(String key, boolean v) {
        if(preferences == null) init();
        preferences.edit().putBoolean(key, v).apply();
    }

    public static void put(String key, int v) {
        if(preferences == null) init();
        preferences.edit().putInt(key, v).apply();
    }

    public static void put(String key, long v) {
        if(preferences == null) init();
        preferences.edit().putLong(key, v).apply();
    }

    public static void put(String key, float v) {
        if(preferences == null) init();
        preferences.edit().putFloat(key, v).apply();
    }

    public static void put(String key, String v) {
        if(preferences == null) init();
        preferences.edit().putString(key, v).apply();
    }

    public static void put(String key, byte[] value) {
        if(preferences == null) init();
        preferences.edit().putString(key, new String(value)).apply();
    }

    public static void put(String key, Json v) {
        put(key, v.toString());
    }

    //
    //  Remove
    //

    public static void remove(String key) {
        if(preferences == null) init();
        preferences.edit().remove(key).apply();
    }

    //
    //  String array
    //

    @MainThread
    public static String[] getStringArray(String key, String[] def) {
        String string = preferences.getString(key, null);
        if (string == null || string.isEmpty())
            return def;

        return new JsonArray(string).getStrings();
    }

    @MainThread
    public static void put(String key, String[] v) {
        if(preferences == null) init();
        JsonArray json = new JsonArray();
        json.put(v);
        preferences.edit().putString(key, json.toString()).apply();
    }


    @MainThread
    public static void addToStringArray(String key, String v) {

        String[] stringArray = getStringArray(key, new String[0]);
        String[] copy = new String[stringArray.length + 1];
        System.arraycopy(stringArray, 0, copy, 0, stringArray.length);
        copy[copy.length - 1] = v;

        put(key, copy);
    }

    @MainThread
    public static void removeFromStringArray(String key, String v) {

        String[] stringArray = getStringArray(key, new String[0]);
        ArrayList<String> copy = new ArrayList<>();

        for (String s : stringArray)
            if (!s.equals(v))
                copy.add(s);

        put(key, copy.toArray(new String[0]));

    }

    @MainThread
    public static void removeFromStringArray(String key, int index) {

        String[] stringArray = getStringArray(key, new String[0]);
        ArrayList<String> copy = new ArrayList<>();

        for (int i = 0; i < stringArray.length; i++)
            if (i != index)
                copy.add(stringArray[i]);

        put(key, copy.toArray(new String[0]));

    }

    //
    //  Files
    //

    public static void saveImageInDownloadFolder(Activity activity, Bitmap bitmap, Callback1<File> onComplete, Callback onPermissionPermissionRestriction) {
        if(preferences == null) init();
        ToolsPermission.requestWritePermission(activity, () -> {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
            final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + externalFileNamePrefix + "_" + System.currentTimeMillis() + ".png");
            try {
                FileOutputStream out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                ToolsThreads.main(() -> onComplete.callback(f));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, onPermissionPermissionRestriction);
    }

    public static void saveFileInDownloadFolder(Activity activity, byte[] bytes, String ex, Callback1<File> onComplete, Callback onPermissionPermissionRestriction) {
        if(preferences == null) init();
        saveFile(activity, new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + externalFileNamePrefix + "_" + System.currentTimeMillis() + "." + ex).getAbsolutePath(),
                bytes, onComplete, onPermissionPermissionRestriction);
    }

    public static void saveFile(Activity activity, String patch, byte[] bytes, Callback1<File> onComplete, Callback onPermissionPermissionRestriction) {
        if(preferences == null) init();
        ToolsPermission.requestWritePermission(activity, () -> {
            final File f = new File(patch);
            f.delete();
            if(f.getParentFile() != null)f.getParentFile().mkdirs();
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