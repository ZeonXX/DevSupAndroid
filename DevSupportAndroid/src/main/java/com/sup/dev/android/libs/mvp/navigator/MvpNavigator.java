package com.sup.dev.android.libs.mvp.navigator;

import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;
import com.sup.dev.android.views.elements.dialogs.DialogProgressTransparent;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public interface MvpNavigator {

    boolean hasBackStack();

    MvpPresenterInterface getCurrent();

    void to(MvpPresenterInterface presenter);

    void replace(MvpPresenterInterface presenter);

    void set(MvpPresenterInterface presenter);

    void remove(MvpPresenterInterface presenter);

    void updateFragment();

    boolean back();

    boolean onBackPressed();

    void showProgressDialog(CallbackSource<DialogProgressTransparent> onShow);

}
