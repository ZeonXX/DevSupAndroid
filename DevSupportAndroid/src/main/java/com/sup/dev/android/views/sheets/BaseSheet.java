package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;

import java.lang.ref.WeakReference;

public abstract class BaseSheet {

    protected WeakReference<ViewSheet> vSheetRef;

    public View instanceView(Context viewContext) {
        if (getLayoutId() != 0) {
            return ToolsView.inflate(viewContext, getLayoutId());
        }
        return null;
    }

    public int getLayoutId() {
        return 0;
    }

    public abstract void bindView(View view);

    public void setVSheet(ViewSheet vSheet) {
        vSheetRef = new WeakReference<>(vSheet);
    }

    public void update() {
        if (vSheetRef != null && vSheetRef.get() != null)
            vSheetRef.get().rebindView();
    }

    @CallSuper
    protected void onDragged(View view) {
    }

    @CallSuper
    protected void onExpanded(View view) {
    }

    @CallSuper
    protected void onCollapsed(View view) {
    }

    @CallSuper
    protected void onHidden(View view) {
    }

    @CallSuper
    protected void onSettling(View view) {
    }

    @CallSuper
    protected void onStateChanged(View view, int newState) {

    }


    //
    //  State - for background control of Sheet
    //

    enum State {HIDE, SHOW, NONE}

    private State state = State.NONE;

    public void hide() {
        setState(State.HIDE);
    }

    public void show() {
        setState(State.SHOW);
    }

    void setState(State state) {
        this.state = state;
        update();
    }

    public State getState() {
        return state;
    }

}
