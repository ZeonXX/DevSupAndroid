package com.sup.dev.android.utils.implementations;

import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsBitmap;
import com.sup.dev.android.utils.interfaces.UtilsImageLoader;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.android.views.widgets._support.ViewImageFlash;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.collections.CashBytes;
import com.sup.dev.java.classes.providers.Provider1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.utils.interfaces.UtilsNetwork;
import com.sup.dev.java.utils.interfaces.UtilsThreads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UtilsImageLoaderImpl implements UtilsImageLoader {

    private final UtilsBitmap utilsBitmap = SupAndroid.di.utilsBitmap();
    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final UtilsNetwork utilsNetwork = SupAndroid.di.utilsNetwork();
    private final UtilsThreads utilsThreads = SupAndroid.di.utilsThreads();

    private final CashBytes<Object> bitmapCash = new CashBytes<>(1024 * 1024 * 5);
    private final ArrayList<Loader> turn = new ArrayList<>();
    private final Provider1<Long, byte[]> loaderCustom;
    private final ThreadPoolExecutor threadPool;

    public UtilsImageLoaderImpl() {
        this(null);
    }

    public UtilsImageLoaderImpl(Provider1<Long, byte[]> loaderCustom) {
        this.loaderCustom = loaderCustom;
        threadPool = new ThreadPoolExecutor(1, 4, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    }

    //
    //  Public
    //

    public void clearCash(long imageId) {
        synchronized (bitmapCash) {
            bitmapCash.remove(asKey(imageId));
        }
    }

    public void clearCash(String url) {
        synchronized (bitmapCash) {
            bitmapCash.remove(url);
        }
    }

    public void replace(long imageId, byte[] bytes) {
        synchronized (bitmapCash) {
            bitmapCash.replace(asKey(imageId), bytes);
        }
    }

    public void replace(String url, byte[] bytes) {
        synchronized (bitmapCash) {
            bitmapCash.replace(asKey(url), bytes);
        }
    }

    public void unsubscribe(ImageView vImage) {

        if (vImage == null) return;

        for (int i = 0; i < turn.size(); i++)
            if (turn.get(i).vImage == vImage)
                turn.remove(i--);

    }

    public void load(long imageId) {
        load(imageId, null, null);
    }

    public void load(long imageId, Callback1<byte[]> onLoaded) {
        load(imageId, null, onLoaded);
    }

    public void load(long imageId, ImageView vImage) {
        load(imageId, vImage, null);
    }

    public void load(long imageId, ImageView vImage, Callback1<byte[]> onLoaded) {
        load(new LoaderCustom(imageId, vImage, onLoaded));
    }

    public void load(String url) {
        load(url, null, null);
    }

    public void load(String url, Callback1<byte[]> onLoaded) {
        load(url, null, onLoaded);
    }

    public void load(String url, ImageView vImage) {
        load(url, vImage, null);
    }

    public void load(String url, ImageView vImage, Callback1<byte[]> onLoaded) {
        load(new LoaderUrl(url, vImage, onLoaded));
    }

    public void load(Loader loader) {

        if (checkCash(loader)) return;

        if (loader.vImage != null) loader.vImage.setImageResource(R.color.focus);

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

    private void loadNow(Loader loader) {

        byte[] bytes = loader.load();
        synchronized (bitmapCash) {
            bitmapCash.add(loader.key, bytes);
        }

        utilsThreads.main(() -> {

            for (int i = 0; i < turn.size(); i++) {
                if (turn.get(i).isKey(loader.key)) {
                    if (turn.get(i).onLoaded != null) turn.get(i).onLoaded.callback(bytes);
                    if (turn.get(i).vImage != null) putImage(turn.get(i), bytes, true);
                    turn.remove(i--);
                }
            }

        });

    }

    private void putImage(Loader loader, byte[] bytes, boolean animate) {
        if (!loader.isKey(loader.vImage.getTag())) return;
        loader.vImage.setImageBitmap(utilsBitmap.decode(bytes));
        utilsView.fromAlpha(loader.vImage);
        if (animate && loader.vImage instanceof ViewImageFlash) ((ViewImageFlash) loader.vImage).makeFlash();
    }

    private boolean checkCash(Loader loader) {

        byte[] bytes;
        synchronized (bitmapCash) {
            bytes = bitmapCash.get(loader.key);
        }

        if (bytes != null) {
            if (loader.onLoaded != null) loader.onLoaded.callback(bytes);
            if (loader.vImage != null) putImage(loader, bytes, false);
        }

        return bytes != null;
    }

    //
    //  Support
    //

    private String asKey(long imageId){
        return "imgId_" + imageId;
    }

    private String asKey(String url){
        return "url_" + url;
    }

    //
    //  Loaders
    //

    private abstract class Loader {

        private final ImageView vImage;
        private final Object key;
        private final Callback1<byte[]> onLoaded;

        private Loader(ImageView vImage, Object key, Callback1<byte[]> onLoaded) {
            this.vImage = vImage;
            this.key = key;
            this.onLoaded = onLoaded;

            if (vImage != null) vImage.setTag(key);
        }

        boolean isKey(Object key) {
            return key == this.key || (key != null && key.equals(this.key));
        }

        abstract byte[] load();
    }

    private class LoaderCustom extends Loader {

        private long imageId;

        private LoaderCustom(long imageId, ImageView vImage, Callback1<byte[]> onLoaded) {
            super(vImage, asKey(imageId), onLoaded);
            this.imageId = imageId;
        }

        byte[] load() {
            return loaderCustom.provide(imageId);
        }
    }

    private class LoaderUrl extends Loader {

        private final String url;

        private LoaderUrl(String url, ImageView vImage, Callback1<byte[]> onLoaded) {
            super(vImage, asKey(url), onLoaded);
            this.url = url;
        }

        byte[] load() {
            try {
                return utilsNetwork.getBytesFromURL(url);
            } catch (IOException e) {
                Debug.log(e);
                return null;
            }
        }
    }
}
