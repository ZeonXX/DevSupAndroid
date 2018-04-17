package com.sup.dev.android.libs.mvp.activity;

import com.sup.dev.android.libs.mvp.fragments.MvpFragmentInterface;

public interface MvpActivity {

    void onFragmentBackPressed();

    void setFragment(MvpFragmentInterface view);

}
