package com.sup.dev.android.views.settings

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.views.views.ViewIcon


class SettingsSeek @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs, R.layout.settings_seek), SeekBar.OnSeekBarChangeListener {

    private val vIcon: ViewIcon
    private val vTitle: TextView
    private val vSubtitle: TextView
    private val vSeekBar: SeekBar

    private var onProgressChanged: ((Int) -> Unit)? = null

    //
    //  Getters
    //

    var progress: Int
        get() = vSeekBar.progress
        set(progress) {
            vSeekBar.progress = progress
        }


    //
    //  Dpad
    //

    private var dpadStep = 1

    init {

        vIcon = view.findViewById(R.id.dev_sup_icon)
        vTitle = view.findViewById(R.id.dev_sup_title)
        vSubtitle = view.findViewById(R.id.dev_sup_subtitle)
        vSeekBar = view.findViewById(R.id.dev_sup_seek_bar)

        vSeekBar.id = View.NO_ID  //   Чтоб система не востонавливала состояние

        val a = context.obtainStyledAttributes(attrs, R.styleable.SettingsSeek, 0, 0)
        val lineVisible = a.getBoolean(R.styleable.SettingsSeek_SettingsSeek_lineVisible, true)
        val title = a.getString(R.styleable.SettingsSeek_SettingsSeek_title)
        val subtitle = a.getString(R.styleable.SettingsSeek_SettingsSeek_subtitle)
        val icon = a.getResourceId(R.styleable.SettingsSeek_SettingsSeek_icon, 0)
        val maxProgress = a.getInteger(R.styleable.SettingsSeek_SettingsSeek_maxProgress, 100)
        var progress = a.getInteger(R.styleable.SettingsSeek_SettingsSeek_progress, 70)
        val iconBackground = a.getResourceId(R.styleable.SettingsAction_SettingsAction_icon_background, 0)
        a.recycle()

        vSeekBar.setOnSeekBarChangeListener(this)

        setLineVisible(lineVisible)
        setTitle(title)
        setSubtitle(subtitle)
        setIcon(icon)
        setMaxProgress(maxProgress)
        setIconBackground(iconBackground)
    }

    //
    //  State
    //

    public override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState())
        bundle.putInt("progress", vSeekBar.progress)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle) {
            val bundle = state as Bundle?
            progress = bundle!!.getInt("progress")
            state = bundle.getParcelable("SUPER_STATE")
        }
        super.onRestoreInstanceState(state)
    }

    //
    //  Setters
    //

    fun setTitle(@StringRes titleRes: Int) {
        setTitle(context.getString(titleRes))
    }

    fun setTitle(title: String?) {
        vTitle.text = title
        vTitle.visibility = if (title != null && !title.isEmpty()) View.VISIBLE else View.GONE
    }

    fun setSubtitle(@StringRes subtitleRes: Int) {
        setSubtitle(context.getString(subtitleRes))
    }

    fun setSubtitle(subtitle: String?) {
        vSubtitle.text = subtitle
        vSubtitle.visibility = if (subtitle != null && !subtitle.isEmpty()) View.VISIBLE else View.GONE
    }

    fun setIcon(@DrawableRes icon: Int) {
        if (icon == 0)
            vIcon.setImageBitmap(null)
        else
            vIcon.setImageResource(icon)
        vIcon.visibility = if (icon == 0) View.GONE else View.VISIBLE
    }

    fun setIconBackground(color: Int) {
        vIcon.setIconBackgroundColor(color)
    }

    fun setMaxProgress(max: Int) {

        vSeekBar.max = max
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        vSeekBar.isEnabled = enabled
        vTitle.isEnabled = enabled
        vSubtitle.isEnabled = enabled
    }

    fun setOnProgressChanged(onProgressChanged: (Int) -> Unit) {
        this.onProgressChanged = onProgressChanged
    }

    //
    //  Events
    //

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (onProgressChanged != null)
            onProgressChanged!!.invoke(progress)
    }

}
