package com.sup.dev.android.views.widgets

import android.graphics.Bitmap
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsStorage
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewCircleImage
import com.sup.dev.java.tools.ToolsText


class WidgetAlert : Widget(R.layout.widget_alert) {

    private val vCheck: CheckBox = findViewById(R.id.vCheckBox)
    private val vEnter: Button = findViewById(R.id.vEnter)
    private val vCancel: Button = findViewById(R.id.vCancel)
    private val vText: TextView = findViewById(R.id.vText)
    private val vTopContainer: ViewGroup = findViewById(R.id.vTopContainer)
    private val vTopImage: ViewCircleImage = findViewById(R.id.vTopImage)
    private val vTopTitle: TextView = findViewById(R.id.vTopTitle)

    private var key: String? = null
    private var lockUntilAccept: Boolean = false
    private var autoHideOnEnter = true

    init {

        isUseMoreScreenSpace = true
        vText.text = ""

        vText.visibility = View.GONE
        vCancel.visibility = View.GONE
        vEnter.visibility = View.GONE
        vCheck.visibility = View.GONE
        vTopContainer.visibility = View.GONE
        vTopImage.visibility = View.GONE
        vTopTitle.visibility = View.GONE

        vText.setTextIsSelectable(true)

        vCheck.setOnCheckedChangeListener { compoundButton, b -> updateLock(vEnter, vCheck) }
    }

    private fun updateLock(vEnter: Button, vCheck: CheckBox) {
        if (lockUntilAccept) vEnter.isEnabled = vCheck.isChecked
    }

    //
    //  Setters
    //

    override fun setCancelable(cancelable: Boolean): WidgetAlert {
        super.setCancelable(cancelable)
        return this
    }

    override fun setEnabled(enabled: Boolean): WidgetAlert {
        super.setEnabled(enabled)
        vCheck.isEnabled = enabled
        vEnter.isEnabled = enabled
        vCancel.isEnabled = enabled
        vText.isEnabled = enabled
        vTopContainer.isEnabled = enabled
        vTopImage.isEnabled = enabled
        vTopTitle.isEnabled = enabled
        return this
    }

    override fun setTitle(title: Int): WidgetAlert {
        super.setTitle(title)
        return this
    }

    override fun setTitle(title: String?): WidgetAlert {
        super.setTitle(title)
        return this
    }

    fun setLockUntilAccept(lockUntilAccept: Boolean): WidgetAlert {
        this.lockUntilAccept = lockUntilAccept
        return this
    }

    fun setChecker(key: String, @StringRes text: Int): WidgetAlert {
        return setChecker(key, ToolsResources.s(text))
    }

    @JvmOverloads
    fun setChecker(key: String, text: String? = SupAndroid.TEXT_APP_DONT_SHOW_AGAIN): WidgetAlert {
        this.key = key
        vCheck.text = text
        vCheck.visibility = View.VISIBLE
        return this
    }


    fun setTitleImageBackgroundRes(@ColorRes res: Int): WidgetAlert {
        return setImageBackground(ToolsResources.getColor(res))
    }

    fun setImageBackground(@ColorInt color: Int): WidgetAlert {
        vTopContainer.setBackgroundColor(color)
        return this
    }

    fun setTitleImage(image: Int): WidgetAlert {
        vTopContainer.visibility = if (image > 0) View.VISIBLE else View.GONE
        ToolsView.setImageOrGone(vTopImage, image)
        return this
    }

    fun setTitleImage(image: Bitmap?): WidgetAlert {
        vTopContainer.visibility = if (image != null) View.VISIBLE else View.GONE
        ToolsView.setImageOrGone(vTopImage, image)
        return this
    }

    fun setTitleImageTopPadding(padding: Float): WidgetAlert {
        vTopImage.setPadding(vTopImage.paddingLeft, padding.toInt(), vTopImage.paddingRight, vTopImage.paddingBottom)
        return this
    }

    fun setTitleImage(setter:(ViewCircleImage)->Unit): WidgetAlert {
        vTopContainer.visibility = View.VISIBLE
        vTopImage.visibility = View.VISIBLE
        setter.invoke(vTopImage)
        return this
    }

    fun setTopTitleText(@StringRes topTitle: Int): WidgetAlert {
        return setTopTitleText(ToolsResources.s(topTitle))
    }

    fun setTopTitleText(topTitle: String?): WidgetAlert {
        vTopContainer.visibility = if (ToolsText.empty(topTitle)) View.GONE else View.VISIBLE
        ToolsView.setTextOrGone(vTopTitle, topTitle)
        return this
    }

    fun addLine(text: Int) = addLine(ToolsResources.s(text))

    fun addLine(text: String): WidgetAlert {
        vText.text = vText.text.toString() + "\n" + text
        vText.visibility = View.VISIBLE
        return this
    }

    fun setText(@StringRes text: Int): WidgetAlert {
        return setText(ToolsResources.s(text))
    }

    fun setText(text: CharSequence?): WidgetAlert {
        ToolsView.setTextOrGone(vText, text)
        return this
    }

    fun setTextGravity(gravity: Int): WidgetAlert {
        vText.gravity = gravity
        return this
    }

    fun setOnEnter(@StringRes s: Int): WidgetAlert {
        return setOnEnter(ToolsResources.s(s))
    }

    fun setOnEnter(@StringRes s: Int, onEnter: (WidgetAlert) -> Unit): WidgetAlert {
        return setOnEnter(ToolsResources.s(s), onEnter)
    }

    @JvmOverloads
    fun setOnEnter(s: String?, onEnter: (WidgetAlert) -> Unit = {}): WidgetAlert {
        ToolsView.setTextOrGone(vEnter, s)
        vEnter.setOnClickListener { v ->
            if (autoHideOnEnter)
                hide()
            else
                setEnabled(false)
            if (key != null) ToolsStorage.put(key!!, vCheck.isChecked)
            onEnter.invoke(this)
        }
        return this
    }

    fun setAutoHideOnEnter(autoHideOnEnter: Boolean): WidgetAlert {
        this.autoHideOnEnter = autoHideOnEnter
        return this
    }

    fun setOnCancel(@StringRes s: Int): WidgetAlert {
        return setOnCancel(ToolsResources.s(s), {})
    }

    fun setOnCancel(onCancel: (WidgetAlert) -> Unit): WidgetAlert {
        return setOnCancel(null, onCancel)
    }

    fun setOnCancel(@StringRes s: Int, onCancel: (WidgetAlert) -> Unit): WidgetAlert {
        return setOnCancel(ToolsResources.s(s), onCancel)
    }

    @JvmOverloads
    fun setOnCancel(s: String?, onCancel: (WidgetAlert) -> Unit = {}): WidgetAlert {
        super.setOnHide{ b -> onCancel.invoke(this) }
        ToolsView.setTextOrGone(vCancel, s)
        vCancel.setOnClickListener { v ->
            hide()
            onCancel.invoke(this)
        }

        return this
    }

    companion object {

        fun check(key: String): Boolean {
            return ToolsStorage.getBoolean(key, false)
        }

        fun clear(key: String) {
            ToolsStorage.clear(key)
        }
    }

}
