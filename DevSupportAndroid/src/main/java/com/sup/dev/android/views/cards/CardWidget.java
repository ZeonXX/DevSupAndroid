package com.sup.dev.android.views.cards;

import android.content.Context;
import android.view.View;

import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetViewWrapper;

public class CardWidget extends Card implements WidgetViewWrapper{

    private final Widget widget;

    public CardWidget(Widget widget){
        this.widget=  widget;
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public View instanceView(Context context) {
        return widget.getView();
    }

    @Override
    public void bindView(View view) {
        widget.onShow();
    }

    @Override
    public <K extends WidgetViewWrapper> K hideWidget() {
        adapter.remove(this);
        return (K) this;
    }

    @Override
    public <K extends WidgetViewWrapper> K setWidgetCancelable(boolean cancelable) {
        return (K) this;
    }

    @Override
    public <K extends WidgetViewWrapper> K setWidgetEnabled(boolean enabled) {
        return (K) this;
    }
}
