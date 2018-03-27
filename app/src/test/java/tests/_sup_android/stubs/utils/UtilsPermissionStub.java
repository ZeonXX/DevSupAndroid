package tests._sup_android.stubs.utils;

import android.app.Activity;

import com.sup.dev.android.utils.interfaces.UtilsPermission;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class UtilsPermissionStub implements UtilsPermission {
    @Override
    public void requestReadPermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {

    }

    @Override
    public void requestWritePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {

    }

    @Override
    public void requestCallPhonePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {

    }

    @Override
    public void requestOverlayPermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {

    }

    @Override
    public void requestMicrophonePermission(Activity activity, Callback onGranted, Callback onPermissionRestriction) {

    }

    @Override
    public boolean hasReadPermission() {
        return false;
    }

    @Override
    public boolean hasWritePermission() {
        return false;
    }

    @Override
    public boolean hasCallPhonePermission() {
        return false;
    }

    @Override
    public boolean hasOverlayPermission() {
        return false;
    }

    @Override
    public boolean hasMicrophonePermission() {
        return false;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void requestPermission(Activity activity, String permission, Callback onGranted, Callback onPermissionRestriction) {

    }
}
