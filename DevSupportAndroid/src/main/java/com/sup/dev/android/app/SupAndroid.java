package com.sup.dev.android.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.sup.dev.android.libs.mvp.activity.MvpActivity;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.views.dialogs.DialogAlert;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import java.util.ArrayList;

public class SupAndroid{

    public static boolean editMode;
    public static Context appContext;

    public static void initEditMode(View view){
        if(!view.isInEditMode())return;
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


    //
    //  MVP Activity
    //

    private static MvpActivity mvpActivity;
    private static final ArrayList<Callback1<MvpActivity>> mvpActivityCallbacks = new ArrayList<>();

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

}
