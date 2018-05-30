package com.sup.dev.android.libs.http;

import java.io.IOException;

public class ExceptionUnsafeRedirects extends IOException{

    public ExceptionUnsafeRedirects(){
        super("Unsafe redirect. To follow unsafe redirects, set followUnsafeRedirects to 'true'");
    }

}
