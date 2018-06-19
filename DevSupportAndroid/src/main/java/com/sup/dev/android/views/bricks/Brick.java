package com.sup.dev.android.views.bricks;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.dialogs_x.DialogBrick;
import com.sup.dev.android.views.popup_x.PopupBrick;
import com.sup.dev.android.views.sheets.SheetBrick;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public abstract class Brick {

    private Callback1<Brick> onHide;
    private boolean enabled = true;
    private boolean canSheetCollapse;
    private boolean canDialogCancel = true;

    private SheetBrick sheetBrick;
    private DialogBrick dialogBrick;

    public View instanceView(Context viewContext) {
        return getLayoutRes() != 0 ? ToolsView.inflate(viewContext, getLayoutRes()) : null;
    }

    public void update() {
        if(sheetBrick != null)sheetBrick.update();
       // if(dialogBrick != null) dialogBrick.update();
    }

    public abstract void bindView(View view);

    public void hide() {
        if(sheetBrick != null)sheetBrick.hide();
        if(dialogBrick != null) dialogBrick.hide();
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
        if(sheetBrick == null) sheetBrick = new SheetBrick(this);
        return sheetBrick;
    }

    public DialogBrick asDialog() {
        if(dialogBrick == null) dialogBrick =  new DialogBrick(this);
        return dialogBrick;
    }

    public DialogBrick asDialogShow() {
        DialogBrick dialogBrick = asDialog();
        dialogBrick.show();
        return dialogBrick;
    }

    public PopupBrick asPopup(){
       return new PopupBrick(this);
    }

    public PopupBrick asPopupShow(View view){
        PopupBrick popupBrick = asPopup();
        popupBrick.show(view);
        return popupBrick;
    }

    public PopupBrick asPopupShow(View view, int x, int y){
        PopupBrick popupBrick = asPopup();
        popupBrick.show(view, x, y);
        return popupBrick;
    }
}
