package com.sup.dev.android.views.dialogs;

import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetViewWrapper;

public class DialogSheetWidget extends DialogSheet implements WidgetViewWrapper {

    private final Widget widget;

    public DialogSheetWidget(Widget widget) {
        super(widget.getView());
        this.widget = widget;
        setCancelable(widget.isCancelable());
        setEnabled(widget.isEnabled());
    }

    @Override
    public boolean onTryCancelOnTouchOutside(){
        return widget.onTryCancelOnTouchOutside();
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

    public DialogSheetWidget setDialogCancelable(boolean cancelable) {
        return super.setDialogCancelable(cancelable);
    }

    public DialogSheetWidget setEnabled(boolean enabled) {
        return super.setEnabled(enabled);
    }

    @Override
    public DialogSheetWidget hideWidget() {
        hide();
        return this;
    }

    @Override
    public DialogSheetWidget setWidgetCancelable(boolean cancelable) {
        return setDialogCancelable(cancelable);
    }

    @Override
    public DialogSheetWidget setWidgetEnabled(boolean enabled) {
        return setEnabled(enabled);
    }
}
