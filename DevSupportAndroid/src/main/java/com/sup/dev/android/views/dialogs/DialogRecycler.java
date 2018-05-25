package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class DialogRecycler extends BaseDialog{

    private final RecyclerView vRecyclerView;

    public DialogRecycler(Context viewContext) {
        super(viewContext,  R.layout.dialog_recycler);

        vRecyclerView =  view.findViewById(R.id.recycler);
        vRecyclerView.setLayoutManager(new LinearLayoutManager(viewContext));
    }

    public DialogRecycler setAdapter(RecyclerView.Adapter adapter){
        vRecyclerView.setAdapter(adapter);
        return this;
    }

    //
    //  Setters
    //


    public DialogRecycler setTitle(@StringRes int title) {
        return (DialogRecycler)super.setTitle(title);
    }

    public DialogRecycler setTitle(String title) {
        return (DialogRecycler)super.setTitle(title);
    }

    public DialogRecycler setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogRecycler)super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogRecycler setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogRecycler)super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogRecycler setCancelable(boolean cancelable) {
        return (DialogRecycler)super.setCancelable(cancelable);
    }

    public DialogRecycler setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogRecycler)super.setOnCancel(onCancel);
    }

    public DialogRecycler setOnCancel(@StringRes int s) {
        return (DialogRecycler)super.setOnCancel(s);
    }

    public DialogRecycler setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogRecycler)super.setOnCancel(s, onCancel);
    }

    public DialogRecycler setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogRecycler)super.setOnCancel(s, onCancel);
    }

    public DialogRecycler setEnabled(boolean enabled) {
        return (DialogRecycler)super.setEnabled(enabled);
    }

    public DialogRecycler setOnEnter(@StringRes int s) {
        return (DialogRecycler)super.setOnEnter(s);
    }

    public DialogRecycler setOnEnter(String s) {
        return (DialogRecycler)super.setOnEnter(s);
    }

    public DialogRecycler setOnEnter(@StringRes int s, Callback1<BaseDialog> onEnter) {
        return (DialogRecycler)super.setOnEnter(s, onEnter);
    }

    public DialogRecycler setOnEnter(String s, Callback1<BaseDialog> onEnter) {
        return (DialogRecycler)super.setOnEnter(s, onEnter);
    }


}
