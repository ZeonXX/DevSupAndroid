package com.sup.dev.android.views.sheets;

import android.view.View;

import com.sup.dev.android.views.bricks.Brick;

public class SheetBrick extends Sheet{

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

    @Override
    public SheetBrick setCanCollapse(boolean canCollapse) {
        brick.setCanSheetCollapse(canCollapse);
        return this;
    }

    @Override
    public SheetBrick setEnabled(boolean b) {
        brick.setEnabled(b);
        return this;
    }

    //
    //  Getters
    //

    @Override
    public boolean isCanCollapse() {
        return brick.isSheetCanCollapse();
    }

    @Override
    public boolean isEnabled() {
        return brick.isEnabled();
    }
}
