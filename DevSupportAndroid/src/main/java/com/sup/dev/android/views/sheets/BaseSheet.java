package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.providers.Provider;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

public abstract class BaseSheet extends FrameLayout {

    protected final Context viewContext;
    protected final View view;
    private final int fabId;
    private final int dimId;
    private final boolean openOnFab;
    private FloatingActionButton vFab;
    private View vDim;

    private BottomSheetBehavior behavior;

    private final Provider<Boolean> onBackPressed = () -> {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hide();
            return true;
        }
        return false;
    };

    public BaseSheet(Context viewContext, AttributeSet attrs, @LayoutRes int res) {
        super(viewContext, attrs);
        SupAndroid.initEditMode(this);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BaseSheet, 0, 0);
        fabId = a.getResourceId(R.styleable.BaseSheet_Sheet_fabId, 0);
        dimId = a.getResourceId(R.styleable.BaseSheet_Sheet_dimId, 0);
        openOnFab = a.getBoolean(R.styleable.BaseSheet_Sheet_openOnFab, false);
        a.recycle();

        this.viewContext = viewContext;
        this.view = ToolsView.inflate(viewContext, res);

        view.setOnClickListener(v -> {
            //  Чтоб не тригерился vDim, когда нажимаешь на тело диалога
        });

        addView(view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (behavior != null) return;

        this.behavior = BottomSheetBehavior.from(this);

        if (fabId != 0) {
            vFab = ToolsView.findViewOnParents(BaseSheet.this, fabId);
            if (openOnFab) vFab.setOnClickListener(v -> show());
        }
        if (dimId != 0) {
            vDim = ToolsView.findViewOnParents(BaseSheet.this, dimId);
            vDim.setAlpha(0);
            vDim.setOnClickListener(v -> hide());
            vDim.setClickable(false);
            vDim.setFocusable(false);
        }

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                BaseSheet.this.onStateChanged(newState);
                if (newState == BottomSheetBehavior.STATE_DRAGGING) onDragged();
                if (newState == BottomSheetBehavior.STATE_EXPANDED) onExpanded();
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) onCollapsed();
                if (newState == BottomSheetBehavior.STATE_HIDDEN) onHidden();
                if (newState == BottomSheetBehavior.STATE_SETTLING) onSettling();
            }

            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (vFab != null) vFab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                if (vDim != null) vDim.setAlpha(slideOffset / 1.5f);
            }


        });
    }

    public <K extends BaseSheet> K show() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        setEnabled(true);
        return (K) this;
    }

    public void hide() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @CallSuper
    protected void onDragged() {
        if (vFab != null) vFab.setVisibility(VISIBLE);
        if (vDim != null) vDim.setClickable(false);
    }

    @CallSuper
    protected void onExpanded() {
        SupAndroid.addOnBack(onBackPressed);
        if (vFab != null) vFab.setVisibility(INVISIBLE);
        if (vDim != null) vDim.setClickable(true);
    }

    @CallSuper
    protected void onCollapsed() {
        SupAndroid.removeOnBack(onBackPressed);
        if (vFab != null) vFab.setVisibility(VISIBLE);
        if (vFab != null) vFab.animate().scaleX(1).scaleY(1).setDuration(0).start();
        if (vDim != null) vDim.setClickable(false);
    }

    @CallSuper
    protected void onHidden() {
        if (vFab != null) vFab.setVisibility(VISIBLE);
        if (vDim != null) vDim.setClickable(false);
    }

    @CallSuper
    protected void onSettling() {
        if (vFab != null) vFab.setVisibility(VISIBLE);
        if (vDim != null) vDim.setClickable(false);
    }

    @CallSuper
    protected void onStateChanged(int newState) {

    }

    //
    //  Setters
    //

    public <K extends BaseSheet> K setCancelable(boolean cancelable) {
        return (K) this;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    //
    //  Save state
    //

    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState());
        bundle.putBoolean("enabled", isEnabled());
        bundle.putBoolean("shoved", behavior.getState() == BottomSheetBehavior.STATE_EXPANDED);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            if (bundle.getBoolean("shoved"))
                ToolsThreads.main(true, () -> {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    setEnabled(bundle.getBoolean("enabled"));
                });

            state = bundle.getParcelable("SUPER_STATE");
        }
        super.onRestoreInstanceState(state);
    }


}
