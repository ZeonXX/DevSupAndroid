package tests._sup_android.stubs.utils;

import android.app.Activity;
import android.graphics.Bitmap;

import com.sup.dev.android.utils.interfaces.UtilsStorage;
import com.sup.dev.java.classes.callbacks.simple.Callback;

import java.util.HashMap;

public class UtilsStorageStub implements UtilsStorage {

    public HashMap<String, Object> values = new HashMap<>();

    @Override
    public boolean contains(String key) {
        return values.containsKey(key);
    }

    @Override
    public void remove(String key) {
        values.remove(key);
    }


    //
    //  Get
    //

    @Override
    public boolean getBoolean(String key, boolean def) {
        return (boolean) values.get(key);
    }

    @Override
    public int getInt(String key, int def) {
        return (int) values.get(key);
    }

    @Override
    public long getLong(String key, long def) {
        return (long) values.get(key);
    }

    @Override
    public float getFloat(String key, float def) {
        return (float) values.get(key);
    }

    @Override
    public String getString(String key, String string) {
        return (String) values.get(key);
    }

    @Override
    public byte[] getBytes(String key) {
        return (byte[]) values.get(key);
    }

    //
    //  Put
    //

    @Override
    public void put(String key, boolean v) {
        values.put(key, v);
    }

    @Override
    public void put(String key, int v) {
        values.put(key, v);
    }

    @Override
    public void put(String key, long v) {
        values.put(key, v);
    }

    @Override
    public void put(String key, float v) {
        values.put(key, v);
    }

    @Override
    public void put(String key, String v) {
        values.put(key, v);
    }

    @Override
    public void put(String key, byte[] v) {
        values.put(key, v);
    }

    @Override
    public String[] getStringArray(String key, String[] def) {
        return new String[0];
    }

    @Override
    public void put(String key, String[] v) {
        values.put(key, v);
    }

    @Override
    public void addToStringArray(String key, String v) {

    }

    @Override
    public void removeFromStringArray(String key, String v) {

    }

    @Override
    public void removeFromStringArray(String key, int index) {

    }

    @Override
    public void saveImage(Activity activity, Bitmap bitmap, int messageRes, Callback onPermissionPermissionRestriction) {

    }

    @Override
    public void saveFile(Activity activity, byte[] bytes, String ex, int messageRes, Callback onPermissionPermissionRestriction) {

    }
}
