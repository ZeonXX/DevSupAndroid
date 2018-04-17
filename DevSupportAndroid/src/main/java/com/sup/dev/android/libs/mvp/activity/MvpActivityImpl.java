package com.sup.dev.android.libs.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.MvpFragmentInterface;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.utils.interfaces.UtilsIntent;
import com.sup.dev.android.utils.interfaces.UtilsView;

public class MvpActivityImpl extends Activity implements MvpActivity {

    public static boolean started;

    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final UtilsIntent utilsIntent = SupAndroid.di.utilsIntent();
    protected final MvpNavigator navigator = SupAndroid.di.navigator();

    private ViewGroup container;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        applyTheme();

        setContentView(getLayout());
        container = findViewById(R.id.fragment);

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
        if(container.getChildCount() == 0) navigator.updateFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SupAndroid.di.setMvpActivity(null);
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


    @Override
    public void setFragment(MvpFragmentInterface fragment) {

        if(fragment == null){
            finish();
            return;
        }

        View old = container.getChildCount() == 0 ? null : container.getChildAt(0);
        container.addView((View) fragment, 0);

        if (old != null) {
            ((View) fragment).setVisibility(View.INVISIBLE);
            utilsView.fromAlpha((View) fragment);
            utilsView.toAlpha(old, () -> container.removeView(old));
        }

    }


}
