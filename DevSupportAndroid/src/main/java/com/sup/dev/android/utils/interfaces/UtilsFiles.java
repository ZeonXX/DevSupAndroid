package com.sup.dev.android.utils.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;

import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

import java.io.File;
import java.io.IOException;

public interface UtilsFiles {

    //
    //  Files
    //

    File writeFile(String patch, byte[] bytes) throws IOException;

    void unpackZip(String path, File zipFile) throws IOException;

    byte[] readAsZip(String filePatch) throws IOException;


    //
    //  Bitmap
    //

    void saveImageInCameraFolder(Activity activity, Bitmap bitmap, Callback1<String> onResult, Callback onPermissionPermissionRestriction);


    //
    //  Getters
    //

    File getDiskCacheDir();

}
