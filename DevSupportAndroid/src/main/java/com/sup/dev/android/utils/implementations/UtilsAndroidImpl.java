package com.sup.dev.android.utils.implementations;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Build;
import android.os.Looper;
import android.os.PowerManager;
import android.view.WindowManager;

import com.sup.dev.android.androiddevsup.BuildConfig;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.magic_box.Miui;
import com.sup.dev.android.utils.interfaces.UtilsAndroid;

import java.util.List;
import java.util.Locale;

import static android.content.Context.WINDOW_SERVICE;

public class UtilsAndroidImpl implements UtilsAndroid {
    
    //
    //  Device
    //

    public String getLanguageCode() {
        return Locale.getDefault().getLanguage().toLowerCase();
    }

    public boolean isEchoCancelerAvailable() {
        boolean echoCancelerAvailable = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            echoCancelerAvailable = AcousticEchoCanceler.isAvailable();
        return echoCancelerAvailable;
    }

    public boolean isDirectToTV() {
        return SupAndroid.di.appContext().getPackageManager().hasSystemFeature("android.software.leanb‌​ack")
                || SupAndroid.di.appContext().getPackageManager().hasSystemFeature("android.software.live_tv")
                || ((UiModeManager) SupAndroid.di.appContext().getSystemService(Context.UI_MODE_SERVICE)).getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION
                || Build.MODEL.toLowerCase().contains("tv box");
    }

    public boolean appIsVisible() {
        ActivityManager activityManager = (ActivityManager) SupAndroid.di.appContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> proc = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : proc)
            if (info.processName.equals(SupAndroid.di.appContext().getPackageName()))
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    return true;

        return false;

    }

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public long getMaxMemory(){
        return Runtime.getRuntime().totalMemory();
    }

    public long getFreeMemory(){
        Runtime runtime =  Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    @Override
    public boolean isMiui() {
        return Miui.isMiui();
    }

    //
    //  Package / Process
    //

    public String getCurrentProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) SupAndroid.di.appContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid)
                return process.processName;
        }
        return null;
    }

    public boolean hasBroadcastReceiver(String process, Intent intent, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> listeners = pm.queryBroadcastReceivers(intent, 0);
        for (ResolveInfo info : listeners) {
            if (info.activityInfo.processName.equals(process))
                return true;
        }
        return false;
    }

    public boolean checkServiceStarted(String appId, String serviceName) {

        ActivityManager am = (ActivityManager) SupAndroid.di.appContext().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(150))
            if (appId.equals(rsi.service.getPackageName())
                    && serviceName.toLowerCase().equals(rsi.service.getClassName().toLowerCase()))
                return true;

        return false;
    }

    //
    //  Screen
    //

    public boolean isScreenPortrait() {
        return !isScreenLandscape();
    }

    public boolean isScreenLandscape() {
        return getScreenW() > getScreenH();
    }

    public int getScreenOrientation() {
        return ((WindowManager) SupAndroid.di.appContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
    }

    public int getScreenW() {
        return SupAndroid.di.appContext().getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenH() {
        return SupAndroid.di.appContext().getResources().getDisplayMetrics().heightPixels;
    }

    public int maxScreenSide() {
        return Math.max(getScreenW(), getScreenH());
    }

    public int minScreenSide() {
        return Math.min(getScreenW(), getScreenH());
    }

    public boolean isScreenKeyLocked() {
        KeyguardManager myKeyManager = (KeyguardManager) SupAndroid.di.appContext().getSystemService(Context.KEYGUARD_SERVICE);
        return myKeyManager.inKeyguardRestrictedInputMode();
    }

    public void screenOn() {
        if (((PowerManager) SupAndroid.di.appContext().getSystemService(Context.POWER_SERVICE)).isScreenOn())
            return;

        PowerManager pm = (PowerManager) SupAndroid.di.appContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock screenLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "FULL WAKE LOCK");

        screenLock.acquire();
        new Thread(() -> {
            SupAndroid.di.utilsThreads().sleep(5000);
            if (screenLock.isHeld())
                screenLock.release();
        }).start();

    }



}
