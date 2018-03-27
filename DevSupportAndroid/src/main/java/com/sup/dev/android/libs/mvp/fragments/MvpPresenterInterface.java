package com.sup.dev.android.libs.mvp.fragments;

import android.app.Fragment;

public interface MvpPresenterInterface {

    int getKey();

    void setKey(int key);

    void onDestroy();

    Fragment instanceFragment();

    boolean onBackPressed();

    void onAttachView(MvpFragmentInterface view);

    void onDetachView();

}
