package com.sup.dev.android.views.bricks;

import android.support.annotation.CallSuper;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;

public class BrickRecycler extends Brick {

    protected RecyclerCardAdapter adapter;
    protected String title;

    @Override
    public int getLayoutRes(Mode mode) {
        return R.layout.brick_recycler;
    }

    @Override
    @CallSuper
    public void bindView(View view, Mode mode) {
        RecyclerView vRecycler = view.findViewById(R.id.recycler);

        if(mode == Mode.SHEET) vRecycler.getLayoutParams().height = ToolsView.dpToPx(320);
        else if(mode == Mode.FRAGMENT) vRecycler.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        else if (mode == Mode.POPUP) {
            vRecycler.setMinimumWidth(ToolsView.dpToPx(164));
            vRecycler.getLayoutParams().height = ToolsView.dpToPx(264);
        }

        TextView vTitle = view.findViewById(R.id.title);

        vTitle.setText(title);
        vTitle.setVisibility(title != null && title.length() > 0 ? View.VISIBLE : View.GONE);

        vRecycler.setAdapter(adapter);
    }

    //
    //  Setters
    //

    public <K extends BrickRecycler> K setAdapter(RecyclerCardAdapter adapter) {
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
