package com.jonesmbindyo.core.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/** Provides a shared [OkHttpClient] with standard SDK timeouts. */
object HttpClientProvider {

    private const val TIMEOUT_SECONDS = 15L

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }
}

