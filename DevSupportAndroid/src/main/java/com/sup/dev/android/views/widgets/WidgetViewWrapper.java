package com.sup.dev.android.views.widgets;

public interface WidgetViewWrapper {

    <K extends WidgetViewWrapper>K hideWidget();

    <K extends WidgetViewWrapper>K setWidgetCancelable(boolean cancelable);

    <K extends WidgetViewWrapper>K setWidgetEnabled(boolean enabled);

}
