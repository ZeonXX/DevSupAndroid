package com.sup.dev.android.tools

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Environment
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.libs.json.Json
import com.sup.dev.java.libs.json.JsonArray
import com.sup.dev.java.tools.ToolsThreads
import java.io.File
import java.io.FileOutputStream
import android.content.Intent
import android.net.Uri


object ToolsStorage {

    var externalFileNamePrefix = "f"
    var preferences: SharedPreferences? = null

    @JvmOverloads
    fun init(storageKey: String = "android_app_pref") {
        preferences = SupAndroid.appContext!!.getSharedPreferences(storageKey, Activity.MODE_PRIVATE)
    }

    operator fun contains(key: String): Boolean {
        return preferences!!.contains(key)
    }

    //
    //  Get
    //

    fun getBoolean(key: String, def: Boolean): Boolean {
        if (preferences == null) init()
        return preferences!!.getBoolean(key, def)
    }

    fun getInt(key: String, def: Int): Int {
        if (preferences == null) init()
        return preferences!!.getInt(key, def)
    }

    fun getLong(key: String, def: Long): Long {
        if (preferences == null) init()
        return preferences!!.getLong(key, def)
    }

    fun getFloat(key: String, def: Float): Float {
        if (preferences == null) init()
        return preferences!!.getFloat(key, def)
    }

    fun getString(key: String, string: String?): String? {
        if (preferences == null) init()
        return preferences!!.getString(key, string)
    }

    fun getBytes(key: String): ByteArray? {
        val s = preferences!!.getString(key, null) ?: return null
        return s.toByteArray()
    }

    fun getJson(key: String): Json? {
        val s = getString(key, null) ?: return null
        return Json(s)
    }

    fun getJsonArray(key: String, def: JsonArray): JsonArray {
        val string = getString(key, null)
        return if (string == null || string.isEmpty()) def else JsonArray(string)

    }

    //
    //  Put
    //

    fun put(key: String, v: Boolean?) {
        if(v == null){
            clear(key)
            return
        }
        if (preferences == null) init()
        preferences!!.edit().putBoolean(key, v).apply()
    }

    fun put(key: String, v: Int?) {
        if(v == null){
            clear(key)
            return
        }
        if (preferences == null) init()
        preferences!!.edit().putInt(key, v).apply()
    }

    fun put(key: String, v: Long?) {
        if(v == null){
            clear(key)
            return
        }
        if (preferences == null) init()
        preferences!!.edit().putLong(key, v).apply()
    }

    fun put(key: String, v: Float?) {
        if(v == null){
            clear(key)
            return
        }
        if (preferences == null) init()
        preferences!!.edit().putFloat(key, v).apply()
    }

    fun put(key: String, v: String?) {
        if(v == null){
            clear(key)
            return
        }
        if (preferences == null) init()
        preferences!!.edit().putString(key, v).apply()
    }

    fun put(key: String, v: ByteArray?) {
        if(v == null){
            clear(key)
            return
        }
        if (preferences == null) init()
        preferences!!.edit().putString(key, String(v)).apply()
    }

    fun put(key: String, v: Json?) {
        if(v == null){
            clear(key)
            return
        }
        put(key, v.toString())
    }

    fun put(key: String, v: JsonArray?) {
        if(v == null){
            clear(key)
            return
        }
        if (preferences == null) init()
        preferences!!.edit().putString(key, v.toString()).apply()
    }

    //
    //  Remove
    //

    fun clear(key: String) {
        if (preferences == null) init()
        preferences!!.edit().remove(key).apply()
    }


    //
    //  Files
    //

    fun saveImageInDownloadFolder(bitmap: Bitmap, onComplete: (File) -> Unit = {}) {
        if (preferences == null) init()
        ToolsPermission.requestWritePermission {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs()
            val f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + externalFileNamePrefix + "_" + System.currentTimeMillis() + ".png")
            try {
                val out = FileOutputStream(f)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.close()
                ToolsThreads.main { onComplete.invoke(f) }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

            //  Without this, the picture will be hidden until open gallery.
            if(SupAndroid.activity != null) {
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = Uri.fromFile(f)
                SupAndroid.activity!!.sendBroadcast(mediaScanIntent)
            }

        }



    }

    fun saveFileInDownloadFolder(bytes: ByteArray, ex: String, onComplete: (File) -> Unit, onPermissionPermissionRestriction: (String)->Unit = {}) {
        if (preferences == null) init()
        saveFile(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + externalFileNamePrefix + "_" + System.currentTimeMillis() + "." + ex).absolutePath,
                bytes, onComplete, onPermissionPermissionRestriction)
    }

    fun saveFile(patch: String, bytes: ByteArray, onComplete: (File) -> Unit, onPermissionPermissionRestriction: (String)->Unit = {}) {
        if (preferences == null) init()
        ToolsPermission.requestWritePermission({
            val f = File(patch)
            f.delete()
            if (f.parentFile != null) f.parentFile.mkdirs()
            try {
                val out = FileOutputStream(f)
                out.write(bytes)
                out.close()
                ToolsThreads.main { onComplete.invoke(f) }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }, onPermissionPermissionRestriction)
    }
}