package com.sup.dev.android.views.screens;

import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.SNavigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.views.views.ViewChip;
import com.sup.dev.java.classes.callbacks.simple.Callback;

import static com.sup.dev.android.app.SupAndroid.SCREEN_ALERT_COLOR;

public class SAlert extends Screen {

    public static void showNetwork(SNavigator.Action action, Callback onRetry) {
        SNavigator.action(action, new SAlert(
                SupAndroid.TEXT_APP_WHOOPS,
                SupAndroid.TEXT_ERROR_NETWORK,
                SupAndroid.TEXT_APP_RETRY,
                onRetry));
    }

    public static void showGone(SNavigator.Action action) {
        SNavigator.action(action, instanceGone());
    }

    public static SAlert instanceGone(){
        return new SAlert(
                SupAndroid.TEXT_APP_WHOOPS,
                SupAndroid.TEXT_ERROR_GONE,
                SupAndroid.TEXT_APP_BACK,
                () -> SNavigator.back());
    }

    public SAlert(String title, String text, String action, Callback onAction) {
        super(R.layout.screen_alert);

        TextView vTitle = findViewById(R.id.title);
        TextView vText = findViewById(R.id.text);
        ViewChip vAction = findViewById(R.id.action);

        vTitle.setText(title);
        vText.setText(text);
        vAction.setText(action);

        if(SCREEN_ALERT_COLOR != 0)setBackgroundColor(SCREEN_ALERT_COLOR);

        vAction.setOnClickListener(v -> {
            if (onAction != null) onAction.callback();
        });

    }

}