package com.sup.dev.android.tools;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.tools.ToolsThreads;

public class ToolsToast {

    public static void show(@StringRes int textRes) {
        show(SupAndroid.appContext.getString(textRes));
    }

    public static void show(@StringRes int textRes, Object... args) {
        show(ToolsResources.getString(textRes, args));
    }

    public static void show(String text) {
        ToolsThreads.main(() -> showNow(text));
    }

    private static void showNow(String text) {
        if (text == null || text.isEmpty()) return;
        Toast.makeText(SupAndroid.appContext, text, Toast.LENGTH_SHORT).show();
    }

    public static void showSnack(@StringRes int textRes) {
        showSnack(SupAndroid.appContext.getString(textRes));
    }

    public static void showSnack(String text) {
        ToolsThreads.main(() -> showSnackNow(SupAndroid.activity.getViewContainer(), text));
    }

    public static void showSnack(View v, @StringRes int textRes) {
        showSnack(v, SupAndroid.appContext.getString(textRes));
    }

    public static void showSnack(View v, String text) {
        ToolsThreads.main(() -> showSnackNow(v, text));
    }

    private static void showSnackNow(View v, String text) {
        if (text == null || text.isEmpty()) return;
        Snackbar snack = Snackbar.make(v, text, Snackbar.LENGTH_SHORT);
        View snackbarView = snack.getView();
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        snack.show();
    }

    public static void showSnackIfLocked(View v, @StringRes int textRes) {
        showSnackIfLocked(v, ToolsResources.getString(textRes));
    }

    public static void showSnackIfLocked(View v, String text) {
        ToolsThreads.main(() -> {
            if (ToolsAndroid.isScreenKeyLocked()) showSnackNow(v, text);
            else showNow(text);
        });
    }


}
