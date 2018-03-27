package com.sup.dev.android.utils.implementations;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsToast;
import com.sup.dev.java.libs.debug.Debug;

public class UtilsToastImpl implements UtilsToast {

    public void show(@StringRes int textRes){
        show(SupAndroid.di.appContext().getString(textRes));
    }

    public void show(String text){
        SupAndroid.di.utilsThreads().main(() -> Toast.makeText(SupAndroid.di.appContext(), text, Toast.LENGTH_SHORT).show());
    }

}
