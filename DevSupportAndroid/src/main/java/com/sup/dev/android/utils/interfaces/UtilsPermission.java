package com.sup.dev.android.utils.interfaces;

import android.app.Activity;

import com.sup.dev.java.classes.callbacks.simple.Callback;

public interface UtilsPermission {

    //
    //  Requests
    //

    void requestReadPermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) ;

    void requestWritePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction);

    void requestCallPhonePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction);

    void requestOverlayPermission(Activity activity, Callback onGranted, Callback onPermissionRestriction);

    void requestMicrophonePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction);

    //
    //  Checks
    //

    boolean hasReadPermission() ;

    boolean hasWritePermission();

    boolean hasCallPhonePermission();

    boolean hasOverlayPermission();

    boolean hasMicrophonePermission();

    //
    //  Methods
    //

    boolean hasPermission(String permission);

    void requestPermission(Activity activity, String permission, Callback onGranted, Callback onPermissionRestriction);



}
