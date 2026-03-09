package com.jonesmbindyo.data.assets

import com.jonesmbindyo.core.config.WheelAssets
import com.jonesmbindyo.core.network.AssetHttpDataSource
import com.jonesmbindyo.data.cache.AssetCache
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File

/**
 * Downloads and caches wheel assets, with Google Drive share-link normalisation.
 *
 * @param httpSource Downloads raw bytes for a given URL.
 * @param assetCache Reads and writes asset files keyed by URL.
 */
class AssetRepository(
    private val httpSource: AssetHttpDataSource,
    private val assetCache: AssetCache,
) {

    /**
     * Returns the local [File] for [url], downloading and caching it first if necessary.
     * Google Drive share links are normalised to direct-download URLs before use.
     */
    suspend fun getAssetFile(url: String): File {
        // Normalise Drive share links → direct download URL
        val downloadUrl = normalizeDriveUrl(url)

        // Return immediately if already cached — no download needed
        if (assetCache.exists(downloadUrl)) {
            return assetCache.getFileForUrl(downloadUrl)
        }

        // Download bytes from network
        val bytes = httpSource.downloadBytes(downloadUrl)

        // Persist to disk and return the cache file
        assetCache.write(downloadUrl, bytes)
        return assetCache.getFileForUrl(downloadUrl)
    }

    /**
     * Downloads all four wheel assets in parallel.
     * Assets that are already cached are skipped automatically via [getAssetFile].
     */
    suspend fun prefetchAssets(resolved: WheelAssets): Unit = coroutineScope {
        val background = async { getAssetFile(resolved.background) }
        val frame = async { getAssetFile(resolved.frame) }
        val wheel = async { getAssetFile(resolved.wheel) }
        val spinButton = async { getAssetFile(resolved.spinButton) }

        // Await all — exceptions from any download propagate to the caller
        background.await()
        frame.await()
        wheel.await()
        spinButton.await()
    }
}

private val DRIVE_FILE_REGEX = Regex("""drive\.google\.com/file/d/([^/?]+)""")

/**
 * Converts a Google Drive share link into a direct-download URL.
 * Non-Drive URLs are returned unchanged.
 *
 * Example:
 * `https://drive.google.com/file/d/<ID>/view?usp=drive_link`
 * → `https://drive.google.com/uc?export=download&id=<ID>`
 */
fun normalizeDriveUrl(url: String): String {
    val fileId = DRIVE_FILE_REGEX.find(url)?.groupValues?.get(1) ?: return url
    return "https://drive.google.com/uc?export=download&id=$fileId"
}

