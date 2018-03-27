package tests._sup_android.stubs.utils;

import android.app.Activity;
import android.graphics.Bitmap;

import com.sup.dev.android.utils.interfaces.UtilsFiles;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

import java.io.File;

public class UtilsFilesStub implements UtilsFiles {

    @Override
    public void saveImageInCameraFolder(Activity activity, Bitmap bitmap, CallbackSource<String> onResult, Callback onPermissionPermissionRestriction) {

    }

    @Override
    public File getDiskCacheDir() {
        return null;
    }
}
