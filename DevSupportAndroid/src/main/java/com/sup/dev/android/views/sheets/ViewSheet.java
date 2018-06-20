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
import com.sup.dev.android.libs.screens.SNavigator;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.behavior.BehaviorBottomSheet;
import com.sup.dev.java.classes.providers.Provider;
import com.sup.dev.java.tools.ToolsThreads;

public class ViewSheet extends FrameLayout {

    private final int fabId;
    private final int dimId;
    private final boolean openOnFab;

    private Sheet sheet;
    private Sheet nextSheet;
    private FloatingActionButton vFab;
    private View vDim;
    private BehaviorBottomSheet behavior;
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

        this.nextSheet = sheet;

        if (behavior != null && behavior.getState() != BottomSheetBehavior.STATE_HIDDEN && behavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            rebindViewInProgress = true;
            hide();
        } else {
            setSheetNow();
        }

    }

    private void setSheetNow() {
        rebindViewInProgress = false;
        if (nextSheet == null) return;
        if (sheet != null) sheet.onDetach(this);
        sheet = nextSheet;
        removeAllViews();
        if (sheet != null) {
            ToolsView.removeFromParent(sheet.getView());
            addView(sheet.getView());
            sheet.onAttach(this);
            rebindView();
            if (behavior != null) behavior.setCanColapse(sheet.isCanCollapse());
        }
    }

    public void rebindView() {
        if (sheet.getState() == Sheet.State.HIDE) hide();
        if (sheet.getState() == Sheet.State.SHOW) show();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (behavior != null) return;

        behavior = (BehaviorBottomSheet) BottomSheetBehavior.from(this);
        if (sheet != null) behavior.setCanColapse(sheet.isCanCollapse());

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
                if (vDim != null) vDim.setAlpha(slideOffset / 1.5f);
            }


        });
    }

    public <K extends ViewSheet> K show() {
        if (sheet.getView() != null) {
            sheet.onShow();
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
        if (vDim != null) vDim.setClickable(false);
        if (sheet != null) sheet.onDragged(this);
    }

    @CallSuper
    protected void onExpanded() {
        SNavigator.addOnBack(onBackPressed);
        if (vDim != null) vDim.setClickable(true);
        if (sheet != null) sheet.onExpanded(this);
    }

    @CallSuper
    protected void onCollapsed() {
        SNavigator.removeOnBack(onBackPressed);
        if (vDim != null) vDim.setClickable(false);
        if (sheet != null) sheet.onCollapsed();

        if (rebindViewInProgress) {
            setSheetNow();
            show();
        }
    }

    @CallSuper
    protected void onHidden() {
        if (vDim != null) vDim.setClickable(false);
        if (sheet != null) sheet.onHidden(this);
    }

    @CallSuper
    protected void onSettling() {
        if (vDim != null) vDim.setClickable(false);
        if (sheet != null) sheet.onSettling(this);
    }

    @CallSuper
    protected void onStateChanged(int newState) {
        if (sheet != null) sheet.onStateChanged(this, newState);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getSize(heightMeasureSpec) <= ToolsView.dpToPx(400/*Запас, чтоб не обрезать 2 пикселя*/))
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(ToolsView.dpToPx(364), MeasureSpec.getMode(heightMeasureSpec)));
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
        bundle.putBoolean("shoved", behavior != null && behavior.getState() == BottomSheetBehavior.STATE_EXPANDED);

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
