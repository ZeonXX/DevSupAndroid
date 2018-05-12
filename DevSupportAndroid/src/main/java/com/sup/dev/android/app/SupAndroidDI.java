package com.sup.dev.android.app;


import android.content.Context;
import android.database.Cursor;

import com.sup.dev.android.libs.mvp.activity.MvpActivity;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.utils.interfaces.UtilsAndroid;
import com.sup.dev.android.utils.interfaces.UtilsAnimator;
import com.sup.dev.android.utils.interfaces.UtilsBitmap;
import com.sup.dev.android.utils.interfaces.UtilsCash;
import com.sup.dev.android.utils.interfaces.UtilsCursor;
import com.sup.dev.android.utils.interfaces.UtilsFiles;
import com.sup.dev.android.utils.interfaces.UtilsImageLoader;
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
import com.sup.dev.java.app.SupJavaDI;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public interface SupAndroidDI extends SupJavaDI {

    void setMvpActivity(MvpActivity mvpActivity);

    Context appContext();

    String appName();

    void mvpActivity(Callback1<MvpActivity> onActivity);

    MvpActivity mvpActivityNow();

    boolean mvpActivityIsSubscribed(Callback1<MvpActivity> onActivity);

    //
    //  Libs
    //

    MvpNavigator navigator();

    //
    //  Utils
    //

    UtilsAndroid utilsAndroid();

    UtilsBitmap utilsBitmap();

    UtilsCursor utilsCursor(Cursor cursor);

    UtilsFiles utilsFiles();
    
    UtilsIntent utilsIntent();

    UtilsMediaPlayer utilsMediaPlayer();

    UtilsNotifications utilsNotifications();

    UtilsPermission utilsPermission();

    UtilsResources utilsResources();

    UtilsStorage utilsStorage();

    UtilsText utilsText();

    UtilsToast utilsToast();

    UtilsView utilsView();

    UtilsCash utilsCash();

    UtilsMetadata utilsMetadata();

    UtilsMetadata utilsMetadata(String patch);

    UtilsImageLoader utilsImageLoader();

    UtilsAnimator utilsAnimator();

}
