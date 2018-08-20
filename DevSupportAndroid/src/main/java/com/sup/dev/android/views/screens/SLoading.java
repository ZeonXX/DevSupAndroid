package com.sup.dev.android.views.screens;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

public abstract class SLoading extends Screen {

    public enum State {NONE, EMPTY, PROGRESS, ERROR}

    protected final ViewGroup vContainer;
    protected final ViewGroup vToolbarContainer;
    protected final Toolbar vToolbar;
    protected final TextView vMessage;
    protected final TextView vAction;
    protected final View vProgress;
    protected final ImageView vEmptyImage;
    protected final FloatingActionButton vFab;

    protected String textErrorNetwork = SupAndroid.TEXT_ERROR_NETWORK;
    protected String textRetry = SupAndroid.TEXT_APP_RETRY;
    protected String textEmpty;
    protected String textProgress;
    protected String textProgressAction;
    protected Callback onProgressAction;
    protected String textAction;
    protected Callback onAction;
    protected State state;

    public SLoading(@LayoutRes int layoutRes) {
        super(R.layout.screen_loading);

        vContainer = findViewById(R.id.container);
        vToolbarContainer = findViewById(R.id.toolbar_icons_container);
        vToolbar = findViewById(R.id.toolbar);
        vMessage = findViewById(R.id.message);
        vAction = findViewById(R.id.action);
        vProgress = findViewById(R.id.progress);
        vEmptyImage = findViewById(R.id.empty_image);
        vFab = findViewById(R.id.fab);

        vAction.setVisibility(View.INVISIBLE);
        vMessage.setVisibility(View.INVISIBLE);
        vProgress.setVisibility(View.INVISIBLE);
        vFab.setVisibility(View.GONE);
        vEmptyImage.setImageDrawable(null);

        setState(State.PROGRESS);
        setContent(layoutRes);
    }

    public abstract void onReloadClicked();

    protected void setContent(@LayoutRes int res) {
        while (vContainer.getChildCount() != 2) vContainer.removeViewAt(0);
        vContainer.addView(ToolsView.inflate(getContext(), res), 0);
    }

    protected ViewIcon addToolbarIcon(@DrawableRes int res, Callback1<View> onClick) {
        ViewIcon viewIcon = ToolsView.inflate(getContext(), R.layout.view_icon_toolbar);
        viewIcon.setImageResource(res);
        viewIcon.setOnClickListener(onClick::callback);
        vToolbarContainer.addView(viewIcon);
        return viewIcon;
    }

    protected void addToolbarView(View v) {
        vToolbar.addView(v);
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

    public void setTextProgress(String textProgress) {
        this.textProgress = textProgress;
    }

    public void setProgressAction(String textProgressAction, Callback onAction) {
        this.textProgressAction = textProgressAction;
        this.onProgressAction = onAction;
    }

    public void setProgressAction(String textProgressAction) {
        this.textProgressAction = textProgressAction;
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

    public void setTitle(@StringRes int title) {
        setTitle(ToolsResources.getString(title));
    }

    public void setTitle(String title) {
        ((Toolbar) findViewById(R.id.toolbar)).setTitle(title);
    }

    public void setState(State state) {
        this.state = state;

        if (state == State.PROGRESS) {
            ToolsThreads.main(600, () -> {
                ToolsView.alpha(vProgress, this.state != State.PROGRESS);
                if (this.state == State.PROGRESS && vMessage.getText().length() > 0) ToolsView.fromAlpha(vMessage);
            });
        } else ToolsView.toAlpha(vProgress);

        if (vEmptyImage.getDrawable() == null) vEmptyImage.setVisibility(GONE);
        else ToolsView.alpha(vEmptyImage, state == State.NONE);

        if (state == State.ERROR) {

            ToolsView.setTextOrGone(vMessage, textErrorNetwork);
            ToolsView.setTextOrGone(vAction, textRetry);
            vAction.setOnClickListener(v -> onReloadClicked());
        }

        if (state == State.EMPTY) {

            ToolsView.setTextOrGone(vMessage, textEmpty);
            ToolsView.setTextOrGone(vAction, textAction);
            vAction.setOnClickListener(v -> {
                if (onAction != null) onAction.callback();
            });
        }

        if (state == State.PROGRESS) {

            vMessage.setVisibility(GONE);
            vMessage.setText(textProgress);
            ToolsView.setTextOrGone(vAction, textProgressAction);
            vAction.setOnClickListener(v -> {
                if (onProgressAction != null) onProgressAction.callback();
            });
        }

        if (state == State.NONE) {

            ToolsView.toAlpha(vMessage);
            ToolsView.toAlpha(vAction);
        }

    }


}
