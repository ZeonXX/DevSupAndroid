package com.sup.dev.android.views.sheets;

import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetViewWrapper;
import com.sup.dev.java.libs.debug.Debug;

public class SheetWidget extends Sheet implements WidgetViewWrapper {

    private final Widget widget;

    public SheetWidget(Widget widget){
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
    protected void onCollapsed() {
        super.onCollapsed();
        widget.onHide();
    }

    @Override
    public SheetWidget hide() {
        super.hide();
        widget.onHide();
        return this;
    }

    //
    //  Setters
    //

    @Override
    public SheetWidget setCancelable(boolean canCollapse) {
        super.setCancelable(canCollapse);
        return this;
    }

    @Override
    public SheetWidget setEnabled(boolean b) {
        super.setEnabled(b);
        return this;
    }


    @Override
    public SheetWidget hideWidget() {
        return hide();
    }

    @Override
    public SheetWidget setWidgetCancelable(boolean cancelable) {
        return setCancelable(cancelable);
    }

    @Override
    public SheetWidget setWidgetEnabled(boolean enabled) {
        return setEnabled(enabled);
    }

}
