package com.sup.dev.android.libs.mvp.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.utils.interfaces.UtilsIntent;

public class MvpActivityImpl extends AppCompatActivity implements MvpActivity{

    public static boolean started;

    private UtilsIntent utilsIntent = SupAndroid.di.utilsIntent();
    protected MvpNavigator navigator = SupAndroid.di.navigator();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(getLayout());

        if (!started) {
            started = true;
            onFirstStart();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        SupAndroid.di.setMvpActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SupAndroid.di.setMvpActivity(null);
    }

    protected int getLayout(){
        return R.layout.mvp_activity;
    }

    protected void onFirstStart(){

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

    protected boolean onLastBackPressed(){
        return false;
    }


    //
    //  Fragments
    //

    @Override
    public void addFragment(Fragment fragment, String key, boolean animate) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(key + "");
        if (animate)
            transaction.setCustomAnimations(R.animator.enter, R.animator.exet, R.animator.enter, R.animator.exet);
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    @Override
    public void backFragment() {
        getFragmentManager().popBackStack();
    }





}
