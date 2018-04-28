package com.sup.dev.android.views.adapters.recycler_view.custom;

import android.view.View;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.items.Item2;

public class RecyclerMenuAdapterWithCircleImage<K> extends RecyclerMenuAdapter<K>{

    private final Callback2<ImageView, K> onLoadImage;

    public RecyclerMenuAdapterWithCircleImage(Callback1<K> onSelected, Callback2<ImageView, K> onLoadImage) {
        super(R.layout.row_text_with_circle_image, onSelected);

        this.onLoadImage=  onLoadImage;
    }

    @Override
    protected void bind(View view, Item2<K, String> item) {
        super.bind(view, item);

        onLoadImage.callback(view.findViewById(R.id.image), item.a1);
    }

}
