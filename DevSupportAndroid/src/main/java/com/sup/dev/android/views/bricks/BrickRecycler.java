package com.sup.dev.android.views.bricks;

import android.support.annotation.CallSuper;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;

public class BrickRecycler extends Brick {

    protected RecyclerView.Adapter adapter;
    protected String title;

    @Override
    public int getLayoutRes() {
        return R.layout.brick_recycler;
    }

    @Override
    @CallSuper
    public void bindView(View view) {
        RecyclerView vRecycler = view.findViewById(R.id.recycler);
        TextView vTitle = view.findViewById(R.id.title);

        vTitle.setText(title);
        vTitle.setVisibility(title != null && title.length() > 0 ? View.VISIBLE : View.GONE);

        vRecycler.setAdapter(adapter);
    }

    //
    //  Setters
    //

    public <K extends BrickRecycler> K setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        update();
        return (K) this;
    }

    public <K extends BrickRecycler> K setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public <K extends BrickRecycler> K setTitle(String title) {
        this.title = title;
        return (K) this;
    }


}
