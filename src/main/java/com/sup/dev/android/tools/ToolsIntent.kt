package com.sup.dev.android.tools

import android.app.Activity
import android.app.Service
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.core.content.FileProvider
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.classes.items.Item2
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsText
import java.io.*
import java.net.URLConnection
import java.util.*


object ToolsIntent {

    private val SHARE_FOLDER = "sup_share_cash"

    private var codeCounter = 0
    private val progressIntents = ArrayList<Item2<Int, (Int, Intent?) -> Unit>?>()
    private val onActivityNotFoundDef: () -> Unit = {ToolsToast.show(SupAndroid.TEXT_ERROR_APP_NOT_FOUND)}

    //
    //  Support
    //

    private val cashRoot: String
        get() = SupAndroid.appContext!!.externalCacheDir!!.absolutePath + SHARE_FOLDER

    fun startIntentForResult(intent: Intent, onResult: (Int, Intent?) -> Unit) {
        if (codeCounter == 65000)
            codeCounter = 0
        val code = codeCounter++
        progressIntents.add(Item2(code, onResult))
        SupAndroid.activity!!.startActivityForResult(intent, code)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        for (pair in progressIntents)
            if (pair != null && requestCode == pair.a1) {
                progressIntents.remove(pair)
                pair.a2.invoke(resultCode, resultIntent)
                return
            }

    }

    fun openApp(stringID: Int) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(ToolsResources.s(stringID))
        SupAndroid.appContext!!.startActivity(intent)
    }

    fun parseExtras(intent: Intent, onNext:(key:String,value:Any) -> Unit){
        val extras = intent.extras
        if(extras != null){
            val keySet = extras.keySet()
            for(k in keySet){
                onNext.invoke(k, extras.get(k))
            }
        }
    }

    //
    //  Intents
    //

    fun startWeb(link: String, onActivityNotFound: ()->Unit) {
        startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(ToolsText.castToWebLink(link)))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound)
    }

    fun startIntent(intent: Intent, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        try {
            SupAndroid.appContext!!.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            err(ex)
            onActivityNotFound.invoke()
        }

    }

    fun openLink(link: String, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(ToolsText.castToWebLink(link)))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound)
    }

    fun startApp(packageName: String, onActivityNotFound: () -> Unit = {}) {
        startApp(packageName, {}, onActivityNotFound)
    }

    fun startApp(packageName: String, onIntentCreated: (Intent) -> Unit = {}, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        val manager = SupAndroid.appContext!!.packageManager
        val intent = manager.getLaunchIntentForPackage(packageName)
        if (intent == null) {
            onActivityNotFound.invoke()
            return
        }
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        onIntentCreated.invoke(intent)
        startIntent(intent, onActivityNotFound)
    }

    fun startPlayMarket(packageName: String, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        startIntent(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName&reviewId=0"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound)
    }

    fun startMail(mail: String, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        startIntent(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$mail"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound)
    }

    fun startPhone(phone: String, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        startIntent(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound)
    }

    fun shareImage(bitmap: Bitmap, text: String, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {

        val files = File(cashRoot).listFiles()
        if (files != null)
            for (f in files)
                if (f.name.contains("x_share_i"))
                    f.delete()

        File(cashRoot).mkdirs()

        val patch = cashRoot + System.currentTimeMillis() + "_x_share_i.png"

        val out: OutputStream
        val file = File(patch)
        try {
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            err(e)
        }

        try {
            SupAndroid.activity!!.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(SupAndroid.activity!!, "${SupAndroid.appId}.fileprovider", file))
                    .putExtra(Intent.EXTRA_TEXT, text)
                    .setType("image/*"), null))
        } catch (ex: ActivityNotFoundException) {
            err(ex)
            onActivityNotFound.invoke()
        }

    }

    fun shareFile(patch: String, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        val fileUti = FileProvider.getUriForFile(SupAndroid.appContext!!, "${SupAndroid.appId}.fileprovider", File(patch))
        shareFile(fileUti, onActivityNotFound)
    }

    fun shareFile(patch: String, type: String, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        val fileUti = FileProvider.getUriForFile(SupAndroid.appContext!!, "${SupAndroid.appId}.fileprovider", File(patch))
        shareFile(fileUti, type, onActivityNotFound)
    }

    fun shareFile(uri: Uri, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        shareFile(uri, URLConnection.guessContentTypeFromName(uri.toString()), onActivityNotFound)
    }

    fun shareFile(uri: Uri, type: String, onActivityNotFound: () -> Unit = {}) {
        shareFile(uri, type, null, onActivityNotFound)
    }

    fun shareFile(uri: Uri, type: String, text: String?, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        try {
            val i = Intent()
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_STREAM, uri)
                    .setType(type)
            if (text != null) i.putExtra(Intent.EXTRA_TEXT, text)
            SupAndroid.activity!!.startActivity(Intent.createChooser(i, null))
        } catch (ex: ActivityNotFoundException) {
            err(ex)
            onActivityNotFound.invoke()
        }

    }

    fun shareText(text: String, onActivityNotFound: () -> Unit = onActivityNotFoundDef) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setType("vText/plain")
                .putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                .putExtra(Intent.EXTRA_TEXT, text)
        startIntent(Intent.createChooser(sharingIntent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound)
    }

    //
    //  Intents result
    //

    fun getGalleryImage(onResult: (ByteArray)->Unit, onError: ()->Unit={}) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        try {
            startIntentForResult(Intent.createChooser(intent, null)) { resultCode, resultIntent ->
                try{
                    if (resultCode != Activity.RESULT_OK || resultIntent == null || resultIntent.data == null) throw IllegalAccessException("Result is null or not OK")
                    val inp = SupAndroid.appContext!!.contentResolver.openInputStream(resultIntent.data)
                    val bytes = ByteArray(inp.available())
                    inp.read(bytes)
                    inp.close()
                    onResult.invoke(bytes)
                }catch (e:Exception){
                    err(e)
                    onError.invoke()
                }

            }
        } catch (ex: ActivityNotFoundException) {
            err(ex)
            onError.invoke()
        }

    }

    //
    //  Services / Activities
    //


    fun startServiceForeground(serviceClass: Class<out Service>, vararg extras: Any) {
        val intent = Intent(SupAndroid.appContext!!, serviceClass)
        addExtras(intent, *extras)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            SupAndroid.appContext!!.startForegroundService(intent)
        else
            SupAndroid.appContext!!.startService(intent)

    }

    fun startService(serviceClass: Class<out Service>, vararg extras: Any) {
        val intent = Intent(SupAndroid.appContext!!, serviceClass)
        addExtras(intent, *extras)
        SupAndroid.appContext!!.startService(intent)
    }

    fun startActivity(viewContext: Context, activityClass: Class<out Activity>, vararg extras: Any) {
        startActivity(viewContext, activityClass, null, *extras)
    }

    fun startActivity(viewContext: Context, activityClass: Class<out Activity>, flags: Int?, vararg extras: Any) {
        val intent = Intent(viewContext, activityClass)

        addExtras(intent, *extras)

        if (flags != null)
            intent.addFlags(flags)

        viewContext.startActivity(intent)
    }

    fun addExtras(intent: Intent, vararg extras: Any) {
        var i = 0
        while (i < extras.size) {
            val extra = extras[i + 1]
            if (extra is Parcelable)
                intent.putExtra(extras[i] as String, extra)
            else if (extra is Serializable)
                intent.putExtra(extras[i] as String, extra)
            else
                throw IllegalArgumentException("Extras must be instance of Parcelable or Serializable")
            i += 2
        }
    }



}
