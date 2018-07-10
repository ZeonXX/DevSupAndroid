package com.sup.dev.android.views.screens;

import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.sup.dev.android.R;
import com.sup.dev.android.libs.screens.Navigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetViewWrapper;

public class SWidget extends Screen implements WidgetViewWrapper {

    private final Widget widget;
    private final ViewGroup vContainer;

    public SWidget(Widget widget) {
        super(R.layout.screen_widget);
        this.widget = widget;

        vContainer = findViewById(R.id.container);
        vContainer.addView(ToolsView.removeFromParent(widget.getView()));
    }

    public void setTitle(String title){
        ((Toolbar)findViewById(R.id.toolbar)).setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        widget.onShow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widget.onHide();
    }

    @Override
    public <K extends WidgetViewWrapper> K hideWidget() {
        Navigator.remove(this);
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
