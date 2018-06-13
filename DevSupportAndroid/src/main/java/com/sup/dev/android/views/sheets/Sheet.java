package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;

import java.lang.ref.WeakReference;

public abstract class Sheet {

    protected WeakReference<ViewSheet> vSheetRef;
    private Callback onCollapsed;

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

    public void update() {
        if (vSheetRef != null && vSheetRef.get() != null)
            vSheetRef.get().rebindView();
    }

    @CallSuper
    protected void onDragged(ViewSheet view) {
    }

    @CallSuper
    protected void onExpanded(ViewSheet view) {
    }

    @CallSuper
    protected void onCollapsed(ViewSheet view) {
        if (onCollapsed != null) onCollapsed.callback();
    }

    @CallSuper
    protected void onHidden(ViewSheet view) {
    }

    @CallSuper
    protected void onSettling(ViewSheet view) {
    }

    @CallSuper
    protected void onStateChanged(ViewSheet view, int newState) {

    }

    @CallSuper
    protected void onAttach(ViewSheet vSheet) {
        vSheetRef = new WeakReference<>(vSheet);
    }

    @CallSuper
    protected void onDetach(ViewSheet vSheet) {
       if(vSheetRef != null) {
           vSheetRef.clear();
           vSheetRef = null;
       }
    }


    public <K extends Sheet> K setOnCollapsed(Callback onCollapsed) {
        this.onCollapsed = onCollapsed;
        return (K) this;
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
