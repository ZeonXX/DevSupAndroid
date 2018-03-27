package com.sup.dev.android.utils.interfaces;

import android.support.annotation.StringRes;

public interface UtilsToast {

    void show(@StringRes int textRes);

    void show(String text);


}
