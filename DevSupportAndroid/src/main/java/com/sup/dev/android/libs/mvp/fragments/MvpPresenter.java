package com.sup.dev.android.libs.mvp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.actions.ActionAdd;
import com.sup.dev.android.libs.mvp.fragments.actions.ActionSingle;
import com.sup.dev.android.libs.mvp.fragments.actions.ActionSingleExecute;
import com.sup.dev.android.libs.mvp.fragments.actions.ActionSkip;
import com.sup.dev.android.libs.mvp.fragments.actions.MvpAction;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.views.elements.dialogs.DialogProgressTransparent;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.utils.interfaces.UtilsThreads;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class MvpPresenter<K extends MvpFragmentInterface> implements MvpPresenterInterface {

    private final UtilsThreads utilsThreads = SupAndroid.di.utilsThreads();
    protected final MvpNavigator navigator = SupAndroid.di.navigator();

    private final ArrayList<MvpAction<K>> actions = new ArrayList<>();
    private final Class<? extends K> fragmentClass;
    private int key;

    private WeakReference<K> view;

    public MvpPresenter(Class<? extends K> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    @Override
    public Fragment instanceFragment() {
        K k;
        try {
            k = fragmentClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        k.setPresenter(this);
        return (Fragment) k;
    }

    //
    //  Methods
    //

    public void showProgressDialog(CallbackSource<DialogProgressTransparent> onShow){
        navigator.showProgressDialog(onShow);
    }

    //
    //  Events
    //

    @MainThread
    public final void onAttachView(MvpFragmentInterface view) {
        this.view = new WeakReference<>((K) view);

        for (int i = 0; i < actions.size(); i++)
            if (executeAction(actions.get(i)))
                i--;

        onAttachView();
    }


    @Override
    @CallSuper
    public void onDetachView() {
        this.view = null;
    }

    @Override
    public void onAttachView(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    //
    //  Setters
    //

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    //
    //  Getters
    //

    public boolean isViewAttached() {
        return view != null && view.get() != null;
    }

    @Override
    public int getKey() {
        return key;
    }

    @VisibleForTesting
    public K getMvpFragment() {
        return view == null ? null : view.get();
    }

    //
    //  Actions
    //

    public boolean executeAction(MvpAction<K> action) {
        if (!isViewAttached()) return false;
        boolean needRemove = action.execute(view.get());
        if (needRemove) actions.remove(action);
        return needRemove;
    }

    // Удаляет все события с таким тегом в списке
    public void clearTag(@NonNull String tag) {
        for (int i = 0; i < actions.size(); i++)
            if (actions.get(i).tag != null && actions.get(i).tag.equals(tag)) {
                actions.remove(i);
                i--;
            }
    }

    public boolean containsTag(@NonNull String tag) {
        for (int i = 0; i < actions.size(); i++)
            if (actions.get(i).tag != null && actions.get(i).tag.equals(tag))
                return true;
        return false;
    }

    @MainThread
    public void action(@NonNull MvpAction<K> action) {
        utilsThreads.main(() -> {
            action.add(actions);
            executeAction(action);
        });
    }

    //  Добавляет действие в конец списка
    public void actionAdd(CallbackSource<K> executor) {
        action(new ActionAdd<>(executor));
    }

    //  Добавляет действие с тегом в конец списка
    public void actionAdd(String tag, CallbackSource<K> executor) {
        action(new ActionAdd<>(tag, executor));
    }

    //  Удаляет все действия из списка и добавляет себя
    public void actionSingle(CallbackSource<K> executor) {
        action(new ActionSingle<>(executor));
    }

    //  Удаляет все действия с такимже тегом и добавляет действие в список
    public void actionSingle(String tag, CallbackSource<K> executor) {
        clearTag(tag);
        action(new ActionAdd<>(tag, executor));
    }

    //  Дабавляет действие в конец списка и удаляется после исполнения
    public void actionSingleExecute(CallbackSource<K> executor) {
        action(new ActionSingleExecute<>(executor));
    }

    //  Не добавляет в список. Выполняется только один раз и если view подключено к презентору, если нет, то пропускается
    public void actionSkip(CallbackSource<K> executor) {
        action(new ActionSkip<>(executor));
    }

}
