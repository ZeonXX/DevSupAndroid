package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.sup.dev.android.R
import com.sup.dev.android.libs.image_loader.ImageLoader
import com.sup.dev.android.libs.image_loader.ImageLoaderId
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.screens.SImageView
import com.sup.dev.android.views.views.layouts.LayoutAspectRatio


class ViewGifImage @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val view: LayoutAspectRatio
    private val vGif: ViewGif
    private val vImage: ImageView
    private val vProgress: ProgressBar
    private val vFade: View
    private val vTouch: View
    private val vIcon: ViewIcon

    private var imageId: Long = 0
    private var gifId: Long = 0

    private var customImageControl: Boolean = false
    private var image: ByteArray? = null
    private var gif: ByteArray? = null
    private var callbackImage: ((ImageView, (ByteArray?)->Unit) -> Unit)? = null
    private var callbackGif: ((ByteArray?) -> Unit)? = null
    private var onClickListener: View.OnClickListener? = null

    init {

        view = ToolsView.inflate(context, R.layout.view_gif)
        vGif = view.findViewById(R.id.view_gif_gif)
        vImage = view.findViewById(R.id.view_gif_image)
        vProgress = view.findViewById(R.id.view_gif_progress)
        vFade = view.findViewById(R.id.view_gif_fade)
        vIcon = view.findViewById(R.id.view_gif_icon)
        vTouch = view.findViewById(R.id.view_gif_touch)

        vTouch.setOnClickListener { v -> onClick() }
        vTouch.setOnLongClickListener { v ->
            if (onClickListener != null)
                onClickListener!!.onClick(v)
            else if (gifId != 0L || imageId != 0L)
                Navigator.to(SImageView(if (gifId == 0L) imageId else gifId, gifId != 0L))
            else false
            true
        }

        addView(view)
    }

    override fun setClickable(clickable: Boolean) {
        vTouch.visibility = if (clickable) View.VISIBLE else View.GONE
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        this.onClickListener = onClickListener
    }

    override fun setOnTouchListener(l: View.OnTouchListener) {
        vTouch.setOnTouchListener(l)
    }

    private fun onClick() {

        if (callbackImage != null && image == null) {
            loadImage()
            return
        }

        if (callbackGif != null) {
            if (gif == null)
                loadGif()
            else if (vGif.isPaused)
                play()
            else
                pause()
        } else {
            if (imageId != 0L)
                Navigator.to(SImageView(imageId))
            else if (onClickListener != null) onClickListener!!.onClick(this)
        }

    }

    fun pause() {
        vFade.visibility = View.VISIBLE
        vIcon.visibility = View.VISIBLE
        vIcon.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        vGif.pause()
    }

    fun play() {
        vProgress.visibility = View.GONE
        vImage.visibility = View.GONE
        vFade.visibility = View.GONE
        vIcon.visibility = View.GONE
        vGif.visibility = View.VISIBLE
        vGif.play()
    }

    fun setImageBitmap(bitmap: Bitmap) {
        callbackImage = null
        callbackGif = null
        vProgress.visibility = View.GONE
        vFade.visibility = View.GONE
        vIcon.visibility = View.GONE
        vGif.visibility = View.GONE
        vImage.visibility = View.VISIBLE
        vImage.setImageBitmap(bitmap)
    }

    fun clear() {
        onClickListener = null
        customImageControl = false
        callbackImage = null
        callbackGif = null
        image = null
        gif = null
        vGif.clear()
        vImage.setImageBitmap(null)
        vGif.visibility = View.GONE
        vFade.visibility = View.VISIBLE
        vIcon.visibility = View.GONE
    }

    fun setImageLoader(callbackImage: (ImageView, (ByteArray?)->Unit) -> Unit) {
        this.callbackImage = callbackImage
    }

    fun setGifLoader(callbackGif: (ByteArray?) -> Unit) {
        this.callbackGif = callbackGif
    }

    fun loadImage() {
        val callback = this.callbackImage
        vImage.setImageBitmap(null)
        vImage.visibility = View.VISIBLE
        vFade.visibility = View.GONE
        vProgress.visibility = View.GONE
        vIcon.visibility = View.GONE
        callback!!.invoke(vImage) { image ->
            if (callback === this.callbackImage) {
                this.image = image
                if (image == null) {
                    vFade.visibility = View.VISIBLE
                    vIcon.visibility = View.VISIBLE
                    vIcon.setImageResource(R.drawable.ic_refresh_white_24dp)
                } else {
                    vFade.visibility = View.GONE
                    vProgress.visibility = View.GONE
                    if (!customImageControl) vImage.setImageBitmap(ToolsBitmap.decode(image))
                    loadGif()
                }
            }
        }
    }

    fun loadGif() {
        if (callbackGif == null) return
        val callback  : ((ByteArray?) -> Unit)?= this.callbackGif
        vImage.visibility = View.VISIBLE
        vProgress.visibility = View.VISIBLE
        vFade.visibility = View.VISIBLE
        vIcon.visibility = View.GONE



        /*
        callback.callback(gif -> {
            if (callback != this.callbackGif) return;
            this.gif = gif;
            if (gif == null) {
                vFade.setVisibility(VISIBLE);
                vIcon.setVisibility(VISIBLE);
                vIcon.setImageResource(R.drawable.ic_refresh_white_24dp);
            } else {
                vGif.setOnGifLoaded(this::play);
                vGif.setGif(gif);
            }
        });
         */

        callback!!.invoke({ gif ->
            if (callback === this.callbackGif) {
                this.gif = gif
                if (gif == null) {
                    vFade.visibility = View.VISIBLE
                    vIcon.visibility = View.VISIBLE
                    vIcon.setImageResource(R.drawable.ic_refresh_white_24dp)
                } else {
                    vGif.setOnGifLoaded { this.play() }
                    vGif.setGif(gif!!)
                }
            }
        })
    }

    fun setRatio(w: Int, h: Int) {
        view.setRatio(w, h)
    }

    fun setCustomImageControl(customImageControl: Boolean) {
        this.customImageControl = customImageControl
    }

    fun setImageId(imageId: Long) {
        this.imageId = imageId
    }

    fun setGifId(gifId: Long) {
        this.gifId = gifId
    }

    @JvmOverloads
    fun init(imageId: Long, gifId: Long, w: Int = 0, h: Int = 0, ignoreRation: Boolean = false) {
        clear()
        setCustomImageControl(true)
        if (!ignoreRation) setRatio(w, h)
        setImageLoader { vImageGif, callbackImage -> ImageLoader.load(ImageLoaderId(imageId).sizes(w, h).setImage(vImageGif).setOnLoaded(callbackImage)) }
        if (gifId != 0L)
            setGifLoader { callbackGif -> ImageLoader.load(ImageLoaderId(gifId).setOnLoaded(callbackGif)) }

        if (imageId != 0L)
            loadImage()
        else if (gifId != 0L) loadGif()

        setImageId(imageId)
        setGifId(gifId)
    }

}
