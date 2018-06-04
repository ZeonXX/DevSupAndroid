package com.sup.dev.android.views.fragments.alert;

import android.content.Context;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.mvp.fragments.MvpFragment;
import com.sup.dev.android.views.widgets.ViewChip;

public class FAlert extends MvpFragment<PAlert> {

    private final TextView vTitle;
    private final TextView vText;
    private final ViewChip vAction;


    public FAlert(Context context, PAlert presenter) {
        super(context, presenter, R.layout.fragment_alert);

        vTitle = findViewById(R.id.title);
        vText = findViewById(R.id.text);
        vAction = findViewById(R.id.action);

        vAction.setOnClickListener(v -> presenter.onActionClicked());
    }

    //
    //  Presenter
    //

    public void setInfo(String title, String text, String action){
        vTitle.setText(title);
        vText.setText(text);
        vAction.setText(action);
    }
}
