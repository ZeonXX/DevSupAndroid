package com.sup.dev.android.libs.mvp.navigator;

import android.content.Context;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;
import com.sup.dev.android.views.elements.dialogs.DialogProgressTransparent;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

import java.util.ArrayList;

public class MvpNavigatorImpl implements MvpNavigator {

    //
    //  Methods
    //

    public void showProgressDialog(CallbackSource<DialogProgressTransparent> onShow){
        SupAndroid.di.mvpActivity(activity -> {
            DialogProgressTransparent dialog = new DialogProgressTransparent((Context) activity);
            dialog.show();
            onShow.callback(dialog);
        });
    }

    //
    //  Presenters
    //

    private ArrayList<MvpPresenterInterface> presenters = new ArrayList<>();
    private int key;

    private void addPresenter(MvpPresenterInterface presenter) {
        presenter.setKey(++key);
        presenters.add(presenter);
    }

    private void removePresenter(int key) {
        for (int i = 0; i < presenters.size(); i++)
            if (presenters.get(i).getKey() == key)
                presenters.remove(i--).onDestroy();
    }

    @Override
    public MvpPresenterInterface getPresenter(int key) {
        for (int i = 0; i < presenters.size(); i++)
            if (presenters.get(i).getKey() == key)
                return presenters.get(i);
        return null;
    }

    //
    //  Navigation
    //

    @Override
    public void to(MvpPresenterInterface presenter) {
        addPresenter(presenter);
        addFragment(presenter, getBackStackSize() > 1);
    }

    @Override
    public void replace(MvpPresenterInterface presenter) {
        boolean animated = back();
        addPresenter(presenter);
        addFragment(presenter, animated);
    }

    @Override
    public void set(MvpPresenterInterface presenter) {
        boolean animated = !presenters.isEmpty();

        while (back()) ;

        addPresenter(presenter);
        addFragment(presenter, animated);
    }

    @Override
    public boolean back() {
        if (presenters.size() == 0) return false;

        removePresenter(getCurrent().getKey());
        backFragment();

        return true;
    }

    @Override
    public void remove(MvpPresenterInterface presenter) {
        if (getCurrent() == presenter)
            back();
        else
            removePresenter(presenter.getKey());
    }

    //
    //  Fragments
    //

    private void addFragment(MvpPresenterInterface presenter, boolean animated) {
        SupAndroid.di.mvpActivity(mvpActivity -> mvpActivity.addFragment(presenter.instanceFragment(), presenter.getKey() + "", animated));
    }

    private void backFragment() {
        SupAndroid.di.mvpActivity(mvpActivity -> mvpActivity.backFragment());
    }


    //
    //  Activity
    //

    @Override
    public boolean onBackPressed() {
        return hasBackStack() && (getCurrent().onBackPressed() || back());

    }

    //
    //  Getters
    //

    public int getBackStackSize() {
        return presenters.size();
    }

    @Override
    public boolean hasBackStack() {
        return presenters.size() > 1;
    }

    public MvpPresenterInterface getCurrent() {
        if(presenters.isEmpty())return null;
        return presenters.get(presenters.size() - 1);
    }


}
