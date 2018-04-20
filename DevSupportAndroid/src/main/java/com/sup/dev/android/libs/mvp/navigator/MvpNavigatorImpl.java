package com.sup.dev.android.libs.mvp.navigator;

import android.content.Context;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.activity.MvpActivity;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;
import com.sup.dev.android.views.elements.dialogs.DialogProgressTransparent;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

import java.util.ArrayList;

public class MvpNavigatorImpl implements MvpNavigator, CallbackSource<MvpActivity> {

    private ArrayList<MvpPresenterInterface> presenters = new ArrayList<>();

    //
    //  Presenters
    //

    public void removePresenter(MvpPresenterInterface presenter){
        presenter.clearView();
        presenter.onDestroy();
        presenters.remove(presenter);
    }

    //
    //  Navigation
    //

    @Override
    public void to(MvpPresenterInterface presenter) {
        if(!presenters.isEmpty()) {
            if(!getCurrent().isBackStackAllowed()) removePresenter(presenter);
            else getCurrent().clearView();
        }
        presenters.add(presenter);
        updateFragment();
    }

    @Override
    public void replace(MvpPresenterInterface presenter) {
        if(!presenters.isEmpty())removePresenter(getCurrent());
        to(presenter);
    }

    @Override
    public void set(MvpPresenterInterface presenter) {
        while (presenters.size() != 0) removePresenter(presenters.get(0));
        to(presenter);
    }

    @Override
    public boolean back() {
        if (!hasBackStack()) return false;

        removePresenter(getCurrent());
        updateFragment();

        return true;
    }

    @Override
    public void remove(MvpPresenterInterface presenter) {
        if (getCurrent() == presenter) back();
        else removePresenter(presenter);
    }

    //
    //  Activity
    //

    @Override
    public void updateFragment() {
        MvpActivity activity = SupAndroid.di.mvpActivityNow();
        if(activity == null){
            if(!SupAndroid.di.mvpActivityIsSubscribed(this))
                SupAndroid.di.mvpActivity(this);
        }else{
            callback(activity);
        }
    }

    @Override
    public void callback(MvpActivity activity) {
        activity.setFragment(getCurrent().instanceView((Context) activity));
    }

    @Override
    public boolean onBackPressed() {
        return (!presenters.isEmpty() && getCurrent().onBackPressed()) || back();
    }

    //
    //  Getters
    //

    @Override
    public boolean hasBackStack() {
        return presenters.size() > 1;
    }

    @Override
    public MvpPresenterInterface getCurrent() {
        if (presenters.isEmpty()) return null;
        return presenters.get(presenters.size() - 1);
    }

    //
    //  Support Methods
    //

    public void showProgressDialog(CallbackSource<DialogProgressTransparent> onShow) {
        SupAndroid.di.mvpActivity(activity -> {
            DialogProgressTransparent dialog = new DialogProgressTransparent((Context) activity);
            dialog.setCancelable(false);
            dialog.show();
            onShow.callback(dialog);
        });
    }

}
