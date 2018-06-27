package com.sup.dev.android.views.screens;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public abstract class SLoading extends Screen {

    public enum State {NONE, EMPTY, PROGRESS, ERROR}

    protected final ViewGroup vContainer;
    protected final ViewGroup vToolbarContainer;
    protected final TextView vMessage;
    protected final TextView vAction;
    protected final View vProgress;
    protected final ImageView vEmptyImage;

    protected String textErrorNetwork = SupAndroid.TEXT_ERROR_NETWORK;
    protected String textRetry = SupAndroid.TEXT_APP_RETRY;
    protected String textEmpty;
    protected String textAction;
    protected Callback onAction;

    public SLoading(@LayoutRes int layoutRes) {
        super(R.layout.screen_loading);

        vContainer = findViewById(R.id.container);
        vToolbarContainer = findViewById(R.id.toolbar_icons_container);
        vMessage = findViewById(R.id.message);
        vAction = findViewById(R.id.action);
        vProgress = findViewById(R.id.progress);
        vEmptyImage = findViewById(R.id.empty_image);

        vAction.setVisibility(View.INVISIBLE);
        vMessage.setVisibility(View.INVISIBLE);

        setState(State.PROGRESS);
        setContent(layoutRes);
    }

    public abstract void onReloadClicked();

    protected void setContent(@LayoutRes int res) {
        while (vContainer.getChildCount() != 1) vContainer.removeViewAt(0);
        vContainer.addView(ToolsView.inflate(getContext(), res), 0);
    }

    protected ViewIcon addToolbarIcon(@DrawableRes int res, Callback onClick) {
        ViewIcon viewIcon = ToolsView.inflate(getContext(), R.layout.view_icon_toolbar);
        viewIcon.setImageResource(res);
        viewIcon.setOnClickListener(v -> onClick.callback());
        vToolbarContainer.addView(viewIcon);
        return viewIcon;
    }

    protected void setTextErrorNetwork(@StringRes int t) {
        textErrorNetwork = ToolsResources.getString(t);
    }

    protected void setTextRetry(@StringRes int t) {
        textRetry = ToolsResources.getString(t);
    }

    protected void setTextEmpty(@StringRes int t) {
        setTextEmpty(ToolsResources.getString(t));
    }

    protected void setAction(@StringRes int textAction, Callback onAction) {
        setAction(ToolsResources.getString(textAction), onAction);
    }

    protected void setAction(String textAction, Callback onAction) {
        this.textAction = textAction;
        this.onAction = onAction;
    }

    protected void setTextEmpty(String t) {
        textEmpty = t;
    }

    public void setBackgroundImage(@DrawableRes int res) {
        vEmptyImage.setImageResource(res);
    }


    //
    //  Presenter
    //

    public void setTitle(@StringRes int title) {
        setTitle(ToolsResources.getString(title));
    }

    public void setTitle(String title) {
        ((Toolbar) findViewById(R.id.toolbar)).setTitle(title);
    }

    public void setState(State state) {
        ToolsView.alpha(vAction, state == State.PROGRESS || state == State.NONE);
        ToolsView.alpha(vProgress, state != State.PROGRESS);
        ToolsView.alpha(vMessage, state == State.PROGRESS || state == State.NONE);
        ToolsView.alpha(vEmptyImage, state == State.NONE);

        if (state == State.ERROR) {

            vMessage.setText(textErrorNetwork);
            vAction.setText(textRetry);
            vAction.setOnClickListener(v -> onReloadClicked());
            ToolsView.alpha(vAction, vAction.getText().length() == 0);
        }

        if (state == State.EMPTY) {

            vMessage.setText(textEmpty);
            vAction.setText(textAction);
            vAction.setOnClickListener(v -> {
                if (onAction != null) onAction.callback();
            });
            ToolsView.alpha(vAction, vAction.getText().length() == 0);
        }

    }


}
