package com.sup.dev.android.views.cards;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.tools.ToolsThreads;

public class CardLoading extends Card {

    private boolean dividerVisible = false;
    private boolean enabled = true;
    private int background = 0;

    public enum State {LOADING, ACTION, RETRY}

    public enum Type {CIRCLE, HORIZONTAL, NONE}

    private String actionMessage;
    private String actionButton;
    private Callback1<CardLoading> onAction;
    private String retryMessage;
    private String retryButton;
    private Callback1<CardLoading> onRetry;
    private State state = State.LOADING;
    private Type type = Type.CIRCLE;

    @Override
    public int getLayout() {
        return R.layout.card_loading;
    }

    @Override
    public void bindView(View view) {
        View vDivider = view.findViewById(R.id.divider);
        View vLoadingCircle = view.findViewById(R.id.loading);
        View vLoadingHorizontal = view.findViewById(R.id.loading_horizontal);
        View vContainer = view.findViewById(R.id.container);
        Button vAction = view.findViewById(R.id.action);
        TextView vText = view.findViewById(R.id.text);

        vDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        if (background != 0) view.setBackgroundColor(background);
        vAction.setEnabled(isEnabled());
        vText.setEnabled(isEnabled());

        if (state == State.LOADING) {
            vContainer.setVisibility(View.GONE);

            if(type == Type.CIRCLE) ToolsThreads.main(1000, () -> {if(state == State.LOADING)ToolsView.alpha(vLoadingCircle, type != Type.CIRCLE);});
            else vLoadingCircle.setVisibility(View.GONE);
            if(type == Type.HORIZONTAL)ToolsThreads.main(1000, () ->{if(state == State.LOADING)ToolsView.alpha(vLoadingHorizontal, type != Type.HORIZONTAL);});
            else vLoadingHorizontal.setVisibility(View.GONE);

            vText.setVisibility(View.GONE);
            vAction.setVisibility(View.GONE);
            vText.setText("");
            vAction.setText("");
        }
        if (state == State.RETRY) {
            vContainer.setVisibility(View.VISIBLE);
            ToolsView.toAlpha(vLoadingCircle);
            ToolsView.toAlpha(vLoadingHorizontal);
            vText.setVisibility(View.VISIBLE);
            vAction.setVisibility(retryButton == null || retryButton.isEmpty() ? View.GONE : View.VISIBLE);
            vText.setText(retryMessage);
            vAction.setText(retryButton);
            vAction.setOnClickListener(v -> {
                setState(State.LOADING);
                if (onRetry != null) onRetry.callback(this);
            });
        }
        if (state == State.ACTION) {
            vContainer.setVisibility(View.VISIBLE);
            ToolsView.toAlpha(vLoadingCircle);
            ToolsView.toAlpha(vLoadingHorizontal);
            vText.setVisibility(View.VISIBLE);
            vAction.setVisibility(actionButton == null || actionButton.isEmpty() ? View.GONE : View.VISIBLE);
            vText.setText(actionMessage);
            vAction.setText(actionButton);
            vAction.setOnClickListener(v -> {
                if (onAction != null) onAction.callback(this);
            });
        }
    }

    //
    //  Setters
    //


    public CardLoading setDividerVisible(boolean dividerVisible) {
        this.dividerVisible = dividerVisible;
        update();
        return this;
    }

    public CardLoading setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return this;
    }

    public CardLoading setBackground(int background) {
        this.background = background;
        update();
        return this;
    }

    public CardLoading setState(State state) {
        this.state = state;
        update();
        return this;
    }

    public CardLoading setActionMessage(@StringRes int text) {
        return setActionMessage(ToolsResources.getString(text));
    }

    public CardLoading setActionMessage(String text) {
        this.actionMessage = text;
        update();
        return this;
    }

    public CardLoading setActionButton(@StringRes int text, Callback1<CardLoading> onAction) {
        return setActionButton(ToolsResources.getString(text), onAction);
    }

    public CardLoading setActionButton(String text, Callback1<CardLoading> onAction) {
        actionButton = text;
        this.onAction = onAction;
        update();
        return this;
    }

    public CardLoading setRetryMessage(@StringRes int text) {
        return setRetryMessage(ToolsResources.getString(text));
    }

    public CardLoading setRetryMessage(String text) {
        this.retryMessage = text;
        update();
        return this;
    }

    public CardLoading setRetryButton(@StringRes int text, Callback1<CardLoading> onRetry) {
        return setRetryButton(ToolsResources.getString(text), onRetry);
    }

    public CardLoading setRetryButton(String text, Callback1<CardLoading> onRetry) {
        retryButton = text;
        this.onRetry = onRetry;
        update();
        return this;
    }

    public CardLoading setOnRetry(Callback1<CardLoading> onRetry) {
        this.onRetry = onRetry;
        update();
        return this;
    }

    public CardLoading setType(Type type) {
        this.type = type;
        update();
        return this;
    }

    //
    //  Getters
    //

    public boolean isEnabled() {
        return enabled;
    }
}
