package com.jonesmbindyo.data.cache

import java.io.File
import java.security.MessageDigest

/**
 * Stores and retrieves raw asset bytes under `spinwheel-cache/assets/`.
 * Files are named by the SHA-256 hash of the source URL.
 */
class AssetCache(cacheRoot: File) {

    private val dir = cacheRoot.resolve("assets").also { it.mkdirs() }

    /** Cache [File] for [url]. */
    fun getFileForUrl(url: String): File = dir.resolve(url.sha256())

    /** `true` if [url] is already cached on disk. */
    fun exists(url: String): Boolean = getFileForUrl(url).exists()

    /** Writes [bytes] to the cache file for [url]. */
    fun write(url: String, bytes: ByteArray) {
        getFileForUrl(url).writeBytes(bytes)
    }


    private fun String.sha256(): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }
}

