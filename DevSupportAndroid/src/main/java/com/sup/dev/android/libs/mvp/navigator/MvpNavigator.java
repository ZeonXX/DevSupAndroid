package com.sup.dev.android.libs.mvp.navigator;

import android.support.annotation.StringRes;

import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;
import com.sup.dev.android.views.elements.dialogs.DialogProgressTransparent;
import com.sup.dev.android.views.elements.dialogs.DialogProgressWithTitle;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

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

    void showProgressDialog(Callback1<DialogProgressTransparent> onShow);

    void showProgressDialog(@StringRes int title, Callback1<DialogProgressWithTitle> onShow);

    void showProgressDialog(String title, Callback1<DialogProgressWithTitle> onShow);

}
