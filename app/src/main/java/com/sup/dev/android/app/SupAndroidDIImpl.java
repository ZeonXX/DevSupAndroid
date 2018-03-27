package com.sup.dev.android.app;


import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import com.sup.dev.android.libs.mvp.activity.MvpActivity;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigatorImpl;
import com.sup.dev.android.utils.implementations.UtilsAndroidImpl;
import com.sup.dev.android.utils.implementations.UtilsBitmapImpl;
import com.sup.dev.android.utils.implementations.UtilsCashImpl;
import com.sup.dev.android.utils.implementations.UtilsCursorImpl;
import com.sup.dev.android.utils.implementations.UtilsFilesImpl;
import com.sup.dev.android.utils.implementations.UtilsIntentImpl;
import com.sup.dev.android.utils.implementations.UtilsMediaPlayerImpl;
import com.sup.dev.android.utils.implementations.UtilsMetadataImpl;
import com.sup.dev.android.utils.implementations.UtilsNotificationsImpl;
import com.sup.dev.android.utils.implementations.UtilsPermissionImpl;
import com.sup.dev.android.utils.implementations.UtilsResourcesImpl;
import com.sup.dev.android.utils.implementations.UtilsStorageImpl;
import com.sup.dev.android.utils.implementations.UtilsTextImpl;
import com.sup.dev.android.utils.implementations.UtilsToastImpl;
import com.sup.dev.android.utils.implementations.UtilsViewImpl;
import com.sup.dev.android.utils.interfaces.UtilsAndroid;
import com.sup.dev.android.utils.interfaces.UtilsBitmap;
import com.sup.dev.android.utils.interfaces.UtilsCash;
import com.sup.dev.android.utils.interfaces.UtilsCursor;
import com.sup.dev.android.utils.interfaces.UtilsFiles;
import com.sup.dev.android.utils.interfaces.UtilsIntent;
import com.sup.dev.android.utils.interfaces.UtilsMediaPlayer;
import com.sup.dev.android.utils.interfaces.UtilsMetadata;
import com.sup.dev.android.utils.interfaces.UtilsNotifications;
import com.sup.dev.android.utils.interfaces.UtilsPermission;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsStorage;
import com.sup.dev.android.utils.interfaces.UtilsText;
import com.sup.dev.android.utils.interfaces.UtilsToast;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.app.SupJavaDIImpl;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.utils.implementations.UtilsThreadsImpl;
import com.sup.dev.java.utils.interfaces.UtilsThreads;

import java.util.ArrayList;

public class SupAndroidDIImpl extends SupJavaDIImpl implements SupAndroidDI {

    private final ArrayList<CallbackSource<MvpActivity>> mvpActivityCallbacks = new ArrayList<>();
    private final Context appContext;
    private final String appName;

    private MvpActivity mvpActivity;

    private MvpNavigator mvpNavigator;
    private UtilsThreads utilsThreads;
    private UtilsAndroid utilsAndroid;
    private UtilsBitmap utilsBitmap;
    private UtilsFiles utilsFiles;
    private UtilsIntent utilsIntent;
    private UtilsNotifications utilsNotifications;
    private UtilsPermission utilsPermission;
    private UtilsResources utilsResources;
    private UtilsStorage utilsStorage;
    private UtilsText utilsText;
    private UtilsToast utilsToast;
    private UtilsView utilsView;
    private UtilsCash utilsCash;
    private UtilsMetadata utilsMetadata;

    public SupAndroidDIImpl(Context appContext, String appName) {
        this.appContext = appContext;
        this.appName = appName;
    }

    public void setMvpActivity(MvpActivity mvpActivity) {
        this.mvpActivity = mvpActivity;
        while (mvpActivity != null && !mvpActivityCallbacks.isEmpty())
            mvpActivityCallbacks.remove(0).callback(mvpActivity);
    }

    //
    //
    //  DI
    //
    //

    public Context appContext() {
        return appContext;
    }

    public String appName() {
        return null;
    }

    public void mvpActivity(CallbackSource<MvpActivity> onActivity) {
        if (mvpActivity == null)
            mvpActivityCallbacks.add(onActivity);
        else
            SupAndroid.di.utilsThreads().main(() -> onActivity.callback(mvpActivity));
    }

    //
    //  Mvp
    //

    public MvpNavigator navigator() {
        if(mvpNavigator == null) mvpNavigator = new MvpNavigatorImpl();
        return mvpNavigator;
    }

    //
    //  Utils
    //

    public UtilsThreads utilsThreads() {
        if(utilsThreads == null) utilsThreads = new UtilsThreadsImpl((onNextTime, runnable) -> {
            if (!onNextTime && utilsAndroid().isMainThread())
                runnable.run();
            else
                new Handler(Looper.getMainLooper()).post(runnable);
        });
        return utilsThreads;
    }

    public UtilsAndroid utilsAndroid() {
        if(utilsAndroid == null) utilsAndroid = new UtilsAndroidImpl();
        return utilsAndroid;
    }

    public UtilsBitmap utilsBitmap() {
        if(utilsBitmap == null) utilsBitmap = new UtilsBitmapImpl();
        return utilsBitmap;
    }

    public UtilsCursor utilsCursor(Cursor cursor) {
        return new UtilsCursorImpl(cursor);
    }

    public UtilsFiles utilsFiles() {
        if(utilsFiles == null) utilsFiles = new UtilsFilesImpl();
        return utilsFiles;
    }

    public UtilsIntent utilsIntent() {
        if(utilsIntent == null) utilsIntent = new UtilsIntentImpl();
        return utilsIntent;
    }

    public UtilsMediaPlayer utilsMediaPlayer() {
        return new UtilsMediaPlayerImpl();
    }

    public UtilsNotifications utilsNotifications() {
        if(utilsNotifications == null) utilsNotifications = new UtilsNotificationsImpl(appName);
        return utilsNotifications;
    }

    public UtilsPermission utilsPermission() {
        if(utilsPermission == null) utilsPermission = new UtilsPermissionImpl();
        return utilsPermission;
    }

    public UtilsResources utilsResources() {
        if(utilsResources == null) utilsResources = new UtilsResourcesImpl();
        return utilsResources;
    }

    public UtilsStorage utilsStorage() {
        if(utilsStorage == null) utilsStorage = new UtilsStorageImpl(appName, "Support_App_Preferences");
        return utilsStorage;
    }

    public UtilsText utilsText() {
        if(utilsText == null) utilsText = new UtilsTextImpl();
        return utilsText;
    }

    public UtilsToast utilsToast() {
        if(utilsToast == null) utilsToast = new UtilsToastImpl();
        return utilsToast;
    }

    public UtilsView utilsView() {
        if(utilsView == null) utilsView = new UtilsViewImpl();
        return utilsView;
    }

    public UtilsCash utilsCash() {
        if(utilsCash == null) utilsCash = new UtilsCashImpl(1024 * 1024 * 50);
        return utilsCash;
    }

    public UtilsMetadata utilsMetadata() {
        if(utilsMetadata == null) utilsMetadata = new UtilsMetadataImpl();
        return utilsMetadata;
    }

    public UtilsMetadata utilsMetadata(String patch) {
        return new UtilsMetadataImpl(patch);
    }


}
