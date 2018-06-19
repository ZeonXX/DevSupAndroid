package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.view.View;

import com.sup.dev.android.views.bricks.Brick;
import com.sup.dev.android.views.bricks.BrickViewWrapper;

public class DialogBrick extends Dialog implements BrickViewWrapper{

    private final Brick brick;

    public DialogBrick(Brick brick){
        this.brick = brick;
        setCancelable(brick.isCanDialogCancel());
        setEnabled(brick.isEnabled());
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
        return brick.instanceView(viewContext, Brick.Mode.DIALOG);
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public void bindView(View view) {
        brick.bindView(view, Brick.Mode.DIALOG);
    }

    @Override
    public DialogBrick hide() {
        return super.hide();
    }

    @Override
    public DialogBrick update() {
       return super.update();
    }

    //
    //  Setters
    //


    public DialogBrick setCancelable(boolean cancelable) {
        return super.setCancelable(cancelable);
    }

    public DialogBrick setEnabled(boolean enabled) {
        return super.setEnabled(enabled);
    }

}
