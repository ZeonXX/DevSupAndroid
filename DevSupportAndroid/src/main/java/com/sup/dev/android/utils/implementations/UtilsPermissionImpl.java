package com.sup.dev.android.utils.implementations;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsPermission;
import com.sup.dev.java.classes.callbacks.simple.Callback;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class UtilsPermissionImpl implements UtilsPermission {

    private static final long MAX_WAIT_TIME = 1000 * 10;
    private static final int REQUEST_CODE = 102;

    //
    //  Requests
    //

    public void requestReadPermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            requestPermission(activity, READ_EXTERNAL_STORAGE, onGranted, onPermissionRestriction);
    }

    public void requestWritePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {
        requestPermission(activity, WRITE_EXTERNAL_STORAGE, onGranted, onPermissionRestriction);
    }

    public void requestCallPhonePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {
        requestPermission(activity, CALL_PHONE, onGranted, onPermissionRestriction);
    }

    public void requestOverlayPermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {
        requestPermission(activity, SYSTEM_ALERT_WINDOW, onGranted, onPermissionRestriction);
    }

    public void requestMicrophonePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {
        requestPermission(activity, RECORD_AUDIO, onGranted, onPermissionRestriction);
    }

    //
    //  Checks
    //

    public boolean hasReadPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN || hasPermission(READ_EXTERNAL_STORAGE);
    }

    public boolean hasWritePermission() {
        return hasPermission(WRITE_EXTERNAL_STORAGE);
    }

    public boolean hasCallPhonePermission() {
        return hasPermission(CALL_PHONE);
    }

    public boolean hasOverlayPermission() {
        return hasPermission(SYSTEM_ALERT_WINDOW);
    }

    public boolean hasMicrophonePermission() {
        return hasPermission(RECORD_AUDIO);
    }


    //
    //  Methods
    //

    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(SupAndroid.di.appContext(), permission) == PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity, String permission, Callback onGranted, Callback onPermissionRestriction) {
        if(hasPermission(permission)) {
            if(onGranted != null)onGranted.callback();
            return;
        }
        SupAndroid.di.utilsThreads().thread(() -> {
            if (requestPermission(activity, permission)) {
                if (onGranted != null)
                    SupAndroid.di.utilsThreads().main(2000, onGranted::callback);    //  Без задержки приложение ведет себя так, будто разрешение еще не получено
            } else {
                if (onPermissionRestriction != null)
                    SupAndroid.di.utilsThreads().main(onPermissionRestriction::callback);
            }
        });
    }

    private boolean requestPermission(Activity activity, String permission) {

        if (hasPermission(permission)) return true;

        ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
        return waitForPermission(permission);
    }

    private boolean waitForPermission(String permission) {

        long t = System.currentTimeMillis() + MAX_WAIT_TIME;

        while (t > System.currentTimeMillis() && !hasPermission(permission))
            SupAndroid.di.utilsThreads().sleep(50);

        return hasPermission(permission);
    }


}
