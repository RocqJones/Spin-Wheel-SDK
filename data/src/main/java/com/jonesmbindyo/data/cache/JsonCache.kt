package com.jonesmbindyo.data.cache

import java.io.File

/**
 * Reads and writes the remote config JSON to `spinwheel-cache/config.json`.
 */
class JsonCache(cacheRoot: File) {

    private val file = cacheRoot.resolve("config.json")

    /** Returns the cached JSON string, or `null` if nothing has been written yet. */
    fun read(): String? = if (file.exists()) file.readText() else null

    /** Overwrites `config.json` with [json]. */
    fun write(json: String) {
        file.writeText(json)
    }
}

