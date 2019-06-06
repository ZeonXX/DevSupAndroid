package com.sup.dev.android.views.widgets


interface WidgetViewWrapper {

    fun <K : WidgetViewWrapper> hideWidget(): K

    fun <K : WidgetViewWrapper> setWidgetCancelable(cancelable: Boolean): K

    fun <K : WidgetViewWrapper> setWidgetEnabled(enabled: Boolean): K

}
