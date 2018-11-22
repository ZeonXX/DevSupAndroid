package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.AnimationFocus

open class ViewAvatar constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val paint: Paint
    private val animationFocus: AnimationFocus

    val vImageView: ViewCircleImage
    val vChip: ViewChip
    private val vTouch: ViewDraw

    private var roundBackgroundColor: Int = 0

    init {

        SupAndroid.initEditMode(this)
        val focusColor = ToolsResources.getColor(R.color.focus)

        paint = Paint()
        paint.isAntiAlias = true

        val view: View = ToolsView.inflate(context, R.layout.view_avatar)
        vImageView = view.findViewById(R.id.vDevSupImage)
        vChip = view.findViewById(R.id.vDevSupChip)
        vTouch = view.findViewById(R.id.vDevSupAvatarTouch)

        vChip.visibility = View.GONE

        isEnabled = false

        addView(view)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewAvatar, 0, 0)
        val src = a.getResourceId(R.styleable.ViewAvatar_android_src, R.color.blue_700)
        val text = a.getString(R.styleable.ViewAvatar_ViewAvatar_chipText)
        val chipBackground = a.getColor(R.styleable.ViewAvatar_ViewAvatar_chipBackground, 0)
        val srcIcon = a.getResourceId(R.styleable.ViewAvatar_ViewAvatar_chipIcon, 0)
        val iconUseBackground = a.getBoolean(R.styleable.ViewAvatar_ViewAvatar_chipIconUseBackground, false)
        val iconPadding = a.getDimension(R.styleable.ViewAvatar_ViewAvatar_chipIconPadding, 0f)
        val chipSize = a.getDimension(R.styleable.ViewAvatar_ViewAvatar_chipSize, ToolsView.dpToPx(18).toFloat())
        val roundBackgroundColor = a.getColor(R.styleable.ViewAvatar_ViewAvatar_avatarBackground, 0x00000000)
        a.recycle()

        animationFocus = AnimationFocus(vTouch, focusColor)

        setImage(src)
        vChip.setSize(ToolsView.pxToDp(chipSize))
        vChip.setIconPadding(ToolsView.pxToDp(iconPadding))
        vChip.setIcon(srcIcon)
        vChip.setText(text)
        vChip.setUseIconBackground(iconUseBackground)
        if (chipBackground != 0) vChip.setChipBackground(chipBackground)

        vTouch.setOnDraw { canvas ->
            paint.color = animationFocus.update()
            canvas.drawCircle(vTouch.width / 2f, vTouch.height / 2f, vTouch.height / 2f, paint)
        }

        setRoundBackgroundColor(roundBackgroundColor)
    }

    fun updateChipVisible() {

        if (!vChip.hasIcon() && vChip.getText().isEmpty())
            vChip.visibility = View.GONE
        else
            vChip.visibility = View.VISIBLE
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = roundBackgroundColor
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (Math.min(width, height) / 2).toFloat(), paint)
        super.onDraw(canvas)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        if (params.width == FrameLayout.LayoutParams.WRAP_CONTENT && params.height == FrameLayout.LayoutParams.WRAP_CONTENT) {
            params.width = ToolsView.dpToPx(48)
            params.height = ToolsView.dpToPx(48)
        }

        if (params.width > 0 && params.height == FrameLayout.LayoutParams.WRAP_CONTENT)
            params.height = params.width
        if (params.height > 0 && params.width == FrameLayout.LayoutParams.WRAP_CONTENT) params.width = params.height

        super.setLayoutParams(params)

    }

    //
    //  Setters
    //


    fun setCircleBackgroundColorResource(@ColorRes roundBackgroundColorRes: Int) {
        setRoundBackgroundColor(ToolsResources.getColor(roundBackgroundColorRes))
    }

    fun setRoundBackgroundColor(roundBackgroundColor: Int) {
        this.roundBackgroundColor = roundBackgroundColor
        setWillNotDraw(roundBackgroundColor == 0x00000000)
        invalidate()
    }

    fun setChipText(t: String) {
        vChip.setText(t)
        updateChipVisible()
    }

    fun setChipIcon(@DrawableRes icon: Int) {
        vChip.setIcon(icon)
        updateChipVisible()
    }


    override fun setOnClickListener(l: View.OnClickListener?) {
        vTouch.setOnClickListener(l)
        vTouch.isClickable = l != null
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        vImageView.isEnabled = enabled
    }

    override fun setClickable(clickable: Boolean) {
        super.setClickable(clickable)
        vImageView.isClickable = clickable
    }

    fun setImage(@DrawableRes image: Int) {
        if (image != 0)
            vImageView.setImageResource(image)
        else
            vImageView.setImageBitmap(null)

    }

    fun setImage(bitmap: Bitmap?) {
        vImageView.setImageBitmap(bitmap)
    }

    //
    //  Getters
    //

    fun getText() = vChip.getText()


}
