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

    protected String textErrorNetwork;
    protected String textRetry;
    protected String textEmpty;

    public SLoading(@LayoutRes int layoutRes) {
        super(layoutRes);

        vContainer = findViewById(R.id.container);
        vToolbarContainer = findViewById(R.id.toolbar_icons_container);
        vMessage = findViewById(R.id.message);
        vAction = findViewById(R.id.action);
        vProgress = findViewById(R.id.progress);
        vEmptyImage = findViewById(R.id.empty_image);

        vAction.setVisibility(View.INVISIBLE);
        vMessage.setVisibility(View.INVISIBLE);

        setState(State.PROGRESS);
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
        textEmpty = ToolsResources.getString(t);
    }

    public Object getTextRetry() {
        return SupAndroid.TEXT_APP_RETRY;
    }

    public Object getTextErrorNetwork() {
        return SupAndroid.TEXT_ERROR_NETWORK;
    }

    public Object getTextEmpty() {
        return 0;
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
        ToolsView.alpha(vAction, state != State.ERROR);
        ToolsView.alpha(vProgress, state != State.PROGRESS);
        ToolsView.alpha(vMessage, state == State.PROGRESS || state == State.NONE);
        ToolsView.alpha(vEmptyImage, state == State.NONE);

        if (state == State.ERROR) {

            Object textNetworkError = getTextErrorNetwork();
            if (textNetworkError instanceof Integer) textNetworkError = ToolsResources.getString((int) textNetworkError);

            Object textRetry = getTextRetry();
            if (textRetry instanceof Integer) textRetry = ToolsResources.getString((int) textRetry);


            vMessage.setText(textNetworkError == null ? null : textNetworkError.toString());
            vAction.setText(textRetry == null ? null : textRetry.toString());
            vAction.setOnClickListener(v -> onReloadClicked());
        }

        if (state == State.EMPTY) {

            Object textEmpty = getTextEmpty();
            if (textEmpty instanceof Integer) textEmpty = ToolsResources.getString((int) textEmpty);

            vMessage.setText(textEmpty == null ? null : textEmpty.toString());
        }

    }


}
