package com.sup.dev.android.views.views

import android.content.Context
import androidx.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.models.EventStyleChanged
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.java.libs.eventBus.EventBus
import com.sup.dev.java.tools.ToolsThreads


open class ViewAvatarTitle constructor(context: Context, attrs: AttributeSet? = null) : LayoutCorned(context, attrs) {

    private val eventBus = EventBus.subscribe(EventStyleChanged::class) { ToolsThreads.main(true) { updateCorned() } }

    val vAvatar: ViewAvatar
    val vTitle: ViewTextLinkable
    val vSubtitle: ViewTextLinkable

    private var chipModeAvatar = true

    init {

        setWillNotDraw(false)

        SupAndroid.initEditMode(this)

        val view: View = ToolsView.inflate(context, R.layout.view_avatar_title)

        vAvatar = view.findViewById(R.id.vDevSupAvatar)
        vTitle = view.findViewById(R.id.vDevSupTitle)
        vSubtitle = view.findViewById(R.id.vDevSupSubtitle)

        addView(view)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewAvatarTitle, 0, 0)
        val src = a.getResourceId(R.styleable.ViewAvatarTitle_android_src, 0)
        val srcIcon = a.getResourceId(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipIcon, 0)
        val chipText = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipText)
        val chipBackground = a.getColor(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipBackground, ToolsResources.getAccentColor(context))
        val mText = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_title)
        val mSubtitle = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_subtitle)
        val iconSizePadding = a.getDimension(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipIconPadding, 0f)
        val chipSize = a.getDimension(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipSize, ToolsView.dpToPx(18))
        val roundBackgroundColor = a.getColor(R.styleable.ViewAvatarTitle_ViewAvatarTitle_avatarBackground, 0x00000000)
        val avatarPadding = a.getDimension(R.styleable.ViewAvatarTitle_ViewAvatarTitle_avatarPadding, 0f).toInt()
        chipModeAvatar = a.getBoolean(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipMode, chipModeAvatar)
        a.recycle()

        vAvatar.setImage(src)
        vAvatar.vImageView.setBackgroundColorCircle(roundBackgroundColor)
        vAvatar.setPadding(avatarPadding, avatarPadding, avatarPadding, avatarPadding)
        vAvatar.setChipSize(chipSize.toInt())
        vAvatar.setChipText(chipText)
        vAvatar.setChipIconPadding(iconSizePadding.toInt())
        vAvatar.setChipIcon(srcIcon)
        vAvatar.setChipBackground(chipBackground)

        setTitle(mText)
        setSubtitle(mSubtitle)
        updateCorned()
    }

    fun updateCorned() {
        if (vAvatar.vImageView.isSquareMode()) {
            setChipMode(false)
            if (getCornedSize() != 0f) setCornedSizePx(vAvatar.vImageView.getSquareCorned().toInt())
            setCornedBL(chipModeAvatar && hasOnClickListeners())
            setCornedBR(chipModeAvatar && hasOnClickListeners())
            setCornedTL(chipModeAvatar && hasOnClickListeners())
            setCornedTR(chipModeAvatar && hasOnClickListeners())
        } else {
            setChipMode(chipModeAvatar && hasOnClickListeners())
            setCornedBL(chipModeAvatar && hasOnClickListeners())
            setCornedBR(chipModeAvatar && hasOnClickListeners())
            setCornedTL(chipModeAvatar && hasOnClickListeners())
            setCornedTR(chipModeAvatar && hasOnClickListeners())
        }
        vSubtitle.maxLines = if (hasOnClickListeners()) 2 else 10000
    }

    //
    //  Setters
    //

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        isClickable = l != null
        updateCorned()
    }

    fun setTitle(@StringRes text: Int) {
        vTitle.setText(text)
    }

    fun setTitle(text: String?) {
        vTitle.text = text
    }

    fun setSubtitle(@StringRes text: Int) {
        setSubtitle(ToolsResources.s(text))
    }

    fun setSubtitle(text: String?) {
        vSubtitle.visibility = if (text == null || text.isEmpty()) GONE else VISIBLE
        vSubtitle.text = text
    }

    fun setTitleColor(color: Int) {
        vTitle.setTextColor(color)
    }

    fun setSubtitleColor(color: Int) {
        vSubtitle.setTextColor(color)
    }

    //
    //  Getters
    //


    fun getTitle() = vTitle.text.toString()

    fun getSubTitle() = vSubtitle.text.toString()

}
