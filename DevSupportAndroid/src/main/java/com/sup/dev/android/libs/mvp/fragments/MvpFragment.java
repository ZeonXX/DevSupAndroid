package com.sup.dev.android.libs.mvp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.android.views.widgets.ViewIcon;


public abstract class MvpFragment<K extends MvpPresenterInterface> extends Fragment implements MvpFragmentInterface {

    private static final String KEY = "KEY";
    protected final MvpNavigator navigator = SupAndroid.di.navigator();
    protected final UtilsResources utilsResources = SupAndroid.di.utilsResources();
    protected final UtilsView utilsView = SupAndroid.di.utilsView();

    private boolean attached = false;
    protected boolean alwaysBackIconMode;
    protected K presenter;

    //
    //  LifeCircle
    //

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle bundle) {

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setNavigationIcon(alwaysBackIconMode || navigator.hasBackStack() ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_menu_white_24dp);
            toolbar.setNavigationOnClickListener(v -> SupAndroid.di.mvpActivity(mvpActivity -> mvpActivity.onFragmentBackPressed()));
        }else {
            View v = findViewById(R.id.back);
            if(v != null && v instanceof ImageView){
                ImageView vBack = (ImageView) v;
                vBack.setImageResource(alwaysBackIconMode || navigator.hasBackStack() ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_menu_white_24dp);
                v.setOnClickListener(vv -> SupAndroid.di.mvpActivity(mvpActivity -> mvpActivity.onFragmentBackPressed()));
            }
        }


        onViewCreated();
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null && bundle.containsKey(KEY)) {
            presenter = (K) navigator.getPresenter(bundle.getInt(KEY));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        attach();
    }

    @Override
    public void onResume() {
        super.onResume();
        attach();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(KEY, presenter.getKey());
        detach();
    }

    @Override
    public void onStop() {
        super.onStop();
        detach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detach();
    }

    private void attach() {
        if (!attached && presenter != null) {
            attached = true;
            presenter.onAttachView(this);
        }
    }

    private void detach() {
        if (attached && presenter != null) {
            attached = false;
            presenter.onDetachView();
        }
    }

    //
    //  Methods
    //

    protected <V extends View> V findViewById(int id) {
        if (getView() == null) throw new RuntimeException("Call it on onViewCreated()");
        return getView().findViewById(id);
    }

    //
    //  Setters
    //

    public void setPresenter(MvpPresenterInterface presenter) {
        this.presenter = (K) presenter;
    }

    //
    //  Getters
    //


    public K getPresenter() {
        return presenter;
    }

    public boolean isAttachedToPresenter() {
        return attached;
    }

    //
    //  For Override
    //

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void onViewCreated();


}
