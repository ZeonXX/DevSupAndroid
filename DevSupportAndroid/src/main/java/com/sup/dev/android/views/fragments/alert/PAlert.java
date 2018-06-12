package com.sup.dev.android.views.fragments.alert;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class PAlert extends MvpPresenter<FAlert> {

    public static void showNetwork(MvpNavigator.Action action, Callback onRetry) {
        MvpNavigator.action(action, new PAlert(
                SupAndroid.TEXT_APP_WHOOPS,
                SupAndroid.TEXT_ERROR_NETWORK,
                SupAndroid.TEXT_APP_RETRY,
                onRetry));
    }

    public static void showGone(MvpNavigator.Action action) {
        MvpNavigator.action(action, new PAlert(
                SupAndroid.TEXT_APP_WHOOPS,
                SupAndroid.TEXT_ERROR_GONE,
                SupAndroid.TEXT_APP_BACK,
                () -> MvpNavigator.back()));
    }

    private final Callback onAction;

    private PAlert(String title, String text, String action, Callback onAction) {
        super(FAlert.class);
        this.onAction = onAction;

        actionAdd(v -> v.setInfo(title, text, action));

    }

    //
    //  Fragment
    //

    public void onActionClicked() {
        if (onAction != null) onAction.callback();
    }

}
