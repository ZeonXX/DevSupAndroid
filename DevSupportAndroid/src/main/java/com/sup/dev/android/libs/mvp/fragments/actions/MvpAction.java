package com.sup.dev.android.libs.mvp.fragments.actions;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.sup.dev.java.classes.callbacks.simple.Callback1;

import java.util.List;

public abstract class MvpAction<K> {

    public final String tag;
    @NonNull private final Callback1<K> commandExecutor;

    public MvpAction(@NonNull Callback1<K> commandExecutor) {
        this(null, commandExecutor);
    }

    public MvpAction(String tag, @NonNull Callback1<K> commandExecutor) {
        this.tag = tag;
        this.commandExecutor = commandExecutor;
    }

    @MainThread
    public boolean execute(K view) {
        boolean needRemove_1 = beforeExecution();
        commandExecutor.callback(view);
        boolean needRemove_2 = afterExecution();
        return needRemove_1 || needRemove_2;

    }

    @MainThread
    public void add(@NonNull List<MvpAction<K>> actions) {
        actions.add(this);
    }

    @MainThread
    public boolean beforeExecution() {
        return false;
    }

    @MainThread
    public boolean afterExecution() {
        return false;
    }

}
