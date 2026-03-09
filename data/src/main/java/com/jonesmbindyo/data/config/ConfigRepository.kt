package com.jonesmbindyo.data.config

import com.jonesmbindyo.core.config.RemoteConfig
import com.jonesmbindyo.core.dto.WidgetResponse
import com.jonesmbindyo.core.dto.toRemoteConfig
import com.jonesmbindyo.core.network.ConfigHttpDataSource
import com.jonesmbindyo.data.assets.normalizeDriveUrl
import com.jonesmbindyo.data.cache.JsonCache
import com.jonesmbindyo.data.prefs.SpinWheelPrefs
import kotlinx.serialization.json.Json
import java.io.IOException

/** Fetches, caches, and serves [RemoteConfig], with TTL and offline-fallback logic. */
class ConfigRepository(
    private val httpSource: ConfigHttpDataSource,
    private val jsonCache: JsonCache,
    private val prefs: SpinWheelPrefs,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {

    /**
     * Returns the current [RemoteConfig].
     *
     * - Returns cached config if TTL has not expired, unless [forceRefresh] is `true`.
     * - On network failure, falls back to cached config if available.
     *
     * @throws IOException if no cached config exists and the network request fails.
     */
    suspend fun getConfig(
        configUrl: String,
        forceRefresh: Boolean = false,
    ): RemoteConfig {
        if (!forceRefresh && isCacheValid()) {
            jsonCache.read()?.let { return it.deserialize() }
        }

        return try {
            val responseJson = httpSource.fetchConfigJson(normalizeDriveUrl(configUrl))
            jsonCache.write(responseJson)
            prefs.lastConfigFetchTimeMs = System.currentTimeMillis()
            responseJson.deserialize()
        } catch (e: IOException) {
            jsonCache.read()?.deserialize()
                ?: throw IOException("Config fetch failed and no cached config available.", e)
        }
    }

    private fun isCacheValid(): Boolean {
        val fetchTimeMs = prefs.lastConfigFetchTimeMs
        if (fetchTimeMs == 0L) return false
        val ttlSeconds = jsonCache.read()
            ?.let { runCatching { it.deserialize() }.getOrNull() }
            ?.cacheExpiration
            ?: DEFAULT_TTL_SECONDS
        return System.currentTimeMillis() - fetchTimeMs < ttlSeconds * 1_000L
    }

    /** Parses raw JSON into [WidgetResponse] then maps to the internal [RemoteConfig]. */
    private fun String.deserialize(): RemoteConfig =
        json.decodeFromString<WidgetResponse>(this).toRemoteConfig()

    companion object {
        private const val DEFAULT_TTL_SECONDS = 3600
    }
}