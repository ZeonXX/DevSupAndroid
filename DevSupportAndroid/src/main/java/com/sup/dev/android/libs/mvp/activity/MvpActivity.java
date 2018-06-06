package com.sup.dev.android.libs.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.MvpFragment;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.tools.ToolsIntent;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.Subscription;
import com.sup.dev.java.tools.ToolsThreads;

public class MvpActivity extends Activity {

    public static boolean started;

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
        SupAndroid.setMvpActivity(this);
        if (vContainer.getChildCount() == 0) MvpNavigator.updateFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SupAndroid.setMvpActivity(null);
        MvpNavigator.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MvpNavigator.onActivityDestroy();
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
        ToolsIntent.onActivityResult(requestCode, resultCode, intent);
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void onFragmentBackPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!SupAndroid.onBack() && !MvpNavigator.onBackPressed() && !onLastBackPressed()) {
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

    public void setFragment(MvpFragment fragment) {

        if (fragment == null) {
            finish();
            return;
        }

        View old = vContainer.getChildCount() == 0 ? null : vContainer.getChildAt(0);
        vContainer.addView(fragment, 0);

        if (old != null) {
            (fragment).setVisibility(View.INVISIBLE);
            vTouchLock.setVisibility(View.VISIBLE);
            ToolsView.fromAlpha(fragment);
            ToolsView.toAlpha(old, () -> vContainer.removeView(old));
        }

        if(subscriptionTouchLock != null) subscriptionTouchLock.unsubscribe();
        subscriptionTouchLock = ToolsThreads.main(ToolsView.ANIMATION_TIME, () -> vTouchLock.setVisibility(View.GONE));
    }


}
