package com.sup.dev.android.views.screens;

import android.widget.ImageView;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.NavigationAction;
import com.sup.dev.android.libs.screens.Navigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.views.views.ViewChip;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class SAlert extends Screen {

    public static void showNetwork(NavigationAction action, Callback onRetry) {
        Navigator.action(action, instanceNetwork(onRetry));
    }

    public static SAlert instanceNetwork(Callback onRetry) {
        return new SAlert(
                SupAndroid.TEXT_APP_WHOOPS,
                SupAndroid.TEXT_ERROR_NETWORK,
                SupAndroid.TEXT_APP_RETRY,
                SupAndroid.IMG_ERROR_NETWORK,
                onRetry);
    }

    public static void showGone(NavigationAction action) {
        Navigator.action(action, instanceGone());
    }

    public static SAlert instanceGone() {
        return new SAlert(
                SupAndroid.TEXT_APP_WHOOPS,
                SupAndroid.TEXT_ERROR_GONE,
                SupAndroid.TEXT_APP_BACK,
                SupAndroid.IMG_ERROR_GONE,
                () -> Navigator.back());
    }

    public SAlert(String title, String text, String action, Callback onAction) {
        this(title, text, action, 0, onAction);
    }

    public SAlert(String title, String text, String action, int image,Callback onAction) {
        this(title, text, action, image, 0, onAction);
    }

    public SAlert(String title, String text, String action, int image, int imageFul, Callback onAction) {
        super(R.layout.screen_alert);

        TextView vTitle = findViewById(R.id.title);
        TextView vText = findViewById(R.id.text);
        ViewChip vAction = findViewById(R.id.action);
        ImageView vImage = findViewById(R.id.image);
        ImageView vImageFull = findViewById(R.id.image_full);

        vTitle.setText(title);
        vText.setText(text);
        vAction.setText(action);

        if(image > 0) {
            vImage.setImageResource(image);
            vImage.setVisibility(VISIBLE);
        }else{
            vImage.setImageBitmap(null);
            vImage.setVisibility(GONE);
        }

        if(imageFul > 0) {
            vImageFull.setImageResource(imageFul);
            vImageFull.setVisibility(VISIBLE);
        }else{
            vImageFull.setImageBitmap(null);
            vImageFull.setVisibility(GONE);
        }

        vAction.setOnClickListener(v -> {
            if (onAction != null) onAction.callback();
        });

    }

}