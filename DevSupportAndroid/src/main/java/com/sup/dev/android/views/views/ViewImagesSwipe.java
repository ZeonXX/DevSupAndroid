package com.sup.dev.android.views.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sup.dev.android.R;

public class ViewImagesSwipe extends RecyclerView {

    private final RecyclerCardAdapter adapter = new RecyclerCardAdapter();

    public ViewImagesSwipe(@NonNull Context context) {
        this(context, null);
    }

    public ViewImagesSwipe(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayoutManager(new LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false));
        setAdapter(adapter);
    }

    public void add(Bitmap image) {
        adapter.add(new CardSwipe(image));
    }

    //
    //  Card
    //

    private class CardSwipe extends Card {

        private final Bitmap bitmap;

        public CardSwipe(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public int getLayout() {
            return R.layout.view_image_swipe_card;
        }

        public void bindView(View view) {
          ImageView vImage = view.findViewById(R.id.image);

          vImage.setImageBitmap(bitmap);
        }
    }
}
