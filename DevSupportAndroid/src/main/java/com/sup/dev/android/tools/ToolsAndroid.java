package com.sup.dev.android.tools;

import android.Manifest;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.UiModeManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.audiofx.AcousticEchoCanceler;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.RequiresPermission;
import android.view.WindowManager;

import com.sup.dev.android.BuildConfig;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.magic_box.Miui;
import com.sup.dev.android.magic_box.ServiceNetworkCheck;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.WINDOW_SERVICE;

public class ToolsAndroid {

    //
    //  Device
    //

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public static String getBluetoothMacAddress() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {

            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        } else {
            bluetoothMacAddress = bluetoothAdapter.getAddress();
        }
        return bluetoothMacAddress;
    }

    public static String getProcessName() {
        String processName = getProcessNameCmdLine();
        if(processName == null) getProcessNameActivityManager();
        return processName;
    }

    private static String getProcessNameCmdLine() {
        BufferedReader cmdlineReader = null;
        try {
            cmdlineReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + android.os.Process.myPid() + "/cmdline"), "iso-8859-1"));
            int c;
            StringBuilder processName = new StringBuilder();
            while ((c = cmdlineReader.read()) > 0) processName.append((char) c);
            return processName.toString();
        } catch (Exception ex) {
            Debug.log(ex);
        } finally {
            if (cmdlineReader != null) {
                try {
                    cmdlineReader.close();
                } catch (IOException e) {
                    Debug.log(e);
                }
            }
        }
        return null;
    }

    private static String getProcessNameActivityManager() {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) SupAndroid.appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : infos) {
                if (processInfo.pid == pid) {
                    return processInfo.processName;
                }
            }
        }
        return null;
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static String getNetworkName() {
        ConnectivityManager cm = (ConnectivityManager) SupAndroid.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo n = cm.getActiveNetworkInfo();
        return (n == null) ? null : n.getExtraInfo();
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static void isHasInternetConnection(Callback1<Boolean> onResult) {
        ServiceNetworkCheck.check(onResult);
    }

    public static int getBottomNavigationBarHeight() {
        Resources resources = SupAndroid.appContext.getResources();
        boolean navBarExists = resources.getBoolean(SupAndroid.appContext.getResources().getIdentifier("config_showNavigationBar", "bool", "android"));
        return navBarExists?resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android")):0;
    }

    public static boolean isTablet() {
        return (SupAndroid.appContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getLanguageCode() {
        return Locale.getDefault().getLanguage().toLowerCase();
    }

    public static boolean isEchoCancelerAvailable() {
        return AcousticEchoCanceler.isAvailable();
    }

    public static boolean isDirectToTV() {
        return SupAndroid.appContext.getPackageManager().hasSystemFeature("android.software.leanb‌​ack")
                || SupAndroid.appContext.getPackageManager().hasSystemFeature("android.software.live_tv")
                || ((UiModeManager) SupAndroid.appContext.getSystemService(Context.UI_MODE_SERVICE)).getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION
                || Build.MODEL.toLowerCase().contains("tv box");
    }

    public static boolean appIsVisible() {
        ActivityManager activityManager = (ActivityManager) SupAndroid.appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> proc = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : proc)
            if (info.processName.equals(SupAndroid.appContext.getPackageName()))
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    return true;

        return false;

    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getFreeMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static boolean isMiui() {
        return Miui.isMiui();
    }

    public static void toClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) SupAndroid.appContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text);
        clipboard.setPrimaryClip(clip);
    }

    //
    //  Package / Process
    //

    public static boolean hasBroadcastReceiver(String process, Intent intent, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> listeners = pm.queryBroadcastReceivers(intent, 0);
        for (ResolveInfo info : listeners) {
            if (info.activityInfo.processName.equals(process))
                return true;
        }
        return false;
    }

    public static boolean checkServiceStarted(String appId, String serviceName) {

        ActivityManager am = (ActivityManager) SupAndroid.appContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(150))
            if (appId.equals(rsi.service.getPackageName())
                    && serviceName.toLowerCase().equals(rsi.service.getClassName().toLowerCase()))
                return true;

        return false;
    }

    public static File getSystemRootDir() {
        return Environment.getExternalStorageDirectory();
    }

    //
    //  Screen
    //

    public static boolean isScreenPortrait() {
        return !isScreenLandscape();
    }

    public static boolean isScreenLandscape() {
        return getScreenW() > getScreenH();
    }

    public static int getScreenOrientation() {
        return ((WindowManager) SupAndroid.appContext.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
    }

    public static int getScreenW() {
        return SupAndroid.appContext.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenH() {
        return SupAndroid.appContext.getResources().getDisplayMetrics().heightPixels;
    }

    public static int maxScreenSide() {
        return Math.max(getScreenW(), getScreenH());
    }

    public static int minScreenSide() {
        return Math.min(getScreenW(), getScreenH());
    }

    public static boolean isScreenKeyLocked() {
        KeyguardManager myKeyManager = (KeyguardManager) SupAndroid.appContext.getSystemService(Context.KEYGUARD_SERVICE);
        return myKeyManager.inKeyguardRestrictedInputMode();
    }

    public static void screenOn() {
        if (((PowerManager) SupAndroid.appContext.getSystemService(Context.POWER_SERVICE)).isScreenOn())
            return;

        PowerManager pm = (PowerManager) SupAndroid.appContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock screenLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "FULL WAKE LOCK");

        screenLock.acquire();
        new Thread(() -> {
            ToolsThreads.sleep(5000);
            if (screenLock.isHeld())
                screenLock.release();
        }).start();

    }


}
