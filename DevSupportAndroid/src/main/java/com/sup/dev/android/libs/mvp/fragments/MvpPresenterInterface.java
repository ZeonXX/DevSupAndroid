package com.sup.dev.android.libs.mvp.fragments;

import android.content.Context;

public interface MvpPresenterInterface {


    MvpFragmentInterface instanceView(Context context);

    void clearView();

    void onResume();

    void onPause();

    void onDestroy();

    boolean onBackPressed();

    boolean isBackStackAllowed();
}
