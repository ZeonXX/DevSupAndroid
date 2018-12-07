package com.sup.dev.android.tools

import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.libs.debug.error
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object ToolsCash {

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
        if (ToolsCash.cashSize != 0L) throw RuntimeException("Already init")
        ToolsCash.cashSize = cashSize
        ToolsCash.overClearSize = overClearSize
        ToolsCash.orderClearMaxCount = orderClearMaxCount
    }

    fun put(data: ByteArray, name: String) {
        if (cashSize == 0L) init()

        val cacheDir = SupAndroid.appContext!!.cacheDir

        val file = File(cacheDir, name)

        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(file)
            os.write(data)
            os.flush()
        } catch (ex: IOException) {
            error(ex)
        } finally {
            if (os != null)
                try {
                    os.close()
                } catch (e: IOException) {
                    error(e)
                }

        }
    }

    fun get(name: String): ByteArray? {
        if (cashSize == 0L) init()

        val cacheDir = SupAndroid.appContext!!.cacheDir
        val file = File(cacheDir, name)

        if (!file.exists()) return null


        val data = ByteArray(file.length().toInt())
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(file)
            inputStream.read(data)
        } catch (ex: IOException) {
            error(ex)
        } finally {
            try {
                inputStream!!.close()
            } catch (e: IOException) {
                error(e)
            }

        }
        return data
    }

    fun clear(name: String) {
        File(SupAndroid.appContext!!.cacheDir, name).delete()
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

}