package com.sup.dev.android.libs.mvp.fragments.actions;

import android.support.annotation.NonNull;

import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

/***
 *  Дбавляет действие в конец списка
 */
public class ActionAdd<K> extends MvpAction<K> {

    public ActionAdd(@NonNull CallbackSource<K> commandExecutor) {
        super(commandExecutor);
    }

    public ActionAdd(String tag, @NonNull CallbackSource<K> commandExecutor) {
        super(tag, commandExecutor);
    }

}
