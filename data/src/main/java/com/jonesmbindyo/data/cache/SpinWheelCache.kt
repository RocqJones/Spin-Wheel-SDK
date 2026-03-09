package com.jonesmbindyo.data.cache

import java.io.File

/**
 * Entry point for the SDK's disk cache.
 *
 * Creates and owns the `spinwheel-cache/` directory inside [filesDir],
 * and provides access to the [JsonCache] and [AssetCache] sub-caches.
 *
 * @param filesDir The app's private files directory (i.e. `Context.filesDir`).
 */
class SpinWheelCache(filesDir: File) {

    private val root = filesDir.resolve("spinwheel-cache").also { it.mkdirs() }

    val json  = JsonCache(root)
    val asset = AssetCache(root)

    /** Deletes the entire cache directory and all of its contents. */
    fun clearAll() {
        root.deleteRecursively()
    }
}

