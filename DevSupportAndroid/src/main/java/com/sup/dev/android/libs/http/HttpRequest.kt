package com.sup.dev.android.libs.http

import android.content.ContentValues
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsThreads
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/*
    https://github.com/gjudkins/GjuddyRequest
 */
class HttpRequest(private val url: String) {

    private var connectionTimeout = 3000
    private var readTimeout = 5000

    private val params = ContentValues()
    private val headers = ContentValues()
    private var body: String? = null
    private var format: Format? = null
    private var method: Method? = null
    private var followUnsafeRedirects = false

    enum class Format {
        x_www_form_urlencoded, json
    }

    private enum class Method {
        POST, GET, PUT, DELETE
    }


    @JvmOverloads
    fun makeGet(onResult: (String)->Unit, onError: ((Exception) -> Unit)? = null): HttpRequest {
        method = Method.GET
        return make(onResult, onError)
    }

    @JvmOverloads
    fun makePost(onResult: (String)->Unit, onError: ((Exception) -> Unit)? = null): HttpRequest {
        method = Method.POST
        return make(onResult, onError)
    }

    fun makePut(onResult: (String)->Unit): HttpRequest {
        method = Method.PUT
        return makePut(onResult, null)
    }

    fun makePut(onResult: (String)->Unit, onError: ((Exception) -> Unit)?): HttpRequest {
        method = Method.PUT
        return make(onResult, onError)
    }

    fun makeDelete(onResult: (String)->Unit): HttpRequest {
        return make(onResult, null)
    }

    fun makeDelete(onResult: (String)->Unit, onError: ((Exception) -> Unit)? = null): HttpRequest {
        method = Method.DELETE
        return makeDelete(onResult, onError)
    }

    @Throws(Exception::class)
    fun makeGet(): String {
        method = Method.GET
        return make()
    }

    @Throws(Exception::class)
    fun makePost(): String {
        method = Method.POST
        return make()
    }

    @Throws(Exception::class)
    fun makePut(): String {
        method = Method.PUT
        return make()
    }

    @Throws(Exception::class)
    fun makeDelete(): String {
        method = Method.DELETE
        return make()
    }


    private fun make(onResult: (String)->Unit?, onError: ((Exception) -> Unit)?): HttpRequest {
        ToolsThreads.thread {
            try {
                onResult.invoke(make())
            } catch (e: Exception) {
                if (onError != null)
                    onError.invoke(e)
                else
                    log(e)
            }
        }
        return this
    }

    @Throws(Exception::class)
    private fun make(): String {
        return makeNow(url)
    }

    @Throws(Exception::class)
    private fun makeNow(urlStr: String): String {

        if (format == Format.x_www_form_urlencoded)
            headers.put("Content-Type", "application/x-www-form-urlencoded")
        else if (format == Format.json) headers.put("Content-Type", "application/json")


        val connection = createConnection(urlStr + makeQueryString(params, true), method!!.name)

        for (header in headers.keySet()) connection.setRequestProperty(header, headers.getAsString(header))

        if (body != null && !body!!.isEmpty()) {
            connection.setFixedLengthStreamingMode(body!!.toByteArray().size)
            val out = PrintWriter(connection.outputStream)
            out.print(body)
            out.close()
        }

        if (isCrossProtocolRedirect(urlStr, connection)) {
            if (followUnsafeRedirects) {
                val newUrl = connection.getHeaderField("Location")
                return makeNow(newUrl)
            } else {
                throw ExceptionUnsafeRedirects()
            }
        }

        return readInputStream(connection.inputStream)
    }

    @Throws(IOException::class)
    private fun readInputStream(inputStream: InputStream): String {
        val sb = StringBuilder()
        val reader = BufferedReader(InputStreamReader(inputStream), 8)
        var line: String
        while (true) {
            line = reader.readLine()
            if (line == null) break
            sb.append(line).append("\n")
        }
        inputStream.close()

        return sb.toString()
    }


    //
    //  Setters
    //


    fun setFollowUnsafeRedirects(followUnsafeRedirects: Boolean): HttpRequest {
        this.followUnsafeRedirects = followUnsafeRedirects
        return this
    }

    fun setConnectionTimeout(connectionTimeout: Int): HttpRequest {
        this.connectionTimeout = connectionTimeout
        return this
    }

    fun setReadTimeout(readTimeout: Int): HttpRequest {
        this.readTimeout = readTimeout
        return this
    }

    fun setFormat(format: Format): HttpRequest {
        this.format = format
        return this
    }

    fun header(key: String, value: Any): HttpRequest {
        headers.put(key, value.toString())
        return this
    }

    fun param(key: String, param: Any): HttpRequest {
        params.put(key, param.toString())
        return this
    }

    fun body(body: String): HttpRequest {
        this.body = body
        return this
    }

    //
    //  Connection
    //

    @Throws(Exception::class)
    private fun createConnection(urlStr: String, method: String): HttpURLConnection {
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = connectionTimeout
        connection.readTimeout = readTimeout
        connection.doOutput = method != "GET"
        connection.doInput = true
        connection.requestMethod = method

        return connection
    }

    @Throws(IOException::class)
    private fun isCrossProtocolRedirect(originalUrl: String, connection: HttpURLConnection): Boolean {
        val code = connection.responseCode
        if (code == 301 || code == 302) {
            val newUrl = connection.getHeaderField("Location")

            val protocol1 = URL(originalUrl).protocol
            val protocol2 = URL(newUrl).protocol

            return protocol1 != protocol2
        }
        return false
    }

    private fun makeQueryString(values: ContentValues?, urlEncode: Boolean, includeQuestionMark: Boolean = true): String {
        if (values != null) {
            val sb = StringBuilder()
            var count = 0
            for (key in values.keySet()) {
                if (count == 0 && includeQuestionMark)
                    sb.append("?")
                else if (count > 0)
                    sb.append("&")
                sb.append(if (urlEncode) urlEncodeString(key) else key)
                sb.append("=")
                sb.append(if (urlEncode) urlEncodeString(values.getAsString(key)) else values.getAsString(key))
                count++
            }

            return sb.toString()
        } else {
            return ""
        }
    }

    private fun urlEncodeString(urlString: String): String {
        var urlString = urlString
        try {
            urlString = URLEncoder.encode(urlString, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return urlString
    }

    //
    //  Specials
    //

    fun disableCertVerification() {
        var ctx: SSLContext? = null
        try {
            ctx = SSLContext.getInstance("TLS")
        } catch (e: NoSuchAlgorithmException) {
            log(e)
        }

        try {
            ctx!!.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }), null)
        } catch (e: KeyManagementException) {
            log(e)
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(ctx!!.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
    }

}//
//  Format
//
