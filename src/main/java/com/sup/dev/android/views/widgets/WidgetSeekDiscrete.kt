package com.sup.dev.android.views.widgets


import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.StringRes
import com.sup.dev.android.R

import com.sup.dev.android.tools.ToolsResources;

class WidgetSeekDiscrete : Widget(R.layout.widget_seek_district), SeekBar.OnSeekBarChangeListener {

    private val vSeekBar: SeekBar = view.findViewById(R.id.seek)
    private val vMin: TextView = view.findViewById(R.id.min)
    private val vCurrent: TextView = view.findViewById(R.id.current)
    private val vMax: TextView = view.findViewById(R.id.max)
    private val vCancel: Button = view.findViewById(R.id.cancel)
    private val vEnter: Button = view.findViewById(R.id.enter)

    private var currentTextMask: (Int) -> String = { "${getProgress()}" }

    init {

        vCurrent.setTextSize(19f)
        vMax.setTextSize(16f)
        vMin.setTextSize(16f)
        vMax.setText(null)
        vMin.setText(null)
        vCurrent.setText(null)

        vSeekBar.setOnSeekBarChangeListener(this)
        updateText()
    }

    fun setCurrentTextMask(currentTextMask: (Int) -> String): WidgetSeekDiscrete {
        this.currentTextMask = currentTextMask
        return this
    }

    fun setMax(max: Int): WidgetSeekDiscrete {
        vSeekBar.max = max;
        if (vMax.text.toString().isEmpty())
            vMax.text = "$max"
        return this
    }

    fun setMaxMask(mask: String): WidgetSeekDiscrete {
        vMax.text = mask
        return this
    }

    fun setMinMask(mask: String): WidgetSeekDiscrete {
        vMin.text = mask
        return this
    }

    fun setProgress(progress:Int):WidgetSeekDiscrete {
        vSeekBar.setProgress(progress)
        updateText()
        return this
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        updateText()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }


    fun updateText() {
        this.vCurrent.text = this.currentTextMask.invoke(getProgress())
    }



    fun getProgress() = vSeekBar.progress


    //
    //  Setters
    //

    override fun setTitle(@StringRes title:Int):WidgetSeekDiscrete {
        return super.setTitle(title) as WidgetSeekDiscrete
    }

    override fun setTitle(title:String?):WidgetSeekDiscrete {
        return super.setTitle(title) as WidgetSeekDiscrete;
    }

    fun setOnCancel(s:String):WidgetSeekDiscrete {
        return setOnCancel(s){}
    }

    fun setOnCancel(@StringRes s:Int):WidgetSeekDiscrete {
        return setOnCancel(s){}
    }

    fun setOnCancel(@StringRes s:Int , onCancel:(WidgetSeekDiscrete)->Unit):WidgetSeekDiscrete {
        return setOnCancel(ToolsResources.s(s), onCancel)
    }

    fun setOnCancel(s:String, onCancel:(WidgetSeekDiscrete)->Unit):WidgetSeekDiscrete {
        vCancel.text = s
        vCancel.setOnClickListener {
            hide()
            onCancel.invoke(this)
        }
        return this
    }

    fun setOnEnter(@StringRes s:Int):WidgetSeekDiscrete{
        return setOnEnter(s){w,i->}
    }

    fun setOnEnter(s:String):WidgetSeekDiscrete {
        return setOnEnter(s){w,i->}
    }

    fun setOnEnter(@StringRes s:Int, onEnter:(WidgetSeekDiscrete, Int)->Unit):WidgetSeekDiscrete {
        return setOnEnter(ToolsResources.s(s), onEnter)
    }

    fun setOnEnter(s:String, onEnter:(WidgetSeekDiscrete, Int)->Unit):WidgetSeekDiscrete {
        vEnter.text = s
        vEnter.setOnClickListener {
            hide()
            onEnter.invoke(this, getProgress())
        }
        return this
    }

    override fun setEnabled(enabled:Boolean):WidgetSeekDiscrete {
        vEnter.isEnabled = enabled
        vCancel.isEnabled = enabled
        vSeekBar.isEnabled = enabled
        return super.setEnabled(enabled) as WidgetSeekDiscrete
    }

}
