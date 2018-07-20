package com.sup.dev.android.libs.screens.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.Navigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsIntent;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.Subscription;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

public abstract class SActivity extends Activity {

    public static boolean started;

    protected View vRoot;
    protected ViewGroup vContainer;
    protected View vTouchLock;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SupAndroid.activity = this;

        applyTheme();

        setContentView(getLayout());
        vRoot = findViewById(R.id.screen_activity_root);
        vContainer = findViewById(R.id.screen_activity_view);
        vTouchLock = findViewById(R.id.screen_activity_touch_lock);

        vTouchLock.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SupAndroid.activityIsVisible = true;
        Navigator.onActivityResume();

        if (!started) {
            started = true;
            onFirstStart();
        }

        if(Navigator.getStackSize() == 0)  toMainScreen();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SupAndroid.activityIsVisible = false;
        Navigator.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Navigator.onActivityDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Navigator.onActivityConfigChanged();
    }

    protected void applyTheme() {

    }

    protected int getLayout() {
        return R.layout.screen_activity;
    }

    protected void onFirstStart() {

    }

    protected abstract void toMainScreen();

    public View getViewRoot() {
        return vRoot;
    }

    public View getViewContainer() {
        return vContainer;
    }

    //
    //  Events
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ToolsIntent.onActivityResult(requestCode, resultCode, intent);
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void onViewBackPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!Navigator.onBackPressed() && !onLastBackPressed()) {
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

    private Subscription subscriptionTouchLock;

    public void setView(Screen view, Navigator.Animation animation) {

        if (view == null) {
            finish();
            return;
        }

        ToolsView.hideKeyboard();
        View old = vContainer.getChildCount() == 0 ? null : vContainer.getChildAt(0);
        if (animation != Navigator.Animation.IN) vContainer.addView(ToolsView.removeFromParent(view), 0);
        else vContainer.addView(ToolsView.removeFromParent(view));

        if (old != null && old != view) {

            vTouchLock.setVisibility(View.VISIBLE);
            ToolsView.clearAnimation(old);
            ToolsView.clearAnimation(view);

            if (animation == Navigator.Animation.NONE) {
                ToolsView.clearAnimation(old);
                vContainer.removeView(old);
            }

            if (animation == Navigator.Animation.OUT) {
                old.animate()
                        .alpha(0)
                        .translationX(ToolsAndroid.getScreenW() / 3)
                        .setDuration(150)
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .setListener(new AnimatorListenerAdapter() {

                            public void onAnimationEnd(Animator animation) {
                                old.animate().setListener(null);
                                old.setAlpha(1);
                                old.setTranslationX(0);
                                vContainer.removeView(old);
                            }
                        });
            }
            if (animation == Navigator.Animation.IN) {
                view.setAlpha(0);
                view.setTranslationX(ToolsAndroid.getScreenW() / 3);
                view.animate()
                        .alpha(1)
                        .translationX(0)
                        .setDuration(150)
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .setListener(new AnimatorListenerAdapter() {

                            public void onAnimationEnd(Animator animation) {
                                view.animate().setListener(null);
                                view.setAlpha(1);
                                view.setTranslationX(0);
                                vContainer.removeView(old);
                            }
                        });
            }

            if (animation == Navigator.Animation.ALPHA) {
                view.setVisibility(View.INVISIBLE);
                ToolsView.toAlpha(old, () -> vContainer.removeView(old));
                ToolsView.fromAlpha(view);
            }

        }

        if (subscriptionTouchLock != null) subscriptionTouchLock.unsubscribe();
        subscriptionTouchLock = ToolsThreads.main(ToolsView.ANIMATION_TIME, () -> vTouchLock.setVisibility(View.GONE));
    }


}
