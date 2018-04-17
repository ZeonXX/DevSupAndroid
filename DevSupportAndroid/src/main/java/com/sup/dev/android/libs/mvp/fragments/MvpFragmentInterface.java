package com.sup.dev.android.libs.mvp.fragments;

import android.os.Parcelable;
import android.util.SparseArray;

public interface MvpFragmentInterface {

    void onDestroy();

    void load(SparseArray<Parcelable> state);

}
