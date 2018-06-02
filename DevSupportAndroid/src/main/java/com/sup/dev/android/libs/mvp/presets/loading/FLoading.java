package com.sup.dev.android.libs.mvp.presets.loading;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.mvp.fragments.MvpFragment;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.widgets.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.libs.debug.Debug;

public abstract class FLoading<K extends PLoading> extends MvpFragment<K> {

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

    public FLoading(Context context, K presenter) {
        super(context, presenter, R.layout.fragment_loading);

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

    protected void setContent(@LayoutRes int res) {
        while (vContainer.getChildCount() != 1) vContainer.removeViewAt(0);
        vContainer.addView(ToolsView.inflate(getContext(), res), 0);
    }

    protected ViewIcon addToolbarIcon(@DrawableRes int res, Callback onClick) {
        ViewIcon viewIcon = ToolsView.inflate(getContext(), R.layout.w_icon_toolbar);
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

    public String getTextRetry() {
        return textRetry == null? ToolsResources.getString(getTextRetryR()) : textRetry;
    }

    public String getTextErrorNetwork() {
        return textErrorNetwork == null? ToolsResources.getString(getTextErrorNetworkR()) : textErrorNetwork;
    }

    public String getTextEmpty() {
        return textEmpty == null? ToolsResources.getString(getTextEmptyR()) : textEmpty;
    }

    public int getTextRetryR() {
        return ToolsResources.getStringId("app_retry");
    }

    public int getTextErrorNetworkR() {
        return ToolsResources.getStringId("error_network");
    }

    public int getTextEmptyR() {
        return 0;
    }

    public void setBackgroundImage(@DrawableRes int res){
        vEmptyImage.setImageResource(res);
    }



    //
    //  Presenter
    //

    public void setTitle(@StringRes int title){
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
            vMessage.setText(getTextErrorNetwork());
            vAction.setText(getTextRetry());
            vAction.setOnClickListener(v -> presenter.onReloadClicked());
        }

        if (state == State.EMPTY)
            vMessage.setText(getTextEmpty());

    }

}
