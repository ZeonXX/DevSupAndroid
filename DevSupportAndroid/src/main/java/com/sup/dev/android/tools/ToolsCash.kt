package com.sup.dev.android.tools

import android.content.Context
import android.util.Pair
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.libs.debug.Debug
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object ToolsCash{

    private val SPLITTER = "_"

    private var cashSize: Long = 0
    private var overClearSize: Long = 0
    private var orderClearMaxCount: Int = 0

    fun init() {
        init((1024 * 1024 * 20).toLong())
    }

    fun init(cashSize: Long) {
        init(cashSize, cashSize / 2)
    }

    fun init(cashSize: Long, overClearSize: Long) {
        init(cashSize, overClearSize, 1000)
    }

    fun init(cashSize: Long, overClearSize: Long, orderClearMaxCount: Int) {
        if (cashSize != 0L) throw RuntimeException("Already init")
        ToolsCash.cashSize = cashSize
        ToolsCash.overClearSize = overClearSize
        ToolsCash.orderClearMaxCount = orderClearMaxCount
    }

    fun cacheData(data: ByteArray, name: String) {
        if (cashSize == 0L) init()

        val cacheDir = SupAndroid.appContext!!.cacheDir
        val size = getDirSize(cacheDir)
        val newSize = data.size + size

        if (newSize > cashSize) cleanDir(cacheDir, newSize - cashSize + overClearSize)

        val file = File(cacheDir, System.currentTimeMillis().toString() + SPLITTER + name)

        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(file)
            os.write(data)
            os.flush()
        } catch (ex: IOException) {
            Debug.log(ex)
        } finally {
            if (os != null)
                try {
                    os.close()
                } catch (e: IOException) {
                    Debug.log(e)
                }

        }
    }

    fun getData(context: Context, name: String): ByteArray? {
        if (cashSize == 0L) init()

        val cacheDir = context.cacheDir
        val file = File(cacheDir, name)

        if (!file.exists()) return null


        val data = ByteArray(file.length().toInt())
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(file)
            inputStream.read(data)
        } catch (ex: IOException) {
            Debug.log(ex)
        } finally {
            try {
                inputStream!!.close()
            } catch (e: IOException) {
                Debug.log(e)
            }

        }
        return data
    }

    private fun cleanDir(dir: File, bytes: Long) {
        if (cashSize == 0L) init()

        var bytesDeleted: Long = 0
        val files = dir.listFiles()

        if (files!!.size <= orderClearMaxCount) {

            val keys = ArrayList<Pair<Long, File>>()
            val sorted = ArrayList<Long>()

            for (file in files) {
                val date = getDate(file.name)
                sorted.add(date)
                keys.add(Pair(date, file))
            }

            Collections.sort(sorted)

            for (i in sorted.size - 1 downTo -1 + 1) {
                for (pair in keys)
                    if (pair.first.toLong() == sorted[i]) {
                        keys.remove(pair)
                        if (pair.second.delete())
                            bytesDeleted += pair.second.length()
                        break
                    }
                if (bytesDeleted >= bytes) break
            }
        } else {
            for (file in files) {
                if (file.delete()) bytesDeleted += file.length()
                if (bytesDeleted >= bytes) break
            }
        }
    }

    private fun getDirSize(dir: File): Long {
        if (cashSize == 0L) init()

        var size: Long = 0
        val files = dir.listFiles()

        for (file in files!!)
            if (file.isFile)
                size += file.length()

        return size
    }

    private fun getDate(name: String): Long {
        if (cashSize == 0L) init()
        return java.lang.Long.parseLong(name.substring(0, name.indexOf(SPLITTER)))
    }

}