package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.view.View;

import com.sup.dev.android.views.bricks.Brick;
import com.sup.dev.android.views.bricks.BrickViewWrapper;

public class SheetBrick extends Sheet implements BrickViewWrapper{

    private final Brick brick;

    public SheetBrick(Brick brick){
        this.brick = brick;
    }

    @Override
    protected void onExpanded(ViewSheet view) {
        super.onExpanded(view);
        brick.onShow(view);
    }

    @Override
    protected void onCollapsed() {
        super.onCollapsed();
        brick.onHide();
    }

    @Override
    public View instanceView(Context viewContext) {
        return brick.instanceView(viewContext, Brick.Mode.SHEET);
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public void bindView(View view) {
        brick.bindView(view, Brick.Mode.SHEET);
    }

    @Override
    public SheetBrick update() {
        super.update();
        return this;
    }

    @Override
    public SheetBrick hide() {
        super.hide();
        return this;
    }

    //
    //  Setters
    //

    @Override
    public SheetBrick setCancelable(boolean canCollapse) {
        super.setCancelable(canCollapse);
        return this;
    }

    @Override
    public SheetBrick setEnabled(boolean b) {
        super.setEnabled(b);
        return this;
    }



}
