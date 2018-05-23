package com.sup.dev.android.libs.mvp.fragments;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.actions.ActionAdd;
import com.sup.dev.android.libs.mvp.fragments.actions.ActionSingle;
import com.sup.dev.android.libs.mvp.fragments.actions.ActionSingleExecute;
import com.sup.dev.android.libs.mvp.fragments.actions.ActionSkip;
import com.sup.dev.android.libs.mvp.fragments.actions.MvpAction;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.tools.ToolsThreads;

import java.util.ArrayList;

public class MvpPresenter<K extends MvpFragment>{

    private final ArrayList<MvpAction<K>> actions = new ArrayList<>();
    private final Class<? extends K> viewClass;

    protected SparseArray<Parcelable> state;
    protected boolean backStackAllowed = true;
    private K view;

    public MvpPresenter(Class<? extends K> viewClass) {
        this.viewClass = viewClass;
    }

    public MvpFragment instanceView(Context context) {

        if(view != null)view.onDestroy();

        try {
            view = viewClass.getConstructor(Context.class, this.getClass()).newInstance(context, this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(state != null){
            view.load(state);
            state = null;
        }

        for (int i = 0; i < actions.size(); i++)
            if (executeAction(actions.get(i)))
                i--;

        onAttachView();

        return view;
    }

    public void clearView() {
        if(view != null) {
            state = new SparseArray<>();
            view.saveHierarchyState(state);
            view.onDestroy();
        }
        view = null;
    }

    //
    //  Events
    //

    protected void onAttachView(){

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onDestroy() {

    }

    public boolean onBackPressed() {
        return false;
    }

    //
    //  Getters
    //

    public boolean isViewAttached() {
        return view != null;
    }

    public boolean isBackStackAllowed() {
        return backStackAllowed;
    }

    //
    //  Actions
    //

    public boolean executeAction(MvpAction<K> action) {
        if (!isViewAttached()) return false;
        boolean needRemove = action.execute(view);
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
        ToolsThreads.main(() -> {
            action.add(actions);
            executeAction(action);
        });
    }

    //  Добавляет действие в конец списка
    public void actionAdd(Callback1<K> executor) {
        action(new ActionAdd<>(executor));
    }

    //  Добавляет действие с тегом в конец списка
    public void actionAdd(String tag, Callback1<K> executor) {
        action(new ActionAdd<>(tag, executor));
    }

    //  Удаляет все действия из списка и добавляет себя
    public void actionSingle(Callback1<K> executor) {
        action(new ActionSingle<>(executor));
    }

    //  Удаляет все действия с такимже тегом и добавляет действие в список
    public void actionSingle(String tag, Callback1<K> executor) {
        clearTag(tag);
        action(new ActionAdd<>(tag, executor));
    }

    //  Дабавляет действие в конец списка и удаляется после исполнения
    public void actionSingleExecute(Callback1<K> executor) {
        action(new ActionSingleExecute<>(executor));
    }

    //  Не добавляет в список. Выполняется только один раз и если view подключено к презентору, если нет, то пропускается
    public void actionSkip(Callback1<K> executor) {
        action(new ActionSkip<>(executor));
    }

}
