package com.sup.dev.android.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.sup.dev.android.libs.eventbus_multi_process.EventBusMultiProcess;
import com.sup.dev.android.libs.screens.activity.SActivity;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

public class SupAndroid {

    public static int SERVICE_FOREGROUND = 4000;
    public static int SERVICE_NETWORK_CHECK = 4001;

    public static String TEXT_APP_NAME;
    public static String TEXT_APP_WHOOPS;
    public static String TEXT_APP_RETRY;
    public static String TEXT_APP_BACK;
    public static String TEXT_APP_CANCEL;
    public static String TEXT_APP_DONT_SHOW_AGAIN;
    public static String TEXT_APP_ATTENTION;
    public static String TEXT_APP_DOWNLOADING;
    public static String TEXT_APP_DOWNLOADED;
    public static String TEXT_APP_DONE;

    public static String TEXT_ERROR_PERMISSION_READ_FILES;
    public static String TEXT_ERROR_CANT_LOAD_IMAGE;
    public static String TEXT_ERROR_NETWORK;
    public static String TEXT_ERROR_GONE;
    public static String TEXT_ERROR_ACCOUNT_BANED;

    public static int IMG_ERROR_NETWORK;
    public static int IMG_ERROR_GONE;


    public static boolean editMode;
    public static Context appContext;
    public static SActivity activity;
    public static boolean activityIsVisible;

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
        Debug.printerInfo = (tag,s) -> Log.i(tag, s);
        Debug.exceptionPrinter = th -> Log.e("Debug", "", th);

        EventBusMultiProcess.init();

        TEXT_APP_NAME = loadText("app_name");
        TEXT_APP_WHOOPS = loadText("app_whoops");
        TEXT_APP_RETRY = loadText("app_retry");
        TEXT_APP_BACK = loadText("app_back");
        TEXT_APP_CANCEL = loadText("app_cancel");
        TEXT_APP_DONT_SHOW_AGAIN = loadText("app_dont_show_again");
        TEXT_APP_ATTENTION = loadText("app_attention");
        TEXT_ERROR_PERMISSION_READ_FILES = loadText("error_permission_files");
        TEXT_ERROR_CANT_LOAD_IMAGE = loadText("error_cant_load_image");
        TEXT_ERROR_NETWORK = loadText("error_network");
        TEXT_ERROR_GONE = loadText("error_gone");
        TEXT_ERROR_ACCOUNT_BANED = loadText("error_account_baned");
        TEXT_APP_DOWNLOADING = loadText("app_downloading");
        TEXT_APP_DOWNLOADED = loadText("app_downloaded");
        TEXT_APP_DONE = loadText("app_done");

        IMG_ERROR_NETWORK = loadImage("error_network");
        IMG_ERROR_GONE = loadImage("error_gone");


    }

    private static String loadText(String id){
        String t = ToolsResources.getString(id);
        if(t == null) Debug.error("Init warning: can't find text with id ["+id+"]");
        return t;
    }

    private static int loadImage(String id){
        int resId = ToolsResources.getDrawableId("error_network");
        if(resId < 1) Debug.error("Init warning: can't find image with id ["+id+"]");
        return resId;
    }


}
