package com.sup.dev.android.views.widgets


interface WidgetViewWrapper<K> {

    fun hide(): K

    fun setWidgetCancelable(cancelable: Boolean): K

    fun setWidgetEnabled(enabled: Boolean): K

}
