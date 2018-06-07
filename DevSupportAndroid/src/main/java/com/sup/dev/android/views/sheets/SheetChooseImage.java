package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.image_loader.ImageLoaderFile;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsFiles;
import com.sup.dev.android.libs.image_loader.ImageLoader;
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
    private boolean autoHideOnSelected;

    public SheetChooseImage(Context viewContext, AttributeSet attrs) {
        super(viewContext, attrs);
        vRecycler.setLayoutManager(new GridLayoutManager(viewContext, ToolsAndroid.isScreenPortrait() ? 3 : 6));

        adapter = new RecyclerCardAdapter();
        vRecycler.setAdapter(adapter);

        loadImages();
    }

    public void loadImages() {
        String[] projection = new String[]{MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = viewContext.getContentResolver().query(
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


    public SheetChooseImage setAutoHideOnSelected(boolean autoHideOnSelected) {
        this.autoHideOnSelected = autoHideOnSelected;
        return this;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (vRecycler != null) vRecycler.setEnabled(enabled);
        if (vTitle != null) vTitle.setEnabled(enabled);
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
                if (autoHideOnSelected) hide();
                else setEnabled(false);
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
