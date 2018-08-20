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
import com.sup.dev.java.tools.ToolsThreads;

public abstract class SActivity extends Activity {

    public static boolean started;

    protected View vActivityRoot;
    protected ViewGroup vActivityContainer;
    protected View vActivityTouchLock;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SupAndroid.activity = this;

        applyTheme();

        setContentView(getLayout());
        vActivityRoot = findViewById(R.id.screen_activity_root);
        vActivityContainer = findViewById(R.id.screen_activity_view);
        vActivityTouchLock = findViewById(R.id.screen_activity_touch_lock);

        vActivityTouchLock.setVisibility(View.GONE);
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
        return vActivityRoot;
    }

    public View getViewContainer() {
        return vActivityContainer;
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

    public void setScreen(Screen screen, Navigator.Animation animation) {

        if (screen == null) {
            finish();
            return;
        }

        ToolsView.hideKeyboard();
        View old = vActivityContainer.getChildCount() == 0 ? null : vActivityContainer.getChildAt(0);
        if (animation != Navigator.Animation.IN) vActivityContainer.addView(ToolsView.removeFromParent(screen), 0);
        else vActivityContainer.addView(ToolsView.removeFromParent(screen));

        if (old != null && old != screen) {

            vActivityTouchLock.setVisibility(View.VISIBLE);
            ToolsView.clearAnimation(old);
            ToolsView.clearAnimation(screen);

            if (animation == Navigator.Animation.NONE) {
                ToolsView.clearAnimation(old);
                vActivityContainer.removeView(old);
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
                                vActivityContainer.removeView(old);
                            }
                        });
            }
            if (animation == Navigator.Animation.IN) {
                screen.setAlpha(0);
                screen.setTranslationX(ToolsAndroid.getScreenW() / 3);
                screen.animate()
                        .alpha(1)
                        .translationX(0)
                        .setDuration(150)
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .setListener(new AnimatorListenerAdapter() {

                            public void onAnimationEnd(Animator animation) {
                                screen.animate().setListener(null);
                                screen.setAlpha(1);
                                screen.setTranslationX(0);
                                vActivityContainer.removeView(old);
                            }
                        });
            }

            if (animation == Navigator.Animation.ALPHA) {
                screen.setVisibility(View.INVISIBLE);
                ToolsView.toAlpha(old, () -> vActivityContainer.removeView(old));
                ToolsView.fromAlpha(screen);
            }

        }

        if (subscriptionTouchLock != null) subscriptionTouchLock.unsubscribe();
        subscriptionTouchLock = ToolsThreads.main(ToolsView.ANIMATION_TIME, () -> vActivityTouchLock.setVisibility(View.GONE));
    }


}
