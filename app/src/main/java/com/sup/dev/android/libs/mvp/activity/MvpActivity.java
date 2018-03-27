package com.sup.dev.android.libs.mvp.activity;

import android.app.Fragment;

public interface MvpActivity {

    void onFragmentBackPressed();

    void addFragment(Fragment fragment, String key, boolean animate);

    void backFragment();


}
