package com.sup.dev.android.views.dialogs;

import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetViewWrapper;

public class DialogWidget extends Dialog implements WidgetViewWrapper {

    private final Widget widget;

    public DialogWidget(Widget widget){
        super(widget.getView());
        this.widget = widget;
        setCancelable(widget.isCanDialogCancel());
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

    //
    //  Setters
    //

    public DialogWidget setDialogCancelable(boolean cancelable) {
        return super.setDialogCancelable(cancelable);
    }

    public DialogWidget setEnabled(boolean enabled) {
        return super.setEnabled(enabled);
    }

    @Override
    public DialogWidget hideWidget() {
        hide();
        return this;
    }

    @Override
    public DialogWidget setWidgetCancelable(boolean cancelable) {
        return setDialogCancelable(cancelable);
    }

    @Override
    public DialogWidget setWidgetEnabled(boolean enabled) {
        return setEnabled(enabled);
    }
}
