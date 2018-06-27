package com.sup.dev.android.views.popup;

import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetViewWrapper;

public class PopupWidget extends Popup implements WidgetViewWrapper {

    private final Widget widget;

    public PopupWidget(Widget widget){
        super(widget.getView());
        this.widget = widget;
        setCancelable(widget.isCancelable());
        setEnabled(widget.isEnabled());
    }

    @Override
    protected void onShow() {
        super.onShow();
        widget.onShow();
    }

    @Override
    protected void onHide() {
        super.onHide();
        widget.onHide();
    }

    @Override
    public PopupWidget hide() {
        return super.hide();
    }

    //
    //  Setters
    //


    public PopupWidget setCancelable(boolean cancelable) {
        return super.setCancelable(cancelable);
    }

    public PopupWidget setEnabled(boolean enabled) {
        return super.setEnabled(enabled);
    }

    @Override
    public PopupWidget hideWidget() {
        return hide();
    }

    @Override
    public PopupWidget setWidgetCancelable(boolean cancelable) {
        return setCancelable(cancelable);
    }

    @Override
    public PopupWidget setWidgetEnabled(boolean enabled) {
        return setEnabled(enabled);
    }
}
