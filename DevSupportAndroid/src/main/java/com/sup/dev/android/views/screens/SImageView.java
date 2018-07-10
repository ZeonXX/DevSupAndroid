package com.sup.dev.android.views.screens;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.image_loader.ImageLoaderId;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsStorage;
import com.sup.dev.android.tools.ToolsToast;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetProgressTransparent;
import com.sup.dev.java.tools.ToolsThreads;

public class SImageView extends Screen{

    public SImageView(Bitmap bitmap) {
        this(bitmap, 0);
    }

    public SImageView(long id) {
        this(null, id);
    }

    private SImageView(Bitmap bitmap, long id) {
        super(R.layout.screen_image_view);

        ImageView vImage   = findViewById(R.id.image);
        ViewIcon vDownload = findViewById(R.id.download);

        vDownload.setOnClickListener(v -> {
            Widget dialog = ToolsView.showProgressDialog(SupAndroid.TEXT_APP_DOWNLOADING);
            ToolsThreads.thread(() -> {
                if(bitmap != null)  ToolsStorage.saveImageInDownloadFolder(bitmap, null);
                else if(id > 0) ImageLoaderId.load(id, bytes -> ToolsStorage.saveImageInDownloadFolder(ToolsBitmap.decode(bytes), null));
                dialog.hide();
                ToolsToast.show(SupAndroid.TEXT_APP_DOWNLOADED);
            });

        });

        if(bitmap != null) vImage.setImageBitmap(bitmap);
        else if(id > 0) ImageLoaderId.load(id, vImage);


    }

}
