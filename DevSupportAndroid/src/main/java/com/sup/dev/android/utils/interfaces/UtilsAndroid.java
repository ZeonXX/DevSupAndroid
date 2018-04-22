package com.sup.dev.android.utils.interfaces;

import android.content.Context;
import android.content.Intent;

import java.io.File;

public interface UtilsAndroid {

    String getLanguageCode();

    boolean isEchoCancelerAvailable();

    boolean isDirectToTV();

    boolean appIsVisible();

    boolean isDebug();

    boolean isMainThread();

    long getMaxMemory();

    long getFreeMemory();

    boolean isMiui();

    //
    //  Package / Process
    //

    String getCurrentProcess();

    boolean hasBroadcastReceiver(String process, Intent intent, Context context);

    boolean checkServiceStarted(String appId, String serviceName);

    File getSystemRootDir();

    //
    //  Screen
    //

    boolean isScreenPortrait();

    boolean isScreenLandscape();

    int getScreenOrientation();

    int getScreenW();

    int getScreenH();

    int maxScreenSide();

    int minScreenSide();

    boolean isScreenKeyLocked();

    void screenOn();

}
