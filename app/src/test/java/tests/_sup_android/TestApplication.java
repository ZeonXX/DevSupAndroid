package tests._sup_android;

import android.app.Application;

import com.sup.dev.android.androiddevsup.R;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.Theme_AppCompat);
    }
}