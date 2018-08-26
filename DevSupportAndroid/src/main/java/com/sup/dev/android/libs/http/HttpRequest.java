package com.sup.dev.android.libs.http;

import android.content.ContentValues;

import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
    https://github.com/gjudkins/GjuddyRequest
 */
public class HttpRequest {

    private int connectionTimeout = 3000;
    private int readTimeout = 5000;

    public enum Format {x_www_form_urlencoded, json}

    private enum Method {POST, GET, PUT, DELETE}

    private ContentValues params = new ContentValues();
    private ContentValues headers = new ContentValues();
    private String body;
    private String url;
    private Format format;
    private Method method;
    private boolean followUnsafeRedirects = false;

    public HttpRequest(String url) {
        this.url = url;
    }

    public HttpRequest makeGet(Callback1<String> onResult) {

        return makeGet(onResult, null);
    }


    public HttpRequest makeGet(Callback1<String> onResult, Callback1<Exception> onError) {
        method = Method.GET;
        return make(onResult, onError);
    }

    public HttpRequest makePost(Callback1<String> onResult) {
        return makePost(onResult, null);
    }

    public HttpRequest makePost(Callback1<String> onResult, Callback1<Exception> onError) {
        method = Method.POST;
        return make(onResult, onError);
    }

    public HttpRequest makePut(Callback1<String> onResult) {
        method = Method.PUT;
        return makePut(onResult, null);
    }

    public HttpRequest makePut(Callback1<String> onResult, Callback1<Exception> onError) {
        method = Method.PUT;
        return make(onResult, onError);
    }

    public HttpRequest makeDelete(Callback1<String> onResult) {
        return make(onResult, null);
    }

    public HttpRequest makeDelete(Callback1<String> onResult, Callback1<Exception> onError) {
        method = Method.DELETE;
        return makeDelete(onResult, onError);
    }

    public String makeGet() throws Exception {
        method = Method.GET;
        return make();
    }

    public String makePost() throws Exception {
        method = Method.POST;
        return make();
    }

    public String makePut() throws Exception {
        method = Method.PUT;
        return make();
    }

    public String makeDelete() throws Exception {
        method = Method.DELETE;
        return make();
    }


    private HttpRequest make(Callback1<String> onResult, Callback1<Exception> onError) {
        ToolsThreads.thread(() -> {
            try {
                if (onResult != null) onResult.callback(make());
            } catch (Exception e) {
                if (onError != null) onError.callback(e);
                else Debug.log(e);
            }
        });
        return this;
    }

    private String make() throws Exception {
        return makeNow(url);
    }

    private String makeNow(String urlStr) throws Exception {

        if (format == Format.x_www_form_urlencoded) headers.put("Content-Type", "application/x-www-form-urlencoded");
        else if (format == Format.json) headers.put("Content-Type", "application/json");


        HttpURLConnection connection = createConnection(urlStr + makeQueryString(params, true), method.name());

        for (String header : headers.keySet()) connection.setRequestProperty(header, headers.getAsString(header));

        if (body != null && !body.isEmpty()) {
            connection.setFixedLengthStreamingMode(body.getBytes().length);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(body);
            out.close();
        }

        if (isCrossProtocolRedirect(urlStr, connection)) {
            if (followUnsafeRedirects) {
                String newUrl = connection.getHeaderField("Location");
                return makeNow(newUrl);
            } else {
                throw new ExceptionUnsafeRedirects();
            }
        }

        return readInputStream(connection.getInputStream());
    }

    private String readInputStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8);
        String line;
        while ((line = reader.readLine()) != null) sb.append(line).append("\n");
        is.close();

        return sb.toString();
    }


    //
    //  Setters
    //


    public HttpRequest setFollowUnsafeRedirects(boolean followUnsafeRedirects) {
        this.followUnsafeRedirects = followUnsafeRedirects;
        return this;
    }

    public HttpRequest setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public HttpRequest setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public HttpRequest setFormat(Format format) {
        this.format = format;
        return this;
    }

    public HttpRequest header(String key, Object value) {
        headers.put(key, value.toString());
        return this;
    }

    public HttpRequest param(String key, Object param) {
        params.put(key, param.toString());
        return this;
    }

    public HttpRequest body(String body) {
        this.body = body;
        return this;
    }

    //
    //  Connection
    //

    private HttpURLConnection createConnection(String urlStr, String method) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        if (method.equals("GET"))
            connection.setDoOutput(false);
        else
            connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(method);

        return connection;
    }

    private static boolean isCrossProtocolRedirect(String originalUrl, HttpURLConnection connection) throws IOException {
        int code = connection.getResponseCode();
        if (code == 301 || code == 302) {
            String newUrl = connection.getHeaderField("Location");

            String protocol1 = new URL(originalUrl).getProtocol();
            String protocol2 = new URL(newUrl).getProtocol();

            return !protocol1.equals(protocol2);
        }
        return false;
    }


    //
    //  Format
    //

    private static String makeQueryString(ContentValues values, boolean urlEncode) {
        return makeQueryString(values, urlEncode, true);
    }

    private static String makeQueryString(ContentValues values, boolean urlEncode, boolean includeQuestionMark) {
        if (values != null) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (String key : values.keySet()) {
                if (count == 0 && includeQuestionMark)
                    sb.append("?");
                else if (count > 0)
                    sb.append("&");
                sb.append(urlEncode ? urlEncodeString(key) : key);
                sb.append("=");
                sb.append(urlEncode ? urlEncodeString(values.getAsString(key)) : values.getAsString(key));
                count++;
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    private static String urlEncodeString(String urlString) {
        try {
            urlString = URLEncoder.encode(urlString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlString;
    }

    //
    //  Specials
    //

    public void disableCertVerification() {
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            Debug.log(e);
        }
        try {
            ctx.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            }, null);
        } catch (KeyManagementException e) {
            Debug.log(e);
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

}
