package com.sup.dev.android.views.bricks;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.dialogs_adwanced.DialogBrick;
import com.sup.dev.android.views.sheets.SheetBrick;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public abstract class Brick {

    private Callback1<Brick> onHide;
    private boolean enabled = true;
    private boolean canSheetCollapse;
    private boolean canDialogCancel;

    public View instanceView(Context viewContext) {
        return getLayoutRes() != 0 ? ToolsView.inflate(viewContext, getLayoutRes()) : null;
    }

    public void update() {

    }

    public abstract void bindView(View view);

    public void hide() {

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

    //
    //  Getters
    //

    @LayoutRes
    public abstract int getLayoutRes();

    public boolean isSheetCanCollapse() {
        return canSheetCollapse;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isCanDialogCancel() {
        return canDialogCancel;
    }

    //
    //  Support
    //

    public SheetBrick asSheet() {
        return new SheetBrick(this);
    }

    public DialogBrick asDialog() {
        return new DialogBrick(this);
    }

    public DialogBrick asDialogShow() {
        return new DialogBrick(this).show();
    }
}
