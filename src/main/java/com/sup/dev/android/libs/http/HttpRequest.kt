package com.sup.dev.android.libs.http

import android.content.ContentValues
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.libs.debug.info
import com.sup.dev.java.libs.json.Json
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
    private var format = Format.json
    private var method = Method.GET
    private var followUnsafeRedirects = false
    private var authorizationType: String? = null
    private var authorization: String? = null

    enum class Format {
        x_www_form_urlencoded, json
    }

    enum class Method {
        POST, GET, PUT, DELETE
    }

    fun make(onResult: (String) -> Unit?, onError: ((Exception) -> Unit)?): HttpRequest {
        ToolsThreads.thread {
            try {
                val result = makeNow(url)
                ToolsThreads.main { onResult.invoke(result) }
            } catch (e: Exception) {
                info("XRequest", "<- ERROR [$e]")
                err(e)
                if (onError != null)
                    onError.invoke(e)
                else
                    err(e)
            }
        }
        return this
    }

    @Throws(Exception::class)
    private fun makeNow(urlStr: String): String {

        if (format == Format.x_www_form_urlencoded) headers.put("Content-Type", "application/x-www-form-urlencoded")
        if (authorization != null)  headers.put("Authorization", "$authorizationType $authorization")
        else if (format == Format.json) headers.put("Content-Type", "application/json")

        val request = urlStr + makeQueryString(params, true)
        info("XRequest", "-> [$request] [$body]")
        val connection = createConnection(request, method.name)

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

        val code = connection.responseCode
        var result = ""
        try {
            result = readInputStream(connection.inputStream)
        } catch (e: Exception) {

        }
        info("XRequest", "<- [$request] [$code] [$result]")

        if (code != 200) throw IllegalArgumentException("Code[$code] is not 200.")

        return result
    }

    @Throws(IOException::class)
    private fun readInputStream(inputStream: InputStream): String {
        val sb = StringBuilder()
        val reader = BufferedReader(InputStreamReader(inputStream), 8)
        var line: String?
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

    fun setMethods(method: Method): HttpRequest {
        this.method = method
        return this
    }

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

    fun setBody(body: String): HttpRequest {
        this.body = body
        return this
    }

    fun setJson(json: Json): HttpRequest {
        this.body = json.toString()
        return this
    }

    fun setAuthorization(authorizationType: String, authorization: String): HttpRequest{
        this.authorizationType = authorizationType
        this.authorization = authorization
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
        var urlStringV = urlString
        try {
            urlStringV = URLEncoder.encode(urlStringV, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return urlStringV
    }

    //
    //  Specials
    //

    fun disableCertVerification() {
        var ctx: SSLContext? = null
        try {
            ctx = SSLContext.getInstance("TLS")
        } catch (e: NoSuchAlgorithmException) {
            err(e)
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
            err(e)
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(ctx!!.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
    }

}//
//  Format
//
