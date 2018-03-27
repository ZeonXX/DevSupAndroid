package com.sup.dev.android.libs.mvp.activity_navigation;


import android.app.Fragment;

import com.sup.dev.android.libs.mvp.activity.MvpActivity;

public interface MvpActivityNavigation extends MvpActivity {

    void setNavigationLock(boolean lock);
}
