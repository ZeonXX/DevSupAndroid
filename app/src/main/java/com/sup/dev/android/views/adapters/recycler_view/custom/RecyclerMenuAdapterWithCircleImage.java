package com.sup.dev.android.views.adapters.recycler_view.custom;

import android.view.View;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.java.classes.callbacks.simple.CallbackPair;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.classes.items.Pair;

public class RecyclerMenuAdapterWithCircleImage<K> extends RecyclerMenuAdapter<K>{

    private final CallbackPair<ImageView, K> onLoadImage;

    public RecyclerMenuAdapterWithCircleImage(CallbackSource<K> onSelected, CallbackPair<ImageView, K> onLoadImage) {
        super(R.layout.row_text_with_circle_image, onSelected);

        this.onLoadImage=  onLoadImage;
    }

    @Override
    protected void bind(View view, Pair<K, String> item) {
        super.bind(view, item);

        onLoadImage.callback(view.findViewById(R.id.image), item.left);
    }

}
