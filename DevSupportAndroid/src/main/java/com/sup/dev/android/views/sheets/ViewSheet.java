package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.providers.Provider;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

public class ViewSheet extends FrameLayout {

    private final int fabId;
    private final int dimId;
    private final boolean openOnFab;

    private Sheet sheet;
    private View view;
    private FloatingActionButton vFab;
    private View vDim;
    private BottomSheetBehavior behavior;
    private boolean rebindViewInProgress;

    private final Provider<Boolean> onBackPressed = () -> {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hide();
            return true;
        }
        return false;
    };

    public ViewSheet(Context viewContext, AttributeSet attrs) {
        super(viewContext, attrs);
        SupAndroid.initEditMode(this);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Sheet, 0, 0);
        fabId = a.getResourceId(R.styleable.Sheet_Sheet_fabId, 0);
        dimId = a.getResourceId(R.styleable.Sheet_Sheet_dimId, 0);
        openOnFab = a.getBoolean(R.styleable.Sheet_Sheet_openOnFab, false);
        a.recycle();

        setClickable(true); //  Чтоб не тригерился vDim, когда нажимаешь на тело диалога
        setBackgroundColor(ToolsResources.getBackgroundColor(viewContext));
    }

    public void setSheet(Sheet sheet) {

        View oldView = view;
        this.sheet = sheet;
        this.view = null;

        if (sheet != null) {
            sheet.setVSheet(this);
            view = sheet.instanceView(getContext());
        }

        if (oldView != null && behavior.getState() != BottomSheetBehavior.STATE_HIDDEN && behavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            rebindViewInProgress = true;
            hide();
        } else {
            removeAllViews();
            addView(view);
            rebindView();
        }

    }

    public void rebindView() {
        if (view != null) sheet.bindView(view);
        if (sheet.getState() == Sheet.State.HIDE) hide();
        if (sheet.getState() == Sheet.State.SHOW) show();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (behavior != null) return;

        this.behavior = BottomSheetBehavior.from(this);

        if (fabId != 0) {
            vFab = ToolsView.findViewOnParents(this, fabId);
            if (vFab != null) {
                if (openOnFab) vFab.setOnClickListener(v -> show());
            }
        }
        if (dimId != 0) {
            vDim = ToolsView.findViewOnParents(this, dimId);
            if (vDim != null) {
                vDim.setAlpha(0);
                vDim.setOnClickListener(v -> hide());
                vDim.setClickable(false);
                vDim.setFocusable(false);
            }
        }

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                ViewSheet.this.onStateChanged(newState);
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

    public <K extends ViewSheet> K show() {
        if (view != null) {
            if (behavior != null) behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            setEnabled(true);
        }
        return (K) this;
    }

    public void hide() {
        if (behavior != null) behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @CallSuper
    protected void onDragged() {
        if (vFab != null) vFab.setVisibility(VISIBLE);
        if (vDim != null) vDim.setClickable(false);
        if (sheet != null) sheet.onDragged(view);
    }

    @CallSuper
    protected void onExpanded() {
        SupAndroid.addOnBack(onBackPressed);
        if (vFab != null) vFab.setVisibility(INVISIBLE);
        if (vDim != null) vDim.setClickable(true);
        if (sheet != null) sheet.onExpanded(view);
    }

    @CallSuper
    protected void onCollapsed() {
        SupAndroid.removeOnBack(onBackPressed);
        if (vFab != null) vFab.setVisibility(VISIBLE);
        if (vFab != null) vFab.animate().scaleX(1).scaleY(1).setDuration(0).start();
        if (vDim != null) vDim.setClickable(false);
        if (sheet != null) sheet.onCollapsed(view);

        if (rebindViewInProgress) {
            rebindViewInProgress = false;
            removeAllViews();
            if (view != null) {
                addView(view);
                rebindView();
                show();
            }
        }
    }

    @CallSuper
    protected void onHidden() {
        if (vFab != null) vFab.setVisibility(VISIBLE);
        if (vDim != null) vDim.setClickable(false);
        if (sheet != null) sheet.onHidden(view);
    }

    @CallSuper
    protected void onSettling() {
        if (vFab != null) vFab.setVisibility(VISIBLE);
        if (vDim != null) vDim.setClickable(false);
        if (sheet != null) sheet.onSettling(view);
    }

    @CallSuper
    protected void onStateChanged(int newState) {
        if (sheet != null) sheet.onStateChanged(view, newState);
    }

    //
    //  Setters
    //

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
