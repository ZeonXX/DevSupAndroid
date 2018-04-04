package com.sup.dev.android.utils.implementations;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.MainThread;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsStorage;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.libs.json.Json;
import com.sup.dev.java.libs.json.JsonArray;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class UtilsStorageImpl implements UtilsStorage {

    private final String externalFileNamePrefix;
    protected final SharedPreferences preferences;

    public UtilsStorageImpl(String externalFileNamePrefix, String storageKey) {
        this.externalFileNamePrefix = externalFileNamePrefix;
        preferences = SupAndroid.di.appContext().getSharedPreferences(storageKey, Activity.MODE_PRIVATE);
    }

    public boolean contains(String key) {
        return preferences.contains(key);
    }

    //
    //  Get
    //

    public boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    public int getInt(String key, int def) {
        return preferences.getInt(key, def);
    }

    public long getLong(String key, long def) {
        return preferences.getLong(key, def);
    }

    public float getFloat(String key, float def) {
        return preferences.getFloat(key, def);
    }

    public String getString(String key, String string) {
        return preferences.getString(key, string);
    }

    public byte[] getBytes(String key) {
        String s = preferences.getString(key, null);
        if (s == null) return null;
        return s.getBytes();
    }

    public Json getJson(String key) {
        String s = getString(key, null);
        if (s == null) return null;
        return new Json(s);
    }

    //
    //  Put
    //

    public void put(String key, boolean v) {
        preferences.edit().putBoolean(key, v).apply();
    }

    public void put(String key, int v) {
        preferences.edit().putInt(key, v).apply();
    }

    public void put(String key, long v) {
        preferences.edit().putLong(key, v).apply();
    }

    public void put(String key, float v) {
        preferences.edit().putFloat(key, v).apply();
    }

    public void put(String key, String v) {
        preferences.edit().putString(key, v).apply();
    }

    public void put(String key, byte[] value) {
        preferences.edit().putString(key, new String(value)).apply();
    }

    public void put(String key, Json v) {
        put(key, v.toString());
    }

    //
    //  Remove
    //

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    //
    //  String array
    //

    @MainThread
    public String[] getStringArray(String key, String[] def) {
        String string = preferences.getString(key, null);
        if (string == null || string.isEmpty())
            return def;

        return new JsonArray(string).getStrings();
    }

    @MainThread
    public void put(String key, String[] v) {
        JsonArray json = new JsonArray();
        json.put(v);
        preferences.edit().putString(key, json.toString()).apply();
    }


    @MainThread
    public void addToStringArray(String key, String v) {

        String[] stringArray = getStringArray(key, new String[0]);
        String[] copy = new String[stringArray.length + 1];
        System.arraycopy(stringArray, 0, copy, 0, stringArray.length);
        copy[copy.length - 1] = v;

        put(key, copy);
    }

    @MainThread
    public void removeFromStringArray(String key, String v) {

        String[] stringArray = getStringArray(key, new String[0]);
        ArrayList<String> copy = new ArrayList<>();

        for (String s : stringArray)
            if (!s.equals(v))
                copy.add(s);

        put(key, copy.toArray(new String[0]));

    }

    @MainThread
    public void removeFromStringArray(String key, int index) {

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

    public void saveImage(Activity activity, Bitmap bitmap, int messageRes, Callback onPermissionPermissionRestriction) {
        SupAndroid.di.utilsPermission().requestWritePermission(activity, () -> {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
            final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + externalFileNamePrefix + "_" + System.currentTimeMillis() + ".png");
            try {
                FileOutputStream out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                SupAndroid.di.utilsThreads().main(() -> SupAndroid.di.utilsToast().show(SupAndroid.di.utilsResources().getString(messageRes) + f.getAbsolutePath()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, onPermissionPermissionRestriction);
    }

    public void saveFile(Activity activity, byte[] bytes, String ex, int messageRes, Callback onPermissionPermissionRestriction) {
        SupAndroid.di.utilsPermission().requestWritePermission(activity, () -> {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
            final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + externalFileNamePrefix + "_" + System.currentTimeMillis() + "." + ex);
            try {
                FileOutputStream out = new FileOutputStream(f);
                out.write(bytes);
                out.close();
                SupAndroid.di.utilsThreads().main(() -> SupAndroid.di.utilsToast().show(SupAndroid.di.utilsResources().getString(messageRes) + f.getAbsolutePath()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, onPermissionPermissionRestriction);
    }


}