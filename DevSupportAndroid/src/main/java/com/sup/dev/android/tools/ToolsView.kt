package com.sup.dev.android.tools

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Selection
import android.text.Spannable
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.util.Linkify
import android.util.TypedValue
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.magic_box.AndroidBug5497Workaround
import com.sup.dev.android.views.views.ViewTextLinkable
import com.sup.dev.android.views.widgets.WidgetProgressTransparent
import com.sup.dev.android.views.widgets.WidgetProgressWithTitle
import com.sup.dev.java.classes.items.Item
import com.sup.dev.java.tools.ToolsText
import com.sup.dev.java.tools.ToolsThreads
import java.util.regex.Pattern


object ToolsView {

    val ANIMATION_TIME = 300

    fun onFieldEnterKey(vFiled:EditText, callback:()->Unit){
        vFiled.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode==KeyEvent.KEYCODE_ENTER)) {
                callback.invoke()
                return@OnKeyListener true
            }
            false
        })
    }

    fun makeHalfFullscreen(activity: Activity) {
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun makeFullscreen(activity: Activity) {
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun dontAutoShowKeyboard(window: Window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun setFabEnabledR(vFab: FloatingActionButton, enabled: Boolean, @ColorRes colorEnabled: Int) {
        setFabEnabled(vFab, enabled, ToolsResources.getColor(colorEnabled))
    }

    @JvmOverloads
    fun setFabEnabled(vFab: FloatingActionButton, enabled: Boolean, @ColorInt colorEnabled: Int = ToolsResources.getAccentColor(vFab.context)) {
        vFab.isEnabled = enabled
        setFabColor(vFab, if (enabled) colorEnabled else ToolsResources.getColor(R.color.grey_700))
    }

    fun setFabColorR(vFab: FloatingActionButton, @ColorRes color: Int) {
        setFabColor(vFab, ToolsResources.getColor(color))
    }

    fun setFabColor(vFab: FloatingActionButton, @ColorInt color: Int) {
        vFab.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun makeLinksClickable(vText: ViewTextLinkable) {
        vText.setLinkTextColor(ToolsResources.getAccentColor(vText.context))
        val httpPattern = Pattern.compile("[a-z]+:\\/\\/[^ \\n]*")
        Linkify.addLinks(vText, httpPattern, "")
    }

    fun recyclerHideFabWhenScrollEnd(vRecycler: RecyclerView, vFab: FloatingActionButton) {
        vRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (vRecycler.computeVerticalScrollOffset() != 0 && vRecycler.computeVerticalScrollOffset() + 50 >= vRecycler.computeVerticalScrollRange() - vRecycler.computeVerticalScrollExtent())
                    vFab.hide()
                else
                    vFab.show()
            }

        })
    }

    fun removeFromParent(view: View): View {
        if (view.parent != null) (view.parent as ViewGroup).removeView(view)
        return view
    }

    fun showProgressDialog(): WidgetProgressTransparent {
        val widget = WidgetProgressTransparent().setCancelable(false)
        ToolsThreads.main {  widget.asDialogShow() }
        return widget
    }

    fun showProgressDialog(title: Int): WidgetProgressWithTitle {
        return showProgressDialog(ToolsResources.s(title))
    }

    fun showProgressDialog(title: String?): WidgetProgressWithTitle {
        val widget: WidgetProgressWithTitle = WidgetProgressWithTitle().setTitle(title).setCancelable(false) as WidgetProgressWithTitle
        ToolsThreads.main {  widget.asDialogShow() }
        return widget
    }

    fun setImageOrGone(vImage: ImageView, bitmap: Bitmap?) {
        vImage.setImageBitmap(bitmap)
        if (bitmap != null)
            vImage.visibility = VISIBLE
        else
            vImage.visibility = GONE
    }

    fun setImageOrGone(vImage: ImageView, image: Int) {
        if (image > 0) {
            vImage.setImageResource(image)
            vImage.visibility = VISIBLE
        } else {
            vImage.setImageBitmap(null)
            vImage.visibility = GONE
        }
    }

    fun setTextOrGone(vText: TextView, text: CharSequence?) {
        vText.text = text
        vText.visibility = if (ToolsText.empty(text)) GONE else VISIBLE
    }

    fun setOnClickAndLongClickCoordinates(v: View, onClick: (View, Int, Int, Boolean) -> Unit) {

        val clickScreenX = Item(0)
        val clickScreenY = Item(0)


        v.setOnTouchListener { v1, event ->
            clickScreenX.a = event.x.toInt()
            clickScreenY.a = event.y.toInt()
            false
        }

        v.setOnClickListener { v1 -> onClick.invoke(v, clickScreenX.a, clickScreenY.a, true) }
        v.setOnLongClickListener { v1 ->
            onClick.invoke(v, clickScreenX.a, clickScreenY.a, false)
            true
        }


    }

    fun setOnClickCoordinates(v: View, onClick: (View, Int, Int) -> Unit) {

        val clickScreenX = Item(0)
        val clickScreenY = Item(0)


        v.setOnTouchListener { v1, event ->
            clickScreenX.a = event.x.toInt()
            clickScreenY.a = event.y.toInt()
            false
        }

        v.setOnClickListener { v1 -> onClick.invoke(v, (if (clickScreenX.a == null) 0 else clickScreenX.a)!!, (if (clickScreenY.a == null) 0 else clickScreenY.a)!!) }


    }

    fun setOnLongClickCoordinates(v: View, onClick: (View, Int, Int) -> Unit) {

        val clickScreenX = Item(0)
        val clickScreenY = Item(0)

        v.setOnTouchListener { v1, event ->
            clickScreenX.a = event.x.toInt()
            clickScreenY.a = event.y.toInt()
            false
        }

        v.setOnLongClickListener { v1 ->
            onClick.invoke(v, clickScreenX.a!!, clickScreenY.a!!)
            true
        }
    }

    fun viewPointAsScreenPoint(view: View, x: Int, y: Int): Array<Int> {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        location[0] = location[0] + x
        location[1] = location[1] + y
        return location.toTypedArray()
    }

    fun checkHit(view: View, x: Float, y: Float): Boolean {

        val location = IntArray(2)
        view.getLocationOnScreen(location)

        val l = location[0]
        val t = location[1]
        val r = l + view.width
        val b = t + view.height

        return x >= l && y >= t && x <= r && y <= b
    }

    fun <K : View> inflate(@LayoutRes res: Int): K {
        return LayoutInflater.from(SupAndroid.activity!!).inflate(res, null, false) as K
    }

    fun <K : View> inflate(viewContext: Context, @LayoutRes res: Int): K {
        return LayoutInflater.from(viewContext).inflate(res, null, false) as K
    }

    fun inflate(parent: ViewGroup, @LayoutRes res: Int): View {
        return LayoutInflater.from(parent.context).inflate(res, parent, false)
    }

    fun getRootParent(v: View): ViewGroup? {
        return if (v.parent == null || v.parent !is View)
            v as? ViewGroup
        else
            getRootParent(v.parent as View)
    }

    fun getRootBackground(v: View): Int {
        if (v.background is ColorDrawable) return (v.background as ColorDrawable).color
        return if (v.parent == null || v.parent !is View)
            0
        else
            getRootBackground(v.parent as View)
    }


    fun <K : View> findViewOnParents(v: View, viewId: Int): K? {

        val fView = v.findViewById<K>(viewId)
        if (fView != null) return fView

        return if (v.parent == null || v.parent !is View)
            null
        else
            findViewOnParents(v.parent as View, viewId)
    }

    fun makeTransparentAppBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            AndroidBug5497Workaround.assistActivity(activity)
        }
    }

    fun pxToDp(px: Int) = pxToDp(px.toFloat())

    fun pxToSp(px: Int) = pxToSp(px.toFloat())

    fun dpToPx(dp: Int) = dpToPx(dp.toFloat())

    fun spToPx(sp: Int) = spToPx(sp.toFloat())

    fun pxToDp(px: Float) = px * (px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().displayMetrics))

    fun pxToSp(px: Float) = px * (px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, Resources.getSystem().displayMetrics))

    fun dpToPx(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics)

    fun spToPx(sp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().displayMetrics)


    //
    //  Keyboard
    //

    fun hideKeyboard() {
        val currentFocus = SupAndroid.activity?.currentFocus
        if (currentFocus != null) {
            val imm = SupAndroid.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun showKeyboard(view: View) {
        ToolsThreads.main(350) {
            view.requestFocus()
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    @JvmOverloads
    fun setTextAnimate(v: TextView, text: String, onAlpha: () -> Unit = {}) {

        if (v.text == text) {
            fromAlpha(v)
            return
        }

        toAlpha(v) {
            v.text = text
            onAlpha.invoke()
            fromAlpha(v)
        }
    }


    fun crossfade(inp: View, out: View) {
        fromAlpha(inp)
        toAlpha(out)
    }

    fun clearAnimation(v: View) {
        v.animate().setListener(null)
        if (v.animation != null)
            v.animation.cancel()
    }

    @JvmOverloads
    fun alpha(v: View, toAlpha: Boolean, onAlpha: () -> Unit = {}) {
        if (toAlpha)
            toAlpha(v, onAlpha)
        else
            fromAlpha(v, onAlpha)
    }

    fun fromAlpha(v: View, onFinish: () -> Unit = {}) {
        fromAlpha(v, ANIMATION_TIME, onFinish)
    }

    @JvmOverloads
    fun fromAlpha(v: View, time: Int = ANIMATION_TIME, onFinish: () -> Unit = {}) {

        clearAnimation(v)

        if (v.alpha == 1f && (v.visibility == GONE || v.visibility == View.INVISIBLE))
            v.alpha = 0f

        v.visibility = VISIBLE

        v.animate()
                .alpha(1f)
                .setDuration(time.toLong())
                .setInterpolator(FastOutLinearInInterpolator())
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        onFinish.invoke()
                    }
                })

    }

    fun toAlpha(v: View, onFinish: () -> Unit = {}) {
        toAlpha(v, ANIMATION_TIME, onFinish)
    }

    @JvmOverloads
    fun toAlpha(v: View, time: Int = ANIMATION_TIME, onFinish: () -> Unit = {}) {

        clearAnimation(v)

        v.animate()
                .alpha(0f)
                .setDuration(time.toLong())
                .setInterpolator(LinearOutSlowInInterpolator())
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        v.animate().setListener(null)
                        v.alpha = 1f
                        v.visibility = View.INVISIBLE
                        onFinish.invoke()
                    }
                })
    }

    //
    //  Target alpha
    //

    fun targetAlpha(v: View, alpha: Float) {

        clearAnimation(v)

        val time = (255 / ANIMATION_TIME * Math.abs(v.alpha - alpha)).toLong()

        v.animate()
                .alpha(alpha)
                .setDuration(time).interpolator = LinearOutSlowInInterpolator()
    }

}
