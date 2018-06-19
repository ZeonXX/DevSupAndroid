package com.sup.dev.android.views.bricks;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.dialogs.DialogBrick;
import com.sup.dev.android.views.popup.PopupBrick;
import com.sup.dev.android.views.sheets.SheetBrick;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public abstract class Brick {

    public enum Mode {DIALOG, CARD, SHEET, POPUP, FRAGMENT}

    private Callback1<Brick> onHide;
    private boolean enabled = true;
    private boolean canSheetCollapse;
    private boolean canDialogCancel = true;
    private boolean cancelable = true;

    private BrickViewWrapper viewWrapper;

    public View instanceView(Context viewContext, Mode mode) {
        int layoutRes = getLayoutRes(mode);
        return layoutRes != 0 ? ToolsView.inflate(viewContext, layoutRes) : null;
    }

    public void update() {
        if (viewWrapper != null) viewWrapper.update();
    }

    public abstract void bindView(View view, Mode mode);

    public void hide() {
        if (viewWrapper != null) viewWrapper.hide();
    }

    //
    //  Callbacks
    //

    @CallSuper
    public void onShow(View view) {

    }

    @CallSuper
    public void onHide() {
        if (onHide != null) onHide.callback(this);
    }

    //
    //  Setters
    //

    public <K extends Brick> K setCanSheetCollapse(boolean canSheetCollapse) {
        this.canSheetCollapse = canSheetCollapse;
        return (K) this;
    }

    public <K extends Brick> K setOnHide(Callback1<Brick> onHide) {
        this.onHide = onHide;
        return (K) this;
    }

    public <K extends Brick> K setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return (K) this;
    }

    public <K extends Brick> K setCanDialogCancel(boolean canDialogCancel) {
        this.canDialogCancel = canDialogCancel;
        return (K) this;
    }

    public <K extends Brick> K setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return (K) this;
    }

    //
    //  Getters
    //

    @LayoutRes
    public abstract int getLayoutRes(Mode mode);

    public boolean isSheetCanCollapse() {
        return canSheetCollapse;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isCanDialogCancel() {
        return canDialogCancel;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    //
    //  Support
    //

    public SheetBrick asSheet() {
        SheetBrick sheetBrick = new SheetBrick(this);
        viewWrapper = sheetBrick;
        return sheetBrick;
    }

    public DialogBrick asDialog() {
        DialogBrick dialogBrick = new DialogBrick(this);
        viewWrapper = dialogBrick;
        return dialogBrick;
    }

    public DialogBrick asDialogShow() {
        DialogBrick dialogBrick = asDialog();
        dialogBrick.show();
        return dialogBrick;
    }

    public PopupBrick asPopup() {
        PopupBrick popupBrick = new PopupBrick(this);
        viewWrapper = popupBrick;
        return popupBrick;
    }

    public PopupBrick asPopupShow(View view) {
        PopupBrick popupBrick = asPopup();
        popupBrick.show(view);
        return popupBrick;
    }

    public PopupBrick asPopupShow(View view, int x, int y) {
        PopupBrick popupBrick = asPopup();
        popupBrick.show(view, x, y);
        return popupBrick;
    }
}
