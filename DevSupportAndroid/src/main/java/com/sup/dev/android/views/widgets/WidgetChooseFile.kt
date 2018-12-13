package com.sup.dev.android.views.widgets

import android.content.Context
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsPermission
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.dialogs.DialogSheetWidget
import com.sup.dev.android.views.settings.SettingsAction
import com.sup.dev.android.views.views.ViewIcon
import java.io.File


class WidgetChooseFile : WidgetRecycler() {

    private var currentFolder: File? = null
    private var rootFolder: File? = null
    private var showFolders = true
    private var showFiles = true
    private var canGoInFolder = true
    private var fileTypes: Array<out String> = emptyArray()
    private var onFileSelected: (File) -> Unit = {}
    private var onFolderSelected: (File) -> Unit = {}

    init {

        adapter = RecyclerCardAdapter()
        rootFolder = Environment.getExternalStorageDirectory()
        currentFolder = rootFolder

        vRecycler.layoutManager = LinearLayoutManager(view!!.context)
        vRecycler.itemAnimator = null
        setAdapter<WidgetRecycler>(adapter as RecyclerCardAdapter)
        ToolsPermission.requestReadPermission { resetCards(rootFolder!!) }
    }

    override fun onShow() {
        super.onShow()

        vRecycler.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        if (viewWrapper is DialogSheetWidget)
            vRecycler.layoutParams.height = ToolsView.dpToPx(320).toInt()
    }


    private fun resetCards(file: File) {
        adapter!!.clear()
        if (file.absolutePath != rootFolder!!.absolutePath) adapter!!.add(CardBack(file))
        val files = file.listFiles()
        if (showFolders) for (f in files!!) if (f.isDirectory) adapter!!.add(CardFile(f))
        if (showFiles) for (f in files!!) if (!f.isDirectory && checkType(f)) adapter!!.add(CardFile(f))
    }


    private fun checkType(f: File): Boolean {
        if (fileTypes == null) return true
        for (type in fileTypes!!) {
            val name = f.name.toLowerCase()
            val t = "$type."
            if (name.length > -t.length && name.substring(name.length - t.length) == t) return true
        }
        return false
    }

    //
    //  Setters
    //

    fun setShowFiles(showFiles: Boolean): WidgetChooseFile {
        this.showFiles = showFiles
        resetCards(currentFolder!!)
        return this
    }

    fun setShowFolders(showFolders: Boolean): WidgetChooseFile {
        this.showFolders = showFolders
        resetCards(currentFolder!!)
        return this
    }

    fun setCanGoInFolder(canGoInFolder: Boolean): WidgetChooseFile {
        this.canGoInFolder = canGoInFolder
        resetCards(currentFolder!!)
        return this
    }

    fun setFileTypes(vararg fileTypes: String): WidgetChooseFile {
        this.fileTypes = fileTypes
        return this
    }

    fun setFolder(file: File): WidgetChooseFile {
        currentFolder = file
        resetCards(file)
        return this
    }

    fun setRootFolder(file: File): WidgetChooseFile {
        rootFolder = file
        resetCards(file)
        return this
    }

    fun setOnFileSelected(onFileSelected: (File) -> Unit): WidgetChooseFile {
        this.onFileSelected = onFileSelected
        resetCards(currentFolder!!)
        return this
    }

    fun setOnFolderSelected(onFolderSelected: (File) -> Unit): WidgetChooseFile {
        this.onFolderSelected = onFolderSelected
        resetCards(currentFolder!!)
        return this
    }

    //
    //  Card
    //

    inner class CardBack constructor(val file: File) : Card() {

        override fun getLayout(): Int {
            return 0
        }

        override fun instanceView(context: Context): View {
            return SettingsAction(context)
        }

        override fun bindView(view: View) {
            val settingsAction = view as SettingsAction
            settingsAction.view.setPadding(ToolsView.dpToPx(16).toInt(), 0, ToolsView.dpToPx(16).toInt(), 0)
            settingsAction.setIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)
            settingsAction.setOnClickListener { v -> setFolder(file.parentFile) }
            settingsAction.setTitle(file.name)
        }
    }

    private inner class CardFile constructor(private val file: File) : Card() {
        private var viewIcon: ViewIcon? = null

        override fun getLayout(): Int {
            return 0
        }

        override fun instanceView(context: Context): View {
            return SettingsAction(context)
        }

        private fun getViewIcon(context: Context): ViewIcon {
            if (viewIcon == null) {
                viewIcon = ToolsView.inflate(context, R.layout.z_icon)
                viewIcon!!.setImageResource(R.drawable.ic_done_black_24dp)
                viewIcon!!.setOnClickListener { v ->
                    onFolderSelected.invoke(file)
                    hide()
                }
            }
            return viewIcon!!
        }

        override fun bindView(view: View) {
            val v = view as SettingsAction
            v.view.setPadding(ToolsView.dpToPx(16).toInt(), 0, ToolsView.dpToPx(16).toInt(), 0)
            v.setTitle(file.name)
            v.setIcon(if (file.isDirectory) R.drawable.ic_folder_black_24dp else R.drawable.ic_insert_drive_file_black_24dp)
            v.setSubView(if (file.isDirectory && canGoInFolder) getViewIcon(view.getContext()) else null)

            v.setOnClickListener { v1 ->
                if (file.isDirectory) {
                    if (!canGoInFolder) {
                        onFolderSelected.invoke(file)
                        hide()
                    } else {
                        setFolder(file)
                    }
                } else {
                    onFileSelected.invoke(file)
                    hide()
                }
            }
        }
    }

}
