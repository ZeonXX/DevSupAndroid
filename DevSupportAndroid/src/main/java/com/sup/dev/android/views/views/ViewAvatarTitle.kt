package com.sup.dev.android.views.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.AnimationFocus
import com.sup.dev.android.views.views.layouts.LayoutCorned


class ViewAvatarTitle constructor(context: Context, attrs: AttributeSet? = null) : LayoutCorned(context, attrs) {

    private val animationFocus: AnimationFocus
    private val paint: Paint
    private val path: Path

    val viewAvatar: ViewAvatar
    val vTitle: TextView
    val viewSubtitle: TextView

    init {

        setWillNotDraw(false)

        SupAndroid.initEditMode(this)

        val focusColor = ToolsResources.getColor(R.color.focus)

        path = Path()
        paint = Paint()
        paint.isAntiAlias = true

        val view: View = ToolsView.inflate(context, R.layout.view_avatar_title)

        viewAvatar = view.findViewById(R.id.vDevSupAvatar)
        vTitle = view.findViewById(R.id.vDevSupTitle)
        viewSubtitle = view.findViewById(R.id.vDevSupSubtitle)

        addView(view)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewAvatarTitle, 0, 0)
        val src = a.getResourceId(R.styleable.ViewAvatarTitle_android_src, R.color.blue_700)
        val srcIcon = a.getResourceId(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipIcon, 0)
        val chipText = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipText)
        val chipBackground = a.getColor(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipBackground, ToolsResources.getAccentColor(context))
        val mText = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_title)
        val mSubtitle = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_subtitle)
        val iconPadding = a.getDimension(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipIconPadding, 0f)
        val chipSize = a.getDimension(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipSize, ToolsView.dpToPx(18).toFloat())
        val roundBackgroundColor = a.getColor(R.styleable.ViewAvatarTitle_ViewAvatarTitle_avatarBackground, 0x00000000)
        val avatarPadding = a.getDimension(R.styleable.ViewAvatarTitle_ViewAvatarTitle_avatarPadding, 0f).toInt()
        val corned = a.getBoolean(R.styleable.ViewAvatarTitle_ViewAvatarTitle_cornedEnabled, true)
        a.recycle()

        setChipMode(corned)
        setCornedBL(corned)
        setCornedBR(corned)
        setCornedTL(corned)
        setCornedTR(corned)

        animationFocus = AnimationFocus(this, focusColor)

        viewAvatar.setImage(src)
        viewAvatar.setRoundBackgroundColor(roundBackgroundColor)
        viewAvatar.setPadding(avatarPadding, avatarPadding, avatarPadding, avatarPadding)
        viewAvatar.setChipSizePx(chipSize.toInt())
        viewAvatar.setChipText(chipText)
        viewAvatar.setChipIconPadding(iconPadding.toInt())
        viewAvatar.setChipIcon(srcIcon)
        viewAvatar.setChipBackground(chipBackground)

        setTitle(mText)
        setSubtitle(mSubtitle)

    }


    protected override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        animationFocus.update()

        paint.color = animationFocus.update()
        canvas.drawPath(path, paint)
    }

    //
    //  Chip
    //

    protected override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val r: Float = height / 2f
        path.reset()

        if (height < width) {
            path.addCircle(r, r, r, Path.Direction.CCW)
            path.addCircle(width - r, r, r, Path.Direction.CCW)
            path.addRect(r, 0f, width - r, height.toFloat(), Path.Direction.CCW)
        } else {
            path.addCircle(width / 2f, height / 2f, width / 2f, Path.Direction.CCW)
        }

    }

    //
    //  Setters
    //

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        isClickable = l != null
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        super.setOnTouchListener(l)
        if (l == null) animationFocus.resetTouchListener()
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        super.setOnFocusChangeListener(l)
        if (l == null) animationFocus.resetOnFocusChangedListener()
    }

    fun setTitle(@StringRes text: Int) {
        vTitle.setText(text)
    }

    fun setTitle(text: String?) {
        vTitle.text = text
    }

    fun setSubtitle(@StringRes text: Int) {
        viewSubtitle.setText(text)
    }

    fun setSubtitle(text: String?) {
        viewSubtitle.visibility = if (text == null || text.isEmpty()) GONE else VISIBLE
        viewSubtitle.text = text
    }

    //
    //  Getters
    //


    fun getTitle() = vTitle.text.toString()

    fun getSubTitle() = viewSubtitle.text.toString()

}
