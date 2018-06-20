package com.sup.dev.android.views.sheets;

import android.support.annotation.CallSuper;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;

import java.lang.ref.WeakReference;

public abstract class Sheet {

    private final View view;
    private WeakReference<ViewSheet> vSheetRef;
    private boolean isEnabled = true;
    private boolean canCollapse = false;
    private Callback onCollapsed;

    public Sheet(int layoutRes) {
        this(ToolsView.inflate(layoutRes));
    }

    public Sheet(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    protected void onShow(){

    }

    @CallSuper
    protected void onDragged(ViewSheet view) {
    }

    @CallSuper
    protected void onExpanded(ViewSheet view) {
    }

    @CallSuper
    protected void onCollapsed() {
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

    public <K extends Sheet> K setCancelable(boolean canCollapse) {
        this.canCollapse = canCollapse;
        return (K) this;
    }

    @CallSuper
    public  <K extends Sheet> K setEnabled(boolean b){
        this.isEnabled = b;
        return (K) this;
    }

    //
    //  Getters
    //

    public boolean isCanCollapse() {
        return canCollapse;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    //
    //  State - for background control of Sheet
    //

    enum State {HIDE, SHOW, NONE}

    private State state = State.NONE;

    public <K extends Sheet> K hide() {
        setState(State.HIDE);
        return (K) this;
    }

    public <K extends Sheet> K show() {
        setState(State.SHOW);
        return (K) this;
    }

    void setState(State state) {
        this.state = state;
        if(vSheetRef != null && vSheetRef.get() != null) {
            ViewSheet viewSheet = vSheetRef.get();
            if(state == State.SHOW)viewSheet.show();
            if(state == State.HIDE)viewSheet.hide();
        }

    }

    public State getState() {
        return state;
    }

}
