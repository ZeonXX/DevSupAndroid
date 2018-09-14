package com.sup.dev.android.views.widgets

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsStorage
import com.sup.dev.android.tools.ToolsTextAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.tools.ToolsText


class WidgetAlert : Widget(R.layout.widget_alert) {

    private val vCheck: CheckBox
    private val vEnter: Button
    private val vCancel: Button
    private val vText: TextView
    private val vTopContainer: ViewGroup
    private val vTopImage: ImageView
    private val vTopTitle: TextView

    private var key: String? = null
    private var lockUntilAccept: Boolean = false
    private var autoHideOnEnter = true

    init {
        vText = findViewById(R.id.vText)
        vCancel = findViewById(R.id.cancel)
        vEnter = findViewById(R.id.enter)
        vCheck = findViewById(R.id.check_box)
        vTopContainer = findViewById(R.id.top_container)
        vTopImage = findViewById(R.id.top_image)
        vTopTitle = findViewById(R.id.top_title)

        vText.visibility = View.GONE
        vCancel.visibility = View.GONE
        vEnter.visibility = View.GONE
        vCheck.visibility = View.GONE
        vTopContainer.visibility = View.GONE
        vTopImage.visibility = View.GONE
        vTopTitle.visibility = View.GONE

        vCheck.setOnCheckedChangeListener { compoundButton, b -> updateLock(vEnter, vCheck) }
    }

    private fun updateLock(vEnter: Button, vCheck: CheckBox) {
        if (lockUntilAccept) vEnter.isEnabled = vCheck.isChecked
    }

    //
    //  Setters
    //

    fun setLockUntilAccept(lockUntilAccept: Boolean): WidgetAlert {
        this.lockUntilAccept = lockUntilAccept
        return this
    }

    fun setChecker(key: String, @StringRes text: Int): WidgetAlert {
        return setChecker(key, ToolsResources.getString(text))
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

    fun setTitleText(@StringRes topTitle: Int): WidgetAlert {
        return setTitleText(ToolsResources.getString(topTitle))
    }

    fun setTitleText(topTitle: String?): WidgetAlert {
        vTopContainer.visibility = if (ToolsText.empty(topTitle)) View.GONE else View.VISIBLE
        ToolsView.setTextOrGone(vTopTitle, topTitle!!)
        return this
    }

    fun addLine(text: String): WidgetAlert {
        vText.text = vText.text.toString() + "\n" + text
        vText.visibility = View.VISIBLE
        return this
    }

    fun setText(@StringRes text: Int): WidgetAlert {
        return setText(ToolsResources.getString(text))
    }

    fun setText(text: CharSequence?): WidgetAlert {
        ToolsView.setTextOrGone(vText, text!!)
        return this
    }

    fun setTextGravity(gravity: Int): WidgetAlert {
        vText.gravity = gravity
        return this
    }

    fun setOnEnter(@StringRes s: Int): WidgetAlert {
        return setOnEnter(ToolsResources.getString(s))
    }

    fun setOnEnter(@StringRes s: Int, onEnter: (WidgetAlert) -> Unit): WidgetAlert {
        return setOnEnter(ToolsResources.getString(s), onEnter)
    }

    @JvmOverloads
    fun setOnEnter(s: String?, onEnter: (WidgetAlert) -> Unit = {}): WidgetAlert {
        ToolsView.setTextOrGone(vEnter, s!!)
        vEnter.setOnClickListener { v ->
            if (autoHideOnEnter)
                hide()
            else
                setEnabled<Widget>(false)
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
        return setOnCancel(ToolsResources.getString(s), {})
    }

    fun setOnCancel(onCancel: (WidgetAlert) -> Unit): WidgetAlert {
        return setOnCancel(null, onCancel)
    }

    fun setOnCancel(@StringRes s: Int, onCancel: (WidgetAlert) -> Unit): WidgetAlert {
        return setOnCancel(ToolsResources.getString(s), onCancel)
    }

    @JvmOverloads
    fun setOnCancel(s: String?, onCancel: (WidgetAlert) -> Unit = {}): WidgetAlert {
        super.setOnHide<Widget> { b -> onCancel.invoke(this) }
        ToolsView.setTextOrGone(vCancel, s!!)
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
            ToolsStorage.remove(key)
        }
    }

}
