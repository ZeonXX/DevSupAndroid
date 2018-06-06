package com.sup.dev.android.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.widgets.ViewFadeImage;
import com.sup.dev.android.views.widgets._support.FadeView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.collections.CashBytes;
import com.sup.dev.java.classes.providers.Provider1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsNetwork;
import com.sup.dev.java.tools.ToolsThreads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ToolsImageLoader {

    public static final Provider1<Bitmap, Bitmap> TRANSFORMER_SQUARE_CENTER = bitmap -> ToolsBitmap.cropCenterSquare(bitmap);

    private static final CashBytes<Object> bitmapCash = new CashBytes<>(1024 * 1024 * ((android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) ? 5 : 10));
    private static final ArrayList<Loader> turn = new ArrayList<>();
    private static ThreadPoolExecutor threadPool;
    public static Provider1<Long, byte[]> loaderById;

    static {
        threadPool = new ThreadPoolExecutor(1, 8, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    }

    public static BitmapFactory.Options OPTIONS_RGB_565() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        return opt;
    }

    //
    //  Public
    //

    public static void clearCash(long imageId) {
        bitmapCash.remove(asKey(imageId));
    }

    public static void clearCash(String url) {
        bitmapCash.remove(url);
    }

    public static void replace(long imageId, byte[] bytes) {
        bitmapCash.replace(asKey(imageId), bytes);
    }

    public static void replace(String url, byte[] bytes) {
        bitmapCash.replace(asKey(url), bytes);
    }

    public static void unsubscribe(ImageView vImage) {

        if (vImage == null) return;

        for (int i = 0; i < turn.size(); i++)
            if (turn.get(i).vImage == vImage)
                turn.remove(i--);

    }

    public static void load(long imageId) {
        load(imageId, null, null);
    }

    public static void load(long imageId, Callback1<byte[]> onLoaded) {
        load(imageId, null, onLoaded);
    }

    public static void load(long imageId, ImageView vImage) {
        load(imageId, vImage, null);
    }

    public static void load(long imageId, ImageView vImage, Callback1<byte[]> onLoaded) {
        load(new LoaderId(imageId).setImage(vImage).setOnLoaded(onLoaded));
    }

    public static void load(String url) {
        load(url, null, null);
    }

    public static void load(String url, Callback1<byte[]> onLoaded) {
        load(url, null, onLoaded);
    }

    public static void load(String url, ImageView vImage) {
        load(url, vImage, null);
    }

    public static void load(String url, ImageView vImage, Callback1<byte[]> onLoaded) {
        load(new LoaderUrl(url).setImage(vImage).setOnLoaded(onLoaded));
    }

    public static void load(Loader loader) {

        byte[] bytes = bitmapCash.get(loader.key);

        if (bytes != null) {
            if (loader.onLoaded != null) loader.onLoaded.callback(bytes);
            if (loader.vImage != null) ToolsThreads.thread(() -> putImage(loader, parseImage(loader, bytes), false));
            return;
        }

        if (loader.vImage != null) {
            if (loader.w != 0 && loader.h != 0) {
                Bitmap bitmap = Bitmap.createBitmap(loader.w, loader.h, Bitmap.Config.RGB_565);
                bitmap.eraseColor(ToolsResources.getColor(R.color.focus));
                loader.vImage.setImageBitmap(bitmap);
                if (loader.vImage instanceof ViewFadeImage) ((ViewFadeImage) loader.vImage).setFadeColor(ToolsResources.getColor(R.color.focus));
            } else {
                loader.vImage.setImageResource(R.color.focus);
            }

            if (loader.vImage instanceof FadeView) ((FadeView) loader.vImage).stopFade();

            for (int i = 0; i < turn.size(); i++)
                if (turn.get(i).vImage == loader.vImage)
                    turn.remove(i--);

        }

        turn.add(loader);

        for (Loader l : turn) if (l.isKey(loader.key) && l != loader) return;

        threadPool.execute(() -> {
            try {
                loadNow(loader);
            } catch (Throwable ex) {
                Debug.log(ex);
            }
        });
    }

    //
    //  Methods
    //

    private static void loadNow(Loader loader) {

        if (!turn.contains(loader)) return;

        byte[] loadedBytes = loader.load();
        Bitmap bitmap = parseImage(loader, loadedBytes);
        byte[] bytes = loader.cashScaledBytes ? ToolsBitmap.toJPGBytes(bitmap, 100) : loadedBytes;

        ToolsThreads.main(() -> {

            bitmapCash.add(loader.key, bytes);
            for (int i = 0; i < turn.size(); i++) {
                Loader l = turn.get(i);
                if (l.isKey(loader.key)) {
                    if (l.onLoaded != null) l.onLoaded.callback(bytes);
                    if (l.vImage != null) putImage(l, bitmap, true);
                    turn.remove(i--);
                }
            }

        });

    }

    private static Bitmap parseImage(Loader loader, byte[] bytes) {
        Bitmap bm = ToolsBitmap.decode(bytes, loader.w, loader.h, loader.options);
        if (loader.transformer != null) bm = loader.transformer.provide(bm);
        return bm;
    }

    private static void putImage(Loader loader, Bitmap bm, boolean animate) {
        ToolsThreads.main(() -> {
            if (!loader.isKey(loader.vImage.getTag())) return;
            loader.vImage.setImageBitmap(bm);
            if (animate && loader.vImage instanceof FadeView) ((FadeView) loader.vImage).makeFade();
        });
    }

    //
    //  Support
    //

    private static String asKey(long imageId) {
        return "imgId_" + imageId;
    }

    private static String asKey(String url) {
        return "url_" + url;
    }

    //
    //  Loaders
    //

    public static abstract class Loader {

        private ImageView vImage;
        private Object key;
        private Callback1<byte[]> onLoaded;
        private Provider1<Bitmap, Bitmap> transformer;
        private BitmapFactory.Options options;
        private int w;
        private int h;
        private boolean cashScaledBytes;

        public Loader setImage(ImageView vImage) {
            this.vImage = vImage;
            if (vImage != null) vImage.setTag(key);
            return this;
        }

        public Loader setSizes(int w, int h) {
            this.w = w;
            this.h = h;
            return this;
        }

        protected Loader setKey(Object key) {
            this.key = key;
            return this;
        }

        public Loader setOnLoaded(Callback1<byte[]> onLoaded) {
            this.onLoaded = onLoaded;
            return this;
        }

        public Loader setTransformer(Provider1<Bitmap, Bitmap> transformer) {
            this.transformer = transformer;
            return this;
        }

        public Loader setOptions(BitmapFactory.Options options) {
            this.options = options;
            return this;
        }

        public Loader setCashScaledBytes(boolean cashScaledBytes) {
            this.cashScaledBytes = cashScaledBytes;
            return this;
        }

        boolean isKey(Object key) {
            return key == this.key || (key != null && key.equals(this.key));
        }

        abstract byte[] load();
    }

    public static class LoaderId extends Loader {

        private final long imageId;

        public LoaderId(long imageId) {
            this.imageId = imageId;
            setKey(imageId);
        }

        byte[] load() {
            return loaderById.provide(imageId);
        }
    }

    public static class LoaderUrl extends Loader {

        private final String url;

        public LoaderUrl(String url) {
            this.url = url;
            setKey(url);
        }

        byte[] load() {
            try {
                return ToolsNetwork.getBytesFromURL(url);
            } catch (IOException e) {
                Debug.log(e);
                return null;
            }
        }
    }

    public static class LoaderFile extends Loader {

        private final File file;

        public LoaderFile(File file) {
            this.file = file;
            setKey("file_" + file.getAbsolutePath());
        }

        byte[] load() {
            try {
                byte[] bytes = ToolsFiles.readFile(file);
                return bytes;
            } catch (IOException e) {
                Debug.log(e);
                return null;
            }
        }
    }
}

