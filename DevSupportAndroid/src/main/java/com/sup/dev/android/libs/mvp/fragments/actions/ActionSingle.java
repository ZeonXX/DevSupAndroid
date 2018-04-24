package com.sup.dev.android.libs.mvp.fragments.actions;

import android.support.annotation.NonNull;

import com.sup.dev.java.classes.callbacks.simple.Callback1;

import java.util.List;

/***
 *  Удаляет все действия из списка и добавляет себя
 */
public class ActionSingle<K> extends MvpAction<K> {

    public ActionSingle(@NonNull Callback1<K> commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public void add(@NonNull List list) {
        list.clear();
        list.add(this);
    }



}