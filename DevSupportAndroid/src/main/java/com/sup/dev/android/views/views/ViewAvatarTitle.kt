package com.sup.dev.android.views.views

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned


class ViewAvatarTitle constructor(context: Context, attrs: AttributeSet? = null) : LayoutCorned(context, attrs) {

    val vAvatar: ViewAvatar
    val vTitle: ViewTextLinkable
    val vSubtitle: ViewTextLinkable

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
        a.recycle()


        vAvatar.setImage(src)
        vAvatar.setRoundBackgroundColor(roundBackgroundColor)
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
        setChipMode(hasOnClickListeners())
        setCornedBL(hasOnClickListeners())
        setCornedBR(hasOnClickListeners())
        setCornedTL(hasOnClickListeners())
        setCornedTR(hasOnClickListeners())
        vSubtitle.maxLines = if(hasOnClickListeners()) 2 else 10000
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
        vSubtitle.setText(text)
    }

    fun setSubtitle(text: String?) {
        vSubtitle.visibility = if (text == null || text.isEmpty()) GONE else VISIBLE
        vSubtitle.text = text
    }

    //
    //  Getters
    //


    fun getTitle() = vTitle.text.toString()

    fun getSubTitle() = vSubtitle.text.toString()

}
