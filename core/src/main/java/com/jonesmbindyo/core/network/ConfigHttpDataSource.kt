package com.jonesmbindyo.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/** Fetches remote JSON configuration over HTTP. */
class ConfigHttpDataSource(
    private val client: OkHttpClient = HttpClientProvider.client,
) {

    /**
     * Performs a GET request and returns the response body as a [String].
     *
     * @throws IOException if the request fails or the response is not successful.
     */
    suspend fun fetchConfigJson(url: String): String = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).get().build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected response code ${response.code} for $url")
            }
            response.body?.string() ?: throw IOException("Empty response body for $url")
        }
    }
}

