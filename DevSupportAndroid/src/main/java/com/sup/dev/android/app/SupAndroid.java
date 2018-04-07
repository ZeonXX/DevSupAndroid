package com.sup.dev.android.app;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.sup.dev.android.views.elements.dialogs.DialogAlert;
import com.sup.dev.java.app.SupJava;
import com.sup.dev.java.libs.debug.Debug;

public class SupAndroid{

    public static SupAndroidDI di;
    public static boolean editMode;

    public static void initEditMode(View view){
        if(!view.isInEditMode())return;
        SupAndroid.di = new SupAndroidDIImpl(view.getContext());
        SupJava.init(di);
        editMode = true;
    }

    public static void init(SupAndroidDI di) {
        SupAndroid.di = di;
        SupJava.init(di);

        Debug.printer = s -> Log.e("Debug", s);
        Debug.exceptionPrinter = th -> Log.e("Debug", "", th);

    }

    //
    //  Debug Dialog
    //

    private static DialogAlert dialog;

    public static void initDebugDialog(Context viewContext, int autoShowCharsLimit) {

        dialog = new DialogAlert(viewContext == null ? di.appContext() : viewContext);

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
