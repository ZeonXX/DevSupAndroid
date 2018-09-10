package com.sup.dev.android.libs.http

import java.io.IOException

class ExceptionUnsafeRedirects : IOException("Unsafe redirect. To follow unsafe redirects, set followUnsafeRedirects to 'true'")
