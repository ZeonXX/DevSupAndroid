package com.sup.dev.android.libs.mvp.navigator;

import android.content.Context;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.activity.MvpActivity;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.elements.dialogs.DialogProgressTransparent;
import com.sup.dev.android.views.elements.dialogs.DialogProgressWithTitle;
import com.sup.dev.java.classes.callbacks.list.CallbacksList2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

import java.util.ArrayList;

public class Navigator {

    private static ArrayList<MvpPresenter> presenters = new ArrayList<>();
    private static Callback1<MvpActivity> callback1 = mvpActivity -> callback(mvpActivity);

    //
    //  Presenters
    //

    public static void removePresenter(MvpPresenter presenter){
        presenter.clearView();
        presenter.onDestroy();
        presenters.remove(presenter);
    }

    //
    //  Navigation
    //

    public static void to(MvpPresenter presenter) {
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

    public static void replace(MvpPresenter presenter) {
        if(!presenters.isEmpty())removePresenter(getCurrent());
        to(presenter);
    }

    public static void set(MvpPresenter presenter) {
        while (presenters.size() != 0) removePresenter(presenters.get(0));
        to(presenter);
    }

    public static boolean back() {
        if (!hasBackStack()) return false;

        MvpPresenter current = getCurrent();
        removePresenter(current);
        updateFragment();

        onBack.callback(current, getCurrent());

        return true;
    }

    public static void remove(MvpPresenter presenter) {
        if (getCurrent() == presenter) back();
        else removePresenter(presenter);
    }

    //
    //  Activity
    //

    public static void updateFragment() {
        MvpActivity activity = SupAndroid.mvpActivityNow();
        if(activity == null){
            if(!SupAndroid.mvpActivityIsSubscribed(callback1))
                SupAndroid.mvpActivity(callback1);
        }else{
            callback(activity);
        }
    }

    public static void callback(MvpActivity activity) {
        activity.setFragment(getCurrent().instanceView((Context) activity));
        getCurrent().onResume();
    }

    public static void onActivityStop() {
        getCurrent().onPause();
    }

    public static void onActivityDestroy() {
        getCurrent().clearView();
    }

    public static boolean onBackPressed() {
        return (!presenters.isEmpty() && getCurrent().onBackPressed()) || back();
    }

    //
    //  Getters
    //

    public static boolean hasBackStack() {
        return presenters.size() > 1;
    }

    public static MvpPresenter getCurrent() {
        if (presenters.isEmpty()) return null;
        return presenters.get(presenters.size() - 1);
    }

    //
    //  Support Methods
    //

    public static void showProgressDialog(Callback1<DialogProgressTransparent> onShow) {
        SupAndroid.mvpActivity(activity -> {
            DialogProgressTransparent dialog = new DialogProgressTransparent((Context) activity);
            dialog.setCancelable(false);
            dialog.show();
            onShow.callback(dialog);
        });
    }

    public static void showProgressDialog(int title, Callback1<DialogProgressWithTitle> onShow) {
        showProgressDialog(ToolsResources.getString(title), onShow);
    }

    public static void showProgressDialog(String title, Callback1<DialogProgressWithTitle> onShow) {
        SupAndroid.mvpActivity(activity -> {
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

    private static final CallbacksList2<MvpPresenter, MvpPresenter> onBack = new CallbacksList2<>();

    public static void addOnBackListener(Callback2<MvpPresenter, MvpPresenter> onBack) {
        Navigator.onBack.add(onBack);
    }

    public static void removeOnBackListener(Callback2<MvpPresenter, MvpPresenter> onBack) {
        Navigator.onBack.remove(onBack);
    }

}
