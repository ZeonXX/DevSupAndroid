package com.sup.dev.android.views.fragments.loading;

import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;

public abstract class PLoading<K extends FLoading> extends MvpPresenter<K>{

    public PLoading(Class<K> viewClass) {
        super(viewClass);
    }

    //
    //  Fragment
    //

    public abstract void onReloadClicked();

}
