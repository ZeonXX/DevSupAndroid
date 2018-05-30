package com.sup.dev.android.libs.mvp.fragments;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.tools.ToolsView;

public class MvpFragment<K extends MvpPresenter> extends FrameLayout{

    protected final K presenter;

    public MvpFragment(Context context, K presenter, @LayoutRes int layoutRes) {
        super(context);
        setId(R.id.mvp_fragment_id);
        this.presenter = presenter;
        addView(ToolsView.inflate(this, layoutRes));

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setNavigationIcon(MvpNavigator.hasBackStack() ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_menu_white_24dp);
            toolbar.setNavigationOnClickListener(v -> SupAndroid.mvpActivity(mvpActivity -> mvpActivity.onFragmentBackPressed()));
        }else {
            View v = findViewById(R.id.back);
            if(v != null && v instanceof ImageView){
                ImageView vBack = (ImageView) v;
                vBack.setImageResource(MvpNavigator.hasBackStack() ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_menu_white_24dp);
                v.setOnClickListener(vv -> SupAndroid.mvpActivity(mvpActivity -> mvpActivity.onFragmentBackPressed()));
            }
        }
    }

    public void load(SparseArray<Parcelable> parcelable){
        dispatchRestoreInstanceState(parcelable);
    }

    //
    //  LifeCircle
    //

    public void onDestroy() {

    }

}
