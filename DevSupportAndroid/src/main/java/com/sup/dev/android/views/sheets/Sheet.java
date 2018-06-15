package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;

import java.lang.ref.WeakReference;

public abstract class Sheet {

    private final int layoutId;

    protected WeakReference<ViewSheet> vSheetRef;
    protected boolean isEnabled;
    private Callback onCollapsed;

    public Sheet(@LayoutRes int layoutId){
        this.layoutId = layoutId;
    }

    public View instanceView(Context viewContext) {
        if (layoutId != 0) {
            return ToolsView.inflate(viewContext, layoutId);
        }
        return null;
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

    //
    //  Setters
    //

    public <K extends Sheet> K setOnCollapsed(Callback onCollapsed) {
        this.onCollapsed = onCollapsed;
        return (K) this;
    }

    @CallSuper
    public  <K extends Sheet> K setEnabled(boolean b){
        this.isEnabled = b;
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
