package com.sup.dev.android.views.fragments.alert;

import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class PAlert extends MvpPresenter<FAlert> {

    public static void showNetwork(MvpNavigator.Action action, Callback onRetry) {
        MvpNavigator.action(action, new PAlert(
                ToolsResources.getString("app_whoops"),
                ToolsResources.getString("error_network"),
                ToolsResources.getString("app_retry"),
                onRetry));
    }

    public static void showGone(MvpNavigator.Action action) {
        MvpNavigator.action(action, new PAlert(
                ToolsResources.getString("app_whoops"),
                ToolsResources.getString("error_gone"),
                ToolsResources.getString("app_back"),
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
