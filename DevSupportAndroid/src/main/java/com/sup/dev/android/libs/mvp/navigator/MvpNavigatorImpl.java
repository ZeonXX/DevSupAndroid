package com.sup.dev.android.libs.mvp.navigator;

import android.content.Context;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.activity.MvpActivity;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;
import com.sup.dev.android.views.elements.dialogs.DialogProgressTransparent;
import com.sup.dev.android.views.elements.dialogs.DialogProgressWithTitle;
import com.sup.dev.java.classes.callbacks.list.CallbacksList2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.libs.debug.Debug;

import java.util.ArrayList;

public class MvpNavigatorImpl implements MvpNavigator, Callback1<MvpActivity> {

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

    public void to(MvpPresenterInterface presenter) {
        if(!presenters.isEmpty()) {
            if(!getCurrent().isBackStackAllowed()) removePresenter(getCurrent());
            else {
                getCurrent().clearView();
                getCurrent().onPause();
            }
        }
        presenters.add(presenter);
        updateFragment();
    }

    public void replace(MvpPresenterInterface presenter) {
        if(!presenters.isEmpty())removePresenter(getCurrent());
        to(presenter);
    }

    public void set(MvpPresenterInterface presenter) {
        while (presenters.size() != 0) removePresenter(presenters.get(0));
        to(presenter);
    }

    public boolean back() {
        if (!hasBackStack()) return false;

        MvpPresenterInterface current = getCurrent();
        removePresenter(current);
        updateFragment();

        onBack.callback(current, getCurrent());

        return true;
    }

    public void remove(MvpPresenterInterface presenter) {
        if (getCurrent() == presenter) back();
        else removePresenter(presenter);
    }

    //
    //  Activity
    //

    public void updateFragment() {
        MvpActivity activity = SupAndroid.di.mvpActivityNow();
        if(activity == null){
            if(!SupAndroid.di.mvpActivityIsSubscribed(this))
                SupAndroid.di.mvpActivity(this);
        }else{
            callback(activity);
        }
    }

    public void callback(MvpActivity activity) {
        activity.setFragment(getCurrent().instanceView((Context) activity));
        getCurrent().onResume();
    }

    public void onActivityStop() {
        getCurrent().clearView();
        getCurrent().onPause();
    }

    public boolean onBackPressed() {
        return (!presenters.isEmpty() && getCurrent().onBackPressed()) || back();
    }

    //
    //  Getters
    //

    public boolean hasBackStack() {
        return presenters.size() > 1;
    }

    public MvpPresenterInterface getCurrent() {
        if (presenters.isEmpty()) return null;
        return presenters.get(presenters.size() - 1);
    }

    //
    //  Support Methods
    //

    public void showProgressDialog(Callback1<DialogProgressTransparent> onShow) {
        SupAndroid.di.mvpActivity(activity -> {
            DialogProgressTransparent dialog = new DialogProgressTransparent((Context) activity);
            dialog.setCancelable(false);
            dialog.show();
            onShow.callback(dialog);
        });
    }

    public void showProgressDialog(int title, Callback1<DialogProgressWithTitle> onShow) {
        showProgressDialog(SupAndroid.di.utilsResources().getString(title), onShow);
    }

    public void showProgressDialog(String title, Callback1<DialogProgressWithTitle> onShow) {
        SupAndroid.di.mvpActivity(activity -> {
            DialogProgressWithTitle dialog = new DialogProgressWithTitle((Context) activity);
            dialog.setTitle(title);
            dialog.setCancelable(false);
            dialog.show();
            onShow.callback(dialog);
        });
    }

    //
    //  Listeners
    //

    private final CallbacksList2<MvpPresenterInterface, MvpPresenterInterface> onBack = new CallbacksList2<>();

    public void addOnBackListener(Callback2<MvpPresenterInterface, MvpPresenterInterface> onBack) {
        this.onBack.add(onBack);
    }

    public void removeOnBackListener(Callback2<MvpPresenterInterface, MvpPresenterInterface> onBack) {
        this.onBack.remove(onBack);
    }

}
