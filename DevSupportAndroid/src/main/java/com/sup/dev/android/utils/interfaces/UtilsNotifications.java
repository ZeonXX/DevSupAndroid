package com.sup.dev.android.utils.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public interface UtilsNotifications {

    void notification(@DrawableRes int icon, @StringRes int title, @StringRes int body, Class<? extends Activity> activityClass);

    void notification(@DrawableRes int icon, String body, Class<? extends Activity> activityClass);

    void notification(@DrawableRes int icon, String title, String body, Class<? extends Activity> activityClass);

    void notification(@DrawableRes int icon, String title, String body, Class<? extends Activity> activityClass, boolean sound);

    void notification(@DrawableRes int icon, String title, String body, Intent intent, boolean sound);

    void hide();


}
