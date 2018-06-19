package com.sup.dev.android.views.popup_x;

import android.view.View;

import com.sup.dev.android.views.bricks.Brick;

public class PopupBrick extends Popup {

    private final Brick brick;

    public PopupBrick(Brick brick){
        this.brick = brick;
    }


    @Override
    protected void onShow(View view) {
        super.onShow(view);
        brick.onShow(view);
    }

    @Override
    protected void onHide() {
        super.onHide();
        brick.onHide();
    }

    @Override
    public int getLayoutRes() {
        return brick.getLayoutRes();
    }

    @Override
    public void bindView(View view) {
        brick.bindView(view);
    }

    //
    //  Setters
    //


    public PopupBrick setCancelable(boolean cancelable) {
        brick.setCanDialogCancel(cancelable);
        return this;
    }

    public PopupBrick setEnabled(boolean enabled) {
        brick.setEnabled(enabled);
        return this;
    }


    //
    //  Getters
    //

    @Override
    public boolean isCancelable() {
        return brick.isCanDialogCancel();
    }

    @Override
    public boolean isEnabled() {
        return brick.isEnabled();
    }

}
