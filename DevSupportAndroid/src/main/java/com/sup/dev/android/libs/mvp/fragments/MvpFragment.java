package com.sup.dev.android.libs.mvp.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.magic_box.WorkaroundCollapsingToolbarSkrim;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import java.lang.reflect.Field;

public class MvpFragment<K extends MvpPresenter> extends FrameLayout {

    protected final K presenter;
    protected boolean isAppbarExpanded; /* Обход разворачивания бара при повторном создании вью */

    public MvpFragment(Context context, K presenter, @LayoutRes int layoutRes) {
        super(context);
        setId(R.id.mvp_fragment_id);
        this.presenter = presenter;
        addView(ToolsView.inflate(this, layoutRes));

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(MvpNavigator.hasBackStack() ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_menu_white_24dp);
            toolbar.setNavigationOnClickListener(v -> SupAndroid.mvpActivity(mvpActivity -> mvpActivity.onFragmentBackPressed()));
        } else {
            View v = findViewById(R.id.back);
            if (v != null && v instanceof ImageView) {
                ImageView vBack = (ImageView) v;
                vBack.setImageResource(MvpNavigator.hasBackStack() ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_menu_white_24dp);
                v.setOnClickListener(vv -> SupAndroid.mvpActivity(mvpActivity -> mvpActivity.onFragmentBackPressed()));
            }
        }

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> isAppbarExpanded = verticalOffset == 0);
        }

    }

    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState());
        bundle.putBoolean("app_bar_expanded", isAppbarExpanded);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            isAppbarExpanded = bundle.getBoolean("app_bar_expanded");

            /* CollapsingToolbarLayout мирцает после програмнного сорачивания */
            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
            if (collapsingToolbarLayout != null) WorkaroundCollapsingToolbarSkrim.suppressSkrim(collapsingToolbarLayout);

            AppBarLayout appBarLayout = findViewById(R.id.app_bar);
            if (appBarLayout != null) appBarLayout.setExpanded(isAppbarExpanded, false);

            state = bundle.getParcelable("SUPER_STATE");
        }
        super.onRestoreInstanceState(state);
    }


    public void load(SparseArray<Parcelable> parcelable) {
        dispatchRestoreInstanceState(parcelable);
    }

    //
    //  LifeCircle
    //

    public void onDestroy() {

    }

}
