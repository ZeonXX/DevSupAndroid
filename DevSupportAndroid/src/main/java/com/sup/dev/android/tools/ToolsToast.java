package com.sup.dev.android.tools;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.tools.ToolsThreads;

public class ToolsToast {

    public static void show(@StringRes int textRes){
        show(SupAndroid.appContext.getString(textRes));
    }

    public static void show(String text){
        ToolsThreads.main(() -> Toast.makeText(SupAndroid.appContext, text, Toast.LENGTH_SHORT).show());
    }

}
