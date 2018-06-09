package com.sup.dev.android.views.sheets;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.image_loader.ImageLoader;
import com.sup.dev.android.libs.image_loader.ImageLoaderFile;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsFiles;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;

import java.io.File;
import java.io.IOException;

public class SheetChooseImage extends SheetRecycler {

    private final RecyclerCardAdapter adapter;

    private Callback1<Bitmap> onSelected;
    private Callback onError;

    public SheetChooseImage() {
        adapter = new RecyclerCardAdapter();
        setAdapter(adapter);
        loadImages();
    }

    @Override
    public void bindView(View view) {
        super.bindView(view);

        RecyclerView vRecycler = view.findViewById(R.id.recycler);

        vRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), ToolsAndroid.isScreenPortrait() ? 3 : 6));

    }

    public void loadImages() {
        String[] projection = new String[]{MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = SupAndroid.appContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        while (cursor.moveToNext()) adapter.add(new CardImage(new File(cursor.getString(0))));

    }

    //
    //  Setters
    //

    public SheetChooseImage setOnSelected(Callback1<Bitmap> onSelected) {
        this.onSelected = onSelected;
        return this;
    }

    public SheetChooseImage setOnError(Callback onError) {
        this.onError = onError;
        return this;
    }

    //
    //  Card
    //

    private class CardImage extends Card {

        private final File file;

        public CardImage(File file) {
            this.file = file;
        }

        @Override
        public int getLayout() {
            return R.layout.sheet_choose_image_card;
        }

        @Override
        public void bindView(View view) {
            ImageView vImage = view.findViewById(R.id.image);
            vImage.setOnClickListener(v -> {
                if (onSelected != null)
                    try {
                        onSelected.callback(ToolsBitmap.decode(ToolsFiles.readFile(file)));
                    } catch (IOException e) {
                        Debug.log(e);
                        if (onError != null) onError.callback();
                    }
                hide();
            });

            ImageLoader.load(new ImageLoaderFile(file)
                    .setImage(vImage)
                    .cashScaledBytes()
                    .sizes(512, 512)
                    .options(ImageLoader.OPTIONS_RGB_565())
                    .cropSquare());
        }


    }


}
