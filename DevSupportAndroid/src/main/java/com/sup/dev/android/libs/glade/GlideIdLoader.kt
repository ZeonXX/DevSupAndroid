package com.sup.dev.android.libs.glade
/*
import android.content.Context
import com.bumptech.glide.Priority
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GenericLoaderFactory
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.sup.dev.android.tools.ToolsImagesLoader
import java.io.ByteArrayInputStream
import java.io.InputStream

class GlideIdLoader(var mModelCache : ModelCache<GlideId, GlideId>) : ModelLoader<GlideIdLoader.GlideId, InputStream?> {

    override fun getResourceFetcher(model: GlideId, width: Int, height: Int): DataFetcher<InputStream?> {
        var imageFid: GlideId? = mModelCache.get(model, 0, 0)
        if (imageFid == null) {
            mModelCache.put(model, 0, 0, model)
            imageFid = model
        }
        return GlideIdFetcher(imageFid)
    }

    class Factory : ModelLoaderFactory<GlideId, InputStream?> {

        private val mModelCache = ModelCache<GlideId, GlideId>(500)

        override fun build(context: Context, factories: GenericLoaderFactory): ModelLoader<GlideId, InputStream?> {
            return GlideIdLoader(mModelCache)
        }

        override fun teardown() {

        }
    }

    //
    //  Fetcher
    //

    class GlideIdFetcher(val imageId: GlideId) : DataFetcher<InputStream?> {

        override fun loadData(priority: Priority): InputStream? {
            val bytes = ToolsImagesLoader.loaderId.invoke(imageId.id)
            if(bytes != null) return ByteArrayInputStream(bytes)
            return null
        }

        override fun cleanup() {

        }

        override fun getId(): String {
            return imageId.getKey()
        }

        override fun cancel() {

        }
    }

    //
    //  model
    //

    class GlideId(val id: Long) {

        override fun equals(o: Any?): Boolean {
            if(o is GlideId) return id == o.id
            return false

        }

        fun getKey() = "com.sup.dev.android.libs.glade.ImageFid$id"

        override fun hashCode(): Int {
            return getKey().hashCode()
        }

    }

}*/