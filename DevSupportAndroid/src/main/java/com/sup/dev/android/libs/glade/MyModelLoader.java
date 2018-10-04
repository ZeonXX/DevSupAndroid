package com.sup.dev.android.libs.glade;

import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;

import java.io.File;
import java.io.InputStream;

public class MyModelLoader implements ModelLoader<File, InputStream> {

    @Override
    public LoadData<InputStream> buildLoadData(File model, int width, int height,
                                               Options options) {
        return new LoadData<InputStream>(model, new MyDataFetcher(model));
    }

    @Override
    public void handles(File model) {
        return true;
    }

    public static class MyDataFetcher implements DataFetcher<InputStream> {

        public MyDataFetcher(File model){

        }

        @Override
        public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {

        }

        @Override
        public void cleanup() {

        }

        @Override
        public void cancel() {

        }

        @NonNull
        @Override
        public Class<InputStream> getDataClass() {
            return null;
        }

        @NonNull
        @Override
        public DataSource getDataSource() {
            return null;
        }
    }
}