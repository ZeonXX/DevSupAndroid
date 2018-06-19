package com.sup.dev.android.views.popup;

import android.content.Context;
import android.view.View;

import com.sup.dev.android.views.bricks.Brick;
import com.sup.dev.android.views.bricks.BrickViewWrapper;

public class PopupBrick extends Popup implements BrickViewWrapper{

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
    public View instanceView(Context viewContext) {
        return brick.instanceView(viewContext, Brick.Mode.POPUP);
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public void bindView(View view) {
        brick.bindView(view, Brick.Mode.POPUP);
    }

    @Override
    public PopupBrick update() {
        return super.update();
    }

    @Override
    public PopupBrick hide() {
        return super.hide();
    }

    //
    //  Setters
    //


    public PopupBrick setCancelable(boolean cancelable) {
        return super.setCancelable(cancelable);
    }

    public PopupBrick setEnabled(boolean enabled) {
        return super.setEnabled(enabled);
    }



}
