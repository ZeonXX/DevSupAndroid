package tests._sup_android.stubs.utils;

import android.app.Activity;

import com.sup.dev.android.utils.interfaces.UtilsNotifications;

public class UtilsNotificationsStub implements UtilsNotifications {
    @Override
    public void notification(int icon, int title, int body, Class<? extends Activity> activityClass) {

    }

    @Override
    public void notification(int icon, String body, Class<? extends Activity> activityClass) {

    }

    @Override
    public void notification(int icon, String title, String body, Class<? extends Activity> activityClass) {

    }

    @Override
    public void notification(int icon, String title, String body, Class<? extends Activity> activityClass, boolean sound) {

    }
}
