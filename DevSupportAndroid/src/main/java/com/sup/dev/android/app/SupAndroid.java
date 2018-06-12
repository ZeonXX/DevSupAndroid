package com.sup.dev.android.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.sup.dev.android.libs.mvp.activity.MvpActivity;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.dialogs.DialogAlert;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.providers.Provider;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import java.util.ArrayList;

public class SupAndroid {

    public static String TEXT_APP_WHOOPS;
    public static String TEXT_APP_RETRY;
    public static String TEXT_APP_BACK;

    public static String TEXT_ERROR_PERMISSION_READ_FILES;
    public static String TEXT_ERROR_PCANT_LOAD_IMAGE;
    public static String TEXT_ERROR_NETWORK;
    public static String TEXT_ERROR_GONE;

    public static boolean editMode;
    public static Context appContext;

    public static void initEditMode(View view) {
        if (!view.isInEditMode()) return;
        editMode = true;
        init(view.getContext());
    }

    public static void init(Context appContext) {
        SupAndroid.appContext = appContext;
        ToolsThreads.setOnMain((onNextTime, runnable) -> {
            if (!onNextTime && ToolsAndroid.isMainThread()) runnable.run();
            else new Handler(Looper.getMainLooper()).post(runnable);
        });

        Debug.printer = s -> Log.e("Debug", s);
        Debug.exceptionPrinter = th -> Log.e("Debug", "", th);

        TEXT_APP_WHOOPS = ToolsResources.getString("app_whoops");
        TEXT_APP_RETRY = ToolsResources.getString("app_retry");
        TEXT_APP_BACK = ToolsResources.getString("app_back");
        TEXT_ERROR_PERMISSION_READ_FILES = ToolsResources.getString("error_permission_files");
        TEXT_ERROR_PCANT_LOAD_IMAGE = ToolsResources.getString("error_cant_load_image");
        TEXT_ERROR_NETWORK = ToolsResources.getString("error_network");
        TEXT_ERROR_GONE = ToolsResources.getString("error_gone");
    }

    //
    //  MVP Activity
    //

    private static MvpActivity mvpActivity;
    private static final ArrayList<Callback1<MvpActivity>> mvpActivityCallbacks = new ArrayList<>();
    public static final ArrayList<Provider<Boolean>> onbackCallbacks = new ArrayList<>();

    public static void setMvpActivity(MvpActivity mvpActivity) {
        SupAndroid.mvpActivity = mvpActivity;
        while (mvpActivity != null && !mvpActivityCallbacks.isEmpty())
            mvpActivityCallbacks.remove(0).callback(mvpActivity);
    }

    public static void mvpActivity(Callback1<MvpActivity> onActivity) {
        if (mvpActivity == null)
            mvpActivityCallbacks.add(onActivity);
        else
            ToolsThreads.main(() -> onActivity.callback(mvpActivity));
    }

    public static MvpActivity mvpActivityNow() {
        return mvpActivity;
    }

    public static boolean mvpActivityIsSubscribed(Callback1<MvpActivity> onActivity) {
        return mvpActivityCallbacks.contains(onActivity);
    }

    public static void addOnBack(Provider<Boolean> onBack) {
        if (onbackCallbacks.contains(onBack)) onbackCallbacks.remove(onBack);
        onbackCallbacks.add(onBack);
    }

    public static void removeOnBack(Provider<Boolean> onBack) {
        onbackCallbacks.remove(onBack);
    }

    public static boolean onBack() {
        for (int i = onbackCallbacks.size() - 1; i > -1; i--) {
            Provider<Boolean> onBack = onbackCallbacks.remove(i);
            if (onBack.provide()) return true;
        }
        return false;
    }


    //
    //  Debug Dialog
    //

    private static DialogAlert dialog;

    public static void initDebugDialog(Context viewContext, int autoShowCharsLimit) {

        dialog = new DialogAlert(viewContext == null ? appContext : viewContext);

        Debug.exceptionPrinter = th -> {
            Log.e("Debug", "", th);
            String stackTraceString = Log.getStackTraceString(th);
            dialog.addLine(stackTraceString);
            if (stackTraceString.length() >= autoShowCharsLimit)
                showDebugDialog();
        };
    }

    public static void showDebugDialog() {
        dialog.show();
    }

}
