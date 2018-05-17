package com.sup.dev.android.libs.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.MvpFragmentInterface;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.utils.interfaces.UtilsIntent;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.classes.Subscription;
import com.sup.dev.java.utils.interfaces.UtilsThreads;

import java.util.Locale;

public class MvpActivityImpl extends Activity implements MvpActivity {

    public static boolean started;

    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final UtilsIntent utilsIntent = SupAndroid.di.utilsIntent();
    private final UtilsThreads utilsThreads = SupAndroid.di.utilsThreads();
    protected final MvpNavigator navigator = SupAndroid.di.navigator();

    private ViewGroup vContainer;
    private View vTouchLock;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        applyTheme();

        setContentView(getLayout());
        vContainer = findViewById(R.id.mvp_activity_fragment);
        vTouchLock = findViewById(R.id.mvp_activity_touch_lock);

        vTouchLock.setVisibility(View.GONE);

        if (!started) {
            started = true;
            onFirstStart();
        }

    }

    protected void applyTheme() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        SupAndroid.di.setMvpActivity(this);
        if (vContainer.getChildCount() == 0) navigator.updateFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SupAndroid.di.setMvpActivity(null);
        navigator.onActivityStop();
    }

    protected int getLayout() {
        return R.layout.mvp_activity;
    }

    protected void onFirstStart() {

    }

    //
    //  Events
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        utilsIntent.onActivityResult(requestCode, resultCode, intent);
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onFragmentBackPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!navigator.onBackPressed() && !onLastBackPressed()) {
            started = false;
            finish();
        }
    }

    protected boolean onLastBackPressed() {
        return false;
    }


    //
    //  Fragments
    //

    private  Subscription subscriptionTouchLock;

    @Override
    public void setFragment(MvpFragmentInterface fragment) {

        if (fragment == null) {
            finish();
            return;
        }

        View old = vContainer.getChildCount() == 0 ? null : vContainer.getChildAt(0);
        vContainer.addView((View) fragment, 0);

        if (old != null) {
            ((View) fragment).setVisibility(View.INVISIBLE);
            vTouchLock.setVisibility(View.VISIBLE);
            utilsView.fromAlpha((View) fragment);
            utilsView.toAlpha(old, () -> vContainer.removeView(old));
        }

        if(subscriptionTouchLock != null) subscriptionTouchLock.unsubscribe();
        subscriptionTouchLock = utilsThreads.main(UtilsView.ANIMATION_TIME, () -> vTouchLock.setVisibility(View.GONE));
    }


}
